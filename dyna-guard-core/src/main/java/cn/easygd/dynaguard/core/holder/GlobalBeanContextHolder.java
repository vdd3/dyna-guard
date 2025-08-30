package cn.easygd.dynaguard.core.holder;

import cn.easygd.dynaguard.core.bean.GlobalBeanContext;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.ServiceLoader;

/**
 * bean 容器全局容器
 *
 * @author VD
 *
 */
public class GlobalBeanContextHolder {

    /**
     * 获取bean容器
     */
    private static GlobalBeanContext beanContext;

    /**
     * 初始化
     */
    public static GlobalBeanContext getContext() {
        if (beanContext == null) {
            beanContext = Lists.newArrayList(ServiceLoader.load(GlobalBeanContext.class))
                    .stream()
                    .min(Comparator.comparing(GlobalBeanContext::priority))
                    .get();
        }
        return beanContext;
    }
}
