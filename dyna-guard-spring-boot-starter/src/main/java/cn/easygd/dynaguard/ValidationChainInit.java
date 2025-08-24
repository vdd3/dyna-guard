package cn.easygd.dynaguard;

import cn.easygd.dynaguard.core.chain.ValidationChainManager;
import cn.easygd.dynaguard.core.guard.CounterGuard;
import cn.easygd.dynaguard.core.guard.CounterGuardManager;
import cn.easygd.dynaguard.core.metrics.BizValidationStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 验证流程初始化
 *
 * @author VD
 * @version v 0.1 2025/8/12 20:05
 */
public class ValidationChainInit implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ValidationChainInit.class);

    /**
     * 默认统计实现名称
     */
    private static final String DEFAULT_STATISTICS_BEAN_NAME = "localBizValidationStatistics";

    /**
     * 验证链管理器
     */
    private ValidationChainManager validationChainManager;

    /**
     * 熔断器管理器
     */
    private CounterGuardManager counterGuardManager;

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        log.info("validation chain load chain start");

        // 初始化
        validationChainManager.init();

        // 加载流程
        validationChainManager.loadChain();

        // 将计数熔断器注册熔断器管理器中
        Map<String, CounterGuard> beans = applicationContext.getBeansOfType(CounterGuard.class);
        beans.forEach((k, v) -> {
            List<String> chainIdList = v.chainId();
            chainIdList.forEach(chainId -> counterGuardManager.register(chainId, v));
        });

        // 将业务统计器注册到全局的bean管理中，只能有一个全局业务统计器
        Set<Map.Entry<String, BizValidationStatistics>> entrySet = applicationContext.getBeansOfType(BizValidationStatistics.class).entrySet();
        if (entrySet.size() > 1) {
            entrySet.stream().filter(entry -> !DEFAULT_STATISTICS_BEAN_NAME.equals(entry.getKey()))
                    .findFirst()
                    .ifPresent(entry -> SpringBeanContext.setBizValidationStatisticsName(entry.getKey()));
        } else {
            SpringBeanContext.setBizValidationStatisticsName(DEFAULT_STATISTICS_BEAN_NAME);
        }

        log.info("validation chain load chain end");
    }

    /**
     * 注入验证链管理器
     *
     * @param validationChainManager 验证链管理器
     */
    public void setValidationChainManager(ValidationChainManager validationChainManager) {
        this.validationChainManager = validationChainManager;
    }

    /**
     * 注入熔断器管理器
     *
     * @param counterGuardManager 熔断器管理器
     */
    public void setCounterGuardManager(CounterGuardManager counterGuardManager) {
        this.counterGuardManager = counterGuardManager;
    }
}
