package cn.easygd.dynaguard.core.annotation;

import java.lang.annotation.*;

/**
 * 动态验证
 *
 * @author VD
 * @date 2025/7/27 20:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicGuard {

    /**
     * 验证链分组
     *
     * @return 验证链分组
     */
    String group() default "";

    /**
     * 验证链ID
     *
     * @return 验证链ID
     */
    String chainId() default "";
}
