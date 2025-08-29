package cn.easygd.dynaguard.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

/**
 * @author VD
 * @version v 0.1 2025/8/28 23:18
 */
public class ValidationMethodCondition implements Condition {
    /**
     * Determine if the condition matches.
     *
     * @param context  the condition context
     * @param metadata the metadata of the {@link AnnotationMetadata class}
     *                 or {@link MethodMetadata method} being checked
     * @return {@code true} if the condition matches and the component can be registered,
     * or {@code false} to veto the annotated component's registration
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        // 根据实际配置属性名调整，例如假设配置前缀为 dyna.guard.validation
        String[] beanNames = environment.getProperty("validation.validationMethod", String[].class, new String[0]);
        return beanNames.length > 0;
    }
}
