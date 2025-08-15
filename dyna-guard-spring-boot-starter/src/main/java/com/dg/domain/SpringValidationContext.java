package com.dg.domain;

import com.dg.core.holder.GlobalBeanContextHolder;
import com.dg.domain.context.ValidationContext;

import java.util.Map;
import java.util.function.Consumer;

/**
 * spring验证流程上下文
 *
 * @author VD
 * @date 2025/8/15 20:48
 */
public class SpringValidationContext extends ValidationContext {

    /**
     * 构建执行上下文
     *
     * @return 上下文
     */
    @Override
    public Consumer<Map<String, Object>> buildExecuteContext() {
        return map -> {
            map.putAll(this.parameters);
            map.put("beanContext", GlobalBeanContextHolder.getContext());
        };
    }
}
