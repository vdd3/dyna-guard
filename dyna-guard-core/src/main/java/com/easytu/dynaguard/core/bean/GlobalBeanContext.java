package com.easytu.dynaguard.core.bean;

import com.easytu.dynaguard.core.chain.ValidationChainManager;
import com.easytu.dynaguard.core.guard.CounterGuardManager;

/**
 * 单例Bean容器
 *
 * @author VD
 * @date 2025/8/12 20:46
 */
public interface GlobalBeanContext {

    /**
     * 获取bean
     *
     * @param beanName bean名称
     * @return bean
     */
    Object getBean(String beanName);

    /**
     * 获取bean
     *
     * @param clazz 类
     * @return bean
     */
    Object getBean(Class<?> clazz);

    /**
     * 获取bean容器
     *
     * @return bean容器
     */
    Object getBeanManager();

    /**
     * 获取ValidationChainManager
     *
     * @return ValidationChainManager
     */
    default ValidationChainManager getChainManager() {
        return ValidationChainManager.getInstance();
    }

    /**
     * 获取CounterGuardManager
     *
     * @return CounterGuardManager
     */
    default CounterGuardManager getCounterGuardManager() {
        return CounterGuardManager.getInstance();
    }

    /**
     * 优先级
     */
    Integer priority();
}
