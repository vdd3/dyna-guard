package com.dg;

import com.dg.core.chain.ValidationChainManager;
import com.dg.core.guard.CounterGuardManager;
import com.dg.domain.enums.FreeMarkTypeEnum;
import com.dg.utils.GlobalBeanContext;
import org.springframework.context.ApplicationContext;

/**
 * spring bean 容器
 *
 * @author VD
 * @date 2025/8/12 20:58
 */
public class SpringBeanContext implements GlobalBeanContext {

    /**
     * spring bean容器
     */
    private ApplicationContext applicationContext;

    /**
     * 获取bean
     *
     * @param beanName bean名称
     * @return bean
     */
    @Override
    public Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * 获取bean
     *
     * @param clazz 类
     * @return bean
     */
    @Override
    public Object getBean(Class<?> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 注册bean容器
     *
     * @param beanManager bean容器
     */
    @Override
    public void registerBeanManager(Object beanManager) {
        setApplicationContext((ApplicationContext) beanManager);
    }

    /**
     * 获取bean容器
     *
     * @return bean容器
     */
    @Override
    public Object getBeanManager() {
        return applicationContext;
    }

    /**
     * 获取ValidationChainManager
     *
     * @return ValidationChainManager
     */
    @Override
    public ValidationChainManager getChainManager() {
        return (ValidationChainManager) applicationContext.getBean("validationChainManager");
    }

    /**
     * 获取CounterGuardManager
     *
     * @return CounterGuardManager
     */
    @Override
    public CounterGuardManager getCounterGuardManager() {
        return (CounterGuardManager) applicationContext.getBean("counterGuardManager");
    }

    /**
     * 获取bean容器名称
     *
     * @return bean容器名称
     */
    @Override
    public String getBeanContextName() {
        return FreeMarkTypeEnum.SPRING.getType();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
