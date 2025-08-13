package com.dg.domain.config;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 流程数据配置
 *
 * @author VD
 * @date 2025/8/6 21:17
 */
public abstract class ChainDataConfig {

    /**
     * 是否启用监听
     */
    private Boolean enableListener = true;

    /**
     * 字段转换器
     *
     * @return 字段转换器
     */
    public static Consumer<Map<String, Function<String, Object>>> fieldConsumer() {
        return map -> map.put("enableListener", source -> {
            if (StringUtils.isNotBlank(source)) {
                return Boolean.parseBoolean(source);
            }
            return true;
        });
    }

    public Boolean getEnableListener() {
        return enableListener;
    }

    public void setEnableListener(Boolean enableListener) {
        this.enableListener = enableListener;
    }
}
