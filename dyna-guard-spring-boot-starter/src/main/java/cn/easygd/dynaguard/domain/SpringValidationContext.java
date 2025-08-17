package cn.easygd.dynaguard.domain;

import cn.easygd.dynaguard.core.holder.GlobalBeanContextHolder;
import cn.easygd.dynaguard.domain.context.ValidationContext;

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
     * Bean上下文
     */
    public static final String BEAN_CONTEXT = "beanContext";

    /**
     * 构建执行上下文
     *
     * @return 上下文
     */
    @Override
    public Consumer<Map<String, Object>> buildExecuteContext() {
        return map -> {
            map.putAll(this.parameters);
            map.put(BEAN_CONTEXT, GlobalBeanContextHolder.getContext());
        };
    }
}
