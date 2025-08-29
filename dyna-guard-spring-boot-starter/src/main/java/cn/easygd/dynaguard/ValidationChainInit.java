package cn.easygd.dynaguard;

import cn.easygd.dynaguard.core.chain.ValidationChainManager;
import cn.easygd.dynaguard.core.guard.GuardManager;
import cn.easygd.dynaguard.core.guard.counter.CounterGuard;
import cn.easygd.dynaguard.core.guard.interceptrate.InterceptRateGuard;
import cn.easygd.dynaguard.core.guard.interceptrate.LocalInterceptRateGuard;
import cn.easygd.dynaguard.core.metrics.BizValidationStatistics;
import cn.easygd.dynaguard.core.metrics.LocalBizValidationStatistics;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;

/**
 * 验证流程初始化
 *
 * @author VD
 * @version v 0.1 2025/8/12 20:05
 */
public class ValidationChainInit implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ValidationChainInit.class);

    /**
     * 默认统计实现名称
     */
    private static final String DEFAULT_STATISTICS_BEAN_NAME = "localBizValidationStatistics";

    /**
     * 默认拦截率熔断器名称
     */
    private static final String DEFAULT_INTERCEPT_BEAN_NAME = "localInterceptRateGuard";

    /**
     * 验证链管理器
     */
    private ValidationChainManager validationChainManager;

    /**
     * 熔断器管理器
     */
    private GuardManager guardManager;

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        log.info("validation chain load chain start");

        // 初始化
        validationChainManager.init();

        // 加载流程
        validationChainManager.loadChain();

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        // 如果不存在统计实现，则注册默认的
        Map<String, BizValidationStatistics> statisticsBeans = applicationContext.getBeansOfType(BizValidationStatistics.class);
        if (MapUtils.isEmpty(statisticsBeans)) {
            beanFactory.registerSingleton(DEFAULT_STATISTICS_BEAN_NAME, LocalBizValidationStatistics.getInstance());
        }

        // 注册计数熔断器
        Map<String, CounterGuard> counterGuardBeans = applicationContext.getBeansOfType(CounterGuard.class);
        counterGuardBeans.forEach((k, v) -> guardManager.register(v));
        // 注册拦截率熔断器
        Map<String, InterceptRateGuard> interceptRateGuardBeans = applicationContext.getBeansOfType(InterceptRateGuard.class);
        interceptRateGuardBeans.forEach((k, v) -> guardManager.register(v));
        // 判断是否存在全局的拦截率熔断器，如果不存在则注册默认的
        boolean isContainsGlobalInterceptRateGuard = interceptRateGuardBeans.values().stream().anyMatch(v -> CollectionUtils.isEmpty(v.chainId()));
        if (!isContainsGlobalInterceptRateGuard) {
            LocalInterceptRateGuard localInterceptRateGuard = new LocalInterceptRateGuard();
            localInterceptRateGuard.setBizValidationStatistics(applicationContext.getBean(BizValidationStatistics.class));
            beanFactory.registerSingleton(DEFAULT_INTERCEPT_BEAN_NAME, localInterceptRateGuard);
        }

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
     * @param guardManager 熔断器管理器
     */
    public void setCounterGuardManager(GuardManager guardManager) {
        this.guardManager = guardManager;
    }
}
