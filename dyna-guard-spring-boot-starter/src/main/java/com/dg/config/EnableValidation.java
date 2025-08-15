package com.dg.config;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启验证
 *
 * @author VD
 * @date 2025/8/15 19:17
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ValidationBeanSelector.class)
public @interface EnableValidation {

    /**
     * 验证模式
     *
     * @return 验证模式
     */
    AdviceMode mode() default AdviceMode.PROXY;
}
