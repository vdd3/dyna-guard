package cn.easygd.dynaguard.core.bean;

/**
 * 默认全局 Bean 上下文
 *
 * @author VD
 * @version v 0.1 2025/8/15 20:43
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
     * 获取bean容器
     *
     * @return bean容器
     */
    @Override
    public Object getBeanManager() {
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
