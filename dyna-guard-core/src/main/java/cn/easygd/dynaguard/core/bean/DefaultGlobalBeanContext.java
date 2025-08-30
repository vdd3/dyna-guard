package cn.easygd.dynaguard.core.bean;

/**
 * 默认全局 Bean 上下文
 *
 * @author VD
 */
public class DefaultGlobalBeanContext implements GlobalBeanContext {

    /**
     * 获取bean
     *
     * @param beanName bean名称
     * @return bean
     */
    @Override
    public Object getBean(String beanName) {
        return null;
    }

    /**
     * 获取bean
     *
     * @param clazz 类
     * @return bean
     */
    @Override
    public Object getBean(Class<?> clazz) {
        return null;
    }

    /**
     * 优先级
     */
    @Override
    public Integer priority() {
        return 10;
    }
}
