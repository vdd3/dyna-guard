package com.dg.domain.config;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 本地流程数据配置
 *
 * @author VD
 * @date 2025/8/10 15:34
 */
public abstract class LocalChainDataConfig extends ChainDataConfig {

    /**
     * 监听文件
     */
    private List<String> listenerFileList;

    /**
     * 监听间隔
     */
    private Long listenerInterval = TimeUnit.MILLISECONDS.toMillis(5);

    /**
     * 字段转换器
     *
     * @return 字段转换器
     */
    public static Consumer<Map<String, Function<String, Object>>> fieldConsumer() {
        return map -> {
            map.put("enableListener", source -> {
                if (StringUtils.isNotBlank(source)) {
                    return Boolean.parseBoolean(source);
                }
                return true;
            });
            map.put("listenerFileList", source -> {
                if (StringUtils.isNotBlank(source)) {
                    return Lists.newArrayList(source.split(","));
                }
                return Lists.newArrayList();
            });
            map.put("listenerInterval", source -> {
                if (StringUtils.isNotBlank(source)) {
                    return TimeUnit.MILLISECONDS.toMillis(Long.parseLong(source));
                }
                return TimeUnit.MILLISECONDS.toMillis(5);
            });
        };
    }

    public List<String> getListenerFileList() {
        return listenerFileList;
    }

    public void setListenerFileList(List<String> listenerFileList) {
        this.listenerFileList = listenerFileList;
    }

    public Long getListenerInterval() {
        return listenerInterval;
    }

    public void setListenerInterval(Long listenerInterval) {
        this.listenerInterval = listenerInterval;
    }
}
