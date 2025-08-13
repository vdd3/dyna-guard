package com.dg;

import com.dg.core.chain.ValidationChainManager;
import com.dg.core.guard.CounterGuard;
import com.dg.core.guard.CounterGuardManager;
import com.dg.core.holder.GlobalBeanContextHolder;
import com.dg.utils.GlobalBeanContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;
import java.util.Map;

/**
 * 验证流程初始化
 *
 * @author VD
 * @date 2025/8/12 20:05
 */
public class ValidationChainInit implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ValidationMethodInterceptor.class);

    /**
     * 验证链管理器
     */
    private ValidationChainManager validationChainManager;

    /**
     * 熔断器管理器
     */
    private CounterGuardManager counterGuardManager;

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        log.info("validation chain load chain start");

        // 将applicationContext放入全局bean容器中
        GlobalBeanContext beanContext = GlobalBeanContextHolder.getBeanContext();
        beanContext.registerBeanManager(applicationContext);

        // 加载流程
        validationChainManager.loadChain();

        // 将计数熔断器注册熔断器管理器中
        Map<String, CounterGuard> beans = applicationContext.getBeansOfType(CounterGuard.class);
        beans.forEach((k, v) -> {
            List<String> chainIdList = v.chainId();
            chainIdList.forEach(chainId -> counterGuardManager.register(chainId, v));
        });

        log.info("validation chain load chain end");
    }

    /**
     * 注入验证链管理器
     *
     * @param validationChainManager 验证链管理器
     */
    public void setValidationChainManager(ValidationChainManager validationChainManager) {
        this.validationChainManager = validationChainManager;
    }

    /**
     * 注入熔断器管理器
     *
     * @param counterGuardManager 熔断器管理器
     */
    public void setCounterGuardManager(CounterGuardManager counterGuardManager) {
        this.counterGuardManager = counterGuardManager;
    }
}
