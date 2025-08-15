package cn.easygd.dynaguard.core.listener;

import cn.easygd.dynaguard.core.chain.ValidationChain;
import cn.easygd.dynaguard.core.chain.ValidationChainManager;
import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.core.holder.GlobalBeanContextHolder;
import cn.easygd.dynaguard.core.parser.sql.ChainSqlDO;
import cn.easygd.dynaguard.domain.ValidationNode;
import cn.easygd.dynaguard.domain.config.ChainSqlConfig;
import cn.easygd.dynaguard.domain.constants.ParserTypeEnumerable;
import cn.easygd.dynaguard.domain.enums.ParserTypeEnum;
import cn.easygd.dynaguard.utils.ChainSqlUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * sql流程监听器
 *
 * @author VD
 * @date 2025/8/10 16:38
 */
public class ChainSqlListener implements ValidationChainListener {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ChainSqlListener.class);

    /**
     * 注册
     */
    @Override
    public void register() {
        ChainSqlConfig sqlConfig = (ChainSqlConfig) ChainConfigHolder.getDataConfig(ParserTypeEnum.SQL.getType());
        if (!sqlConfig.getEnableListener()) {
            log.info("chain sql listener not enabled");
            return;
        }

        // 核心线程数量
        Long corePoolSize = sqlConfig.getCorePoolSize();
        // 自定义线程工厂（指定线程名、优先级等）
        ThreadFactory threadFactory = r -> {
            Thread thread = new Thread(r, "scheduled-pool-validation chain sql listener");
            // 非守护线程（避免任务未完成JVM退出）
            thread.setDaemon(false);
            // 正常优先级
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        };
        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(corePoolSize.intValue(), threadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());

        // 启动定时任务
        scheduler.scheduleWithFixedDelay(() -> {
                    try {
                        execute(sqlConfig);
                    } catch (Exception e) {
                        log.warn("chain sql scheduled catch exception", e);
                    }
                },
                sqlConfig.getFirstListenerInterval(),
                sqlConfig.getListenerInterval(),
                TimeUnit.SECONDS);

        // 将定时任务线程放到jvm关闭钩子中
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("chain sql scheduled shutdown hook");

            // 拒绝新任务，允许已提交任务完成
            scheduler.shutdown();
            try {
                // 等待 5 秒让任务完成，超时则强制关闭
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    List<Runnable> remainingTasks = scheduler.shutdownNow();
                    log.info("chain sql scheduled shutdown remaining tasks: {}", remainingTasks.size());
                }
            } catch (InterruptedException e) {
                // 等待过程中被中断，强制关闭
                scheduler.shutdownNow();
                Thread.currentThread().interrupt(); // 保留中断状态
            }
            log.info("chain sql scheduled shutdown complete");
        }, "scheduled-pool-validation chain sql listener-Shutdown-Hook"));
    }

    /**
     * 执行
     *
     * @param sqlConfig sql配置
     */
    private void execute(ChainSqlConfig sqlConfig) {
        // 获取当前时间的前2秒
        String updateTime = LocalDateTime.now()
                .minusSeconds(TimeUnit.SECONDS.toSeconds(sqlConfig.getListenerInterval()))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String sql = ChainSqlUtils.genSelectUpdateSql(sqlConfig, updateTime);
        // 查询所有更新过的chainId
        List<String> chainIdList = ChainSqlUtils.executeSql(sqlConfig, sql, ChainSqlUtils::executeSelectSql)
                .stream()
                .map(ChainSqlDO::getChainId)
                .distinct()
                .collect(Collectors.toList());

        // 捞取需更新的chainId
        String sql2 = ChainSqlUtils.genSelectByChainIdSql(sqlConfig, chainIdList);
        List<ChainSqlDO> allChainList = ChainSqlUtils.executeSql(sqlConfig, sql2, ChainSqlUtils::executeSelectSql);

        ValidationChainManager chainManager = GlobalBeanContextHolder.getContext().getChainManager();
        // 需要删除的chainId集合
        List<String> removeChainIdList = Lists.newArrayList();
        // 需要更新的chainId map
        Map<String, ValidationChain> updateChainMap = Maps.newHashMap();

        Map<String, List<ChainSqlDO>> allChainMap = allChainList.stream().collect(Collectors.groupingBy(ChainSqlDO::getChainId));
        allChainMap.forEach((chainId, doList) -> {
            boolean isDeleted = doList.stream().allMatch(ChainSqlDO::getDeleted);
            if (isDeleted) {
                removeChainIdList.add(chainId);
            } else {
                ValidationChain chain = new ValidationChain();
                chain.setGroup(type().getType());
                chain.setChainId(chainId);
                doList.stream().findFirst().ifPresent(source -> {
                    chain.setGuardExpire(source.getGuardExpire());
                    chain.setGuardThreshold(source.getGuardThreshold());
                });
                List<ValidationNode> nodeList = doList.stream()
                        .filter(source -> !source.getDeleted())
                        .sorted(Comparator.comparingInt(ChainSqlDO::getOrder))
                        .map(ChainSqlDO::converter2node)
                        .collect(Collectors.toList());
                chain.setNodes(nodeList);
                updateChainMap.put(chainId, chain);
            }
        });

        chainManager.removeChain(type().getType(), removeChainIdList.toArray(new String[0]));
        chainManager.updateChain(type().getType(), updateChainMap);
    }

    /**
     * 类型
     *
     * @return 类型
     */
    @Override
    public ParserTypeEnumerable type() {
        return ParserTypeEnum.SQL;
    }
}
