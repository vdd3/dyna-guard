package cn.easygd.dynaguard.config;

import cn.easygd.dynaguard.SpringBeanContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 全局bean注册
 *
 * @author VD
 * @version v 0.1 2025/8/15 20:27
 */
public class GlobalBeanRegister implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanContext.setApplicationContext(applicationContext);
    }
}
