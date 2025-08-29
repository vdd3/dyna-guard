package cn.easygd.dynaguard.core.annotation;

import cn.easygd.dynaguard.domain.enums.GuardMode;

import java.lang.annotation.*;

/**
 * 动态验证
 *
 * @author VD
 * @version v 0.1 2025/7/27 20:50
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

    /**
     * 是否开启熔断
     *
     * @return 是否开启熔断
     */
    boolean enableGuard() default false;

    /**
     * 熔断模式
     *
     * @return 熔断模式
     */
    GuardMode guardMode() default GuardMode.COUNTER;

    /**
     * 熔断阈值
     *
     * @return 熔断阈值
     */
    String guardThreshold() default "";
}
