package cn.easygd.dynaguard.core.annotation;

import cn.easygd.dynaguard.domain.enums.ChainRuleMode;
import cn.easygd.dynaguard.domain.enums.GuardMode;
import cn.easygd.dynaguard.domain.guard.CounterThreshold;
import cn.easygd.dynaguard.domain.guard.InterceptRateThreshold;

import java.lang.annotation.*;

/**
 * 动态验证
 *
 * @author VD
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicGuard {

    /**
     * 验证链分组，如果不填默认为解析器第一个解析到的分组
     *
     * @return 验证链分组
     */
    String group() default "";

    /**
     * 验证链ID，如果不填默认为方法全路径
     *
     * @return 验证链ID
     */
    String chainId() default "";

    /**
     * 是否开启熔断，规则模式不会触发熔断
     *
     * @return 是否开启熔断
     */
    boolean enableGuard() default false;

    /**
     * 熔断模式，开启熔断后生效
     *
     * <ul>
     *     <li>COUNTER 为计数模式，需要使用计数熔断阈值</li>
     *     <li>RATE 为拦截率模式，需要使用拦截率阈值</li>
     * </ul>
     *
     * @return 熔断模式
     */
    GuardMode guardMode() default GuardMode.COUNTER;

    /**
     * 熔断阈值
     *
     * <p color = 'red'>需要于熔断模式保持一致</p>
     * @see CounterThreshold
     * @see InterceptRateThreshold
     * @return 熔断阈值
     */
    String guardThreshold() default "";

    /**
     * 链路规则模式
     *
     * <ul>
     *     <li>VALIDATION 为验证模式，代表这个业务需要被校验，但是脚本返回值必须为布尔类型</li>
     *     <li>RULE 为规则模式，代表需要获取业务规则，脚本返回值为Object即可</li>
     * </ul>
     * @return 链路规则模式
     */
    ChainRuleMode chainRuleMode() default ChainRuleMode.VALIDATION;
}
