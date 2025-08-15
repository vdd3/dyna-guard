package com.easytu.dynaguard;

import com.easytu.dynaguard.core.bean.GlobalBeanContext;
import com.easytu.dynaguard.core.chain.ValidationChainManager;
import com.easytu.dynaguard.core.guard.CounterGuardManager;
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
    private static ApplicationContext applicationContext;

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
     * 优先级
     */
    @Override
    public Integer priority() {
        return 5;
    }


    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }
}
