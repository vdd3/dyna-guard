package cn.easygd.dynaguard.core.bean;

import cn.easygd.dynaguard.core.chain.ValidationChainManager;
import cn.easygd.dynaguard.core.guard.GuardManager;
import cn.easygd.dynaguard.core.metrics.BizValidationStatistics;
import cn.easygd.dynaguard.core.metrics.LocalBizValidationStatistics;

/**
 * 单例Bean容器
 *
 * @author VD
 * @version v 0.1 2025/8/12 20:46
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
    default GuardManager getGuardManager() {
        return GuardManager.getInstance();
    }

    /**
     * 获取BizValidationStatistics
     *
     * @return BizValidationStatistics
     */
    default BizValidationStatistics getBizValidationStatistics() {
        return LocalBizValidationStatistics.getInstance();
    }

    /**
     * 优先级
     */
    Integer priority();
}
