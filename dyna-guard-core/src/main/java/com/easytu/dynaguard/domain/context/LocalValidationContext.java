package com.easytu.dynaguard.domain.context;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 本地验证上下文
 *
 * @author VD
 * @date 2025/8/15 20:46
 */
public class LocalValidationContext extends ValidationContext {

    /**
     * 构建执行上下文
     *
     * @return 上下文
     */
    @Override
    public Consumer<Map<String, Object>> buildExecuteContext() {
        return map -> map.putAll(this.getParameters());
    }
}
