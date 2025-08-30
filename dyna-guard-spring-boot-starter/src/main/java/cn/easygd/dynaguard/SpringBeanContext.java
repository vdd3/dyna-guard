package cn.easygd.dynaguard;

import cn.easygd.dynaguard.core.bean.GlobalBeanContext;
import cn.easygd.dynaguard.core.chain.ValidationChainManager;
import cn.easygd.dynaguard.core.guard.GuardManager;
import cn.easygd.dynaguard.core.metrics.BizValidationStatistics;
import org.springframework.context.ApplicationContext;

/**
 * spring bean 容器
 *
 * @author VD
 *
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
    public GuardManager getGuardManager() {
        return (GuardManager) applicationContext.getBean("guardManager");
    }

    /**
     * 获取BizValidationStatistics
     *
     * @return BizValidationStatistics
     */
    @Override
    public BizValidationStatistics getBizValidationStatistics() {
        return applicationContext.getBean(BizValidationStatistics.class);
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
