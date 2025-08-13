package com.dg.core.holder;

import com.dg.core.chain.ValidationChainManager;
import com.dg.core.guard.CounterGuardManager;
import com.dg.utils.GlobalBeanContext;

/**
 * bean 容器全局容器
 *
 * @author VD
 * @date 2025/8/12 20:53
 */
public class GlobalBeanContextHolder {

    /**
     * 单例
     */
    private static volatile GlobalBeanContextHolder INSTANCE;

    /**
     * 获取bean容器
     */
    private GlobalBeanContext beanContext;

    /**
     * 初始化
     *
     * @param beanContext bean容器
     */
    public static void init(GlobalBeanContext beanContext) {
        if (INSTANCE == null) {
            synchronized (GlobalBeanContextHolder.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GlobalBeanContextHolder();
                }
            }
        }
        INSTANCE.beanContext = beanContext;
    }

    /**
     * 获取链路管理器
     *
     * @return 链路管理器
     */
    public static ValidationChainManager getChainManager() {
        return INSTANCE.beanContext.getChainManager();
    }

    /**
     * 获取计数器管理器
     *
     * @return 计数器管理器
     */
    public static CounterGuardManager getCounterGuardManager() {
        return INSTANCE.beanContext.getCounterGuardManager();
    }

    public static GlobalBeanContext getBeanContext() {
        return INSTANCE.beanContext;
    }

    private GlobalBeanContextHolder() {
    }
}
