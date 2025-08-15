package com.easytu.dynaguard.config;

import com.easytu.dynaguard.ValidationChainInit;
import com.easytu.dynaguard.ValidationMethodInterceptor;
import com.easytu.dynaguard.core.chain.ValidationChainManager;
import com.easytu.dynaguard.core.guard.CounterGuardManager;
import com.easytu.dynaguard.domain.config.ValidationChainConfig;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证流程引擎自动配置
 *
 * @author VD
 * @date 2025/8/3 11:59
 */
@Configuration
@AutoConfigureAfter({ValidationChainPropertyAutoConfiguration.class})
@ConditionalOnBean(ValidationChainConfig.class)
public class ValidationChainAutoConfig {

    /**
     * 创建验证流程管理器
     *
     * @param validationChainConfig 验证配置
     * @return ValidationChainManager
     */
    @Bean
    public ValidationChainManager validationChainManager(ValidationChainConfig validationChainConfig) {
        ValidationChainManager manager = ValidationChainManager.getInstance();
        manager.setConfig(validationChainConfig);
        return manager;
    }

    /**
     * 创建计数器管理器
     *
     * @return CounterGuardManager
     */
    @Bean
    public CounterGuardManager counterGuardManager() {
        return CounterGuardManager.getInstance();
    }

    /**
     * 创建全局bean注册器
     *
     * @return GlobalBeanRegister
     */
    @Bean
    public GlobalBeanRegister globalBeanRegister() {
        return new GlobalBeanRegister();
    }

    /**
     * 创建代理
     *
     * @param validationChainConfig 验证配置
     * @return BeanNameAutoProxyCreator
     */
    @Bean
    public BeanNameAutoProxyCreator proxyCreator(ValidationChainConfig validationChainConfig) {
        BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
        creator.setBeanNames(validationChainConfig.getValidationMethodList().toArray(new String[0]));
        creator.setInterceptorNames("validationMethodInterceptor");
        return creator;
    }

    /**
     * 验证方法拦截器
     *
     * @return validationMethodInterceptor
     */
    @Bean("validationMethodInterceptor")
    public ValidationMethodInterceptor validationMethodInterceptor(ValidationChainManager validationChainManager) {
        ValidationMethodInterceptor validationMethodInterceptor = new ValidationMethodInterceptor();
        validationMethodInterceptor.setValidationChainManager(validationChainManager);
        return validationMethodInterceptor;
    }

    /**
     * 初始化验证流程
     *
     * @param validationChainManager 验证流程管理器
     * @param counterGuardManager    熔断管理器
     * @param globalBeanRegister     全局bean注册器
     * @return ValidationChainInit
     */
    @Bean
    public ValidationChainInit validationChainInit(ValidationChainManager validationChainManager,
                                                   CounterGuardManager counterGuardManager,
                                                   GlobalBeanRegister globalBeanRegister) {
        ValidationChainInit init = new ValidationChainInit();
        init.setValidationChainManager(validationChainManager);
        init.setCounterGuardManager(counterGuardManager);
        return init;
    }
}
