package cn.easygd.dynaguard.core.listener;

import cn.easygd.dynaguard.core.chain.ValidationChain;
import cn.easygd.dynaguard.core.chain.ValidationChainManager;
import cn.easygd.dynaguard.core.holder.ChainFilePathParserHolder;
import cn.easygd.dynaguard.core.holder.GlobalBeanContextHolder;
import cn.easygd.dynaguard.core.parser.ValidationChainParser;
import cn.easygd.dynaguard.core.path.ChainFilePathParser;
import cn.easygd.dynaguard.domain.config.LocalChainDataConfig;
import cn.easygd.dynaguard.domain.exception.ValidationChainListenerException;
import cn.easygd.dynaguard.utils.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 本地流程监听器
 *
 * @author VD
 * @date 2025/8/10 14:50
 */
public abstract class LocalChainListener implements ValidationChainListener {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(LocalChainListener.class);

    /**
     * 注册
     */
    @Override
    public void register() {
        ValidationChainManager chainManager = GlobalBeanContextHolder.getContext().getChainManager();
        LocalChainDataConfig config = getConfig();
        final String type = type().getType();
        if (!config.getEnableListener()) {
            log.info("[{}] not enable listener", type);
            return;
        }

        // 按照类型增加文件过滤条件
        String suffix = FileUtils.FILE_EXTENSION_SEPARATOR + type;
        // 获取文件监听时间间隔
        long interval = config.getListenerInterval();
        // 对文件的监听路径进行解析
        ChainFilePathParser parser = ChainFilePathParserHolder.getParser();
        List<String> pathList = parser.parse(config.getListenerFileList());

        // 获取文件最大的路径
        if (CollectionUtils.isNotEmpty(pathList)) {
            Set<String> parentPaths = pathList.stream()
                    .map(path -> path.replace(FileUtils.FILE_PREFIX, ""))
                    .map(path -> {
                        try {
                            File file = new File(path);
                            // 这个地方获取到的一定是绝对路径，所以获取父文件的路径进行监听
                            return file.getParent();
                        } catch (Exception e) {
                            // 这个地方正常情况不会出现异常，如果出现异常，则说明文件不存在
                            log.warn("file not found : {}", path);
                        }
                        return null;
                    }).filter(StringUtils::isNotBlank).collect(Collectors.toSet());

            // 创建文件监听器
            for (String parentPath : parentPaths) {
                // 增加对于文件后缀的过滤
                FileAlterationObserver observer = new FileAlterationObserver(new File(parentPath), new SuffixFileFilter(suffix));
                FileAlterationListenerAdaptor listener = new FileAlterationListenerAdaptor() {
                    @Override
                    public void onFileChange(File file) {
                        log.info("file change : {}", file.getAbsolutePath());
                        chainManager.refreshChain(type);
                    }

                    @Override
                    public void onFileDelete(File file) {
                        log.info("file delete : {}", file.getAbsolutePath());
                        // 解析文件内容
                        ValidationChainParser validationChainParser = chainManager.getChainParser(type);
                        if (Objects.nonNull(validationChainParser)) {
                            String fileInfo = FileUtils.getFileInfo(file);
                            if (StringUtils.isNotBlank(fileInfo)) {
                                List<ValidationChain> chainList = validationChainParser.parse(fileInfo);
                                chainManager.removeChain(type, chainList.stream().map(ValidationChain::getChainId).toArray(String[]::new));
                            }
                        }
                    }
                };
                observer.addListener(listener);
                try {
                    new FileAlterationMonitor(interval, observer).start();
                } catch (Exception e) {
                    throw new ValidationChainListenerException("local file listener start exception", e);
                }
            }
        }
    }

    /**
     * 获取配置
     *
     * @return 配置
     */
    protected abstract LocalChainDataConfig getConfig();
}
