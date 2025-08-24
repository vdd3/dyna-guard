package cn.easygd.dynaguard.config;

import cn.easygd.dynaguard.ValidationChainInit;
import cn.easygd.dynaguard.ValidationMethodInterceptor;
import cn.easygd.dynaguard.core.chain.ValidationChainManager;
import cn.easygd.dynaguard.core.guard.CounterGuardManager;
import cn.easygd.dynaguard.core.metrics.BizValidationStatistics;
import cn.easygd.dynaguard.core.metrics.LocalBizValidationStatistics;
import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证流程引擎自动配置
 *
 * @author VD
 * @version v 0.1 2025/8/3 11:59
 */
@Configuration
@AutoConfigureAfter({ValidationChainPropertyAutoConfiguration.class})
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
     * 创建全局默认的统计器
     *
     * @return BizValidationStatistics
     */
    @Bean("localBizValidationStatistics")
    public BizValidationStatistics bizValidationStatistics() {
        return LocalBizValidationStatistics.getInstance();
    }

    /**
     * 创建代理
     *
     * @param validationChainConfig 验证配置
     * @return BeanNameAutoProxyCreator
     */
    @Bean
    public BeanNameAutoProxyCreator proxyCreator(ValidationChainConfig validationChainConfig,
                                                 ValidationMethodInterceptor validationMethodInterceptor) {
        BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
        creator.setBeanNames(validationChainConfig.getValidationMethodList().toArray(new String[0]));
        creator.setInterceptorNames("validationMethodInterceptor");
        // 必须使用cglb代理
        creator.setProxyTargetClass(true);
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
