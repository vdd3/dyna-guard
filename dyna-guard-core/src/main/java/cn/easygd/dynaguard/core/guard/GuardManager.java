package cn.easygd.dynaguard.core.guard;

import cn.easygd.dynaguard.core.guard.counter.CounterGuard;
import cn.easygd.dynaguard.core.guard.interceptrate.InterceptRateGuard;
import cn.easygd.dynaguard.domain.enums.GuardMode;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 计数熔断器管理器
 *
 * @author VD
 *
 */
public class GuardManager {

    /**
     * 单例
     */
    private static volatile GuardManager instance;

    /**
     * 全局计数熔断器
     */
    private CounterGuard globalCounterGuard;

    /**
     * 全局拦截率熔断器
     */
    private InterceptRateGuard globalInterceptRateGuard;

    /**
     * 计数熔断器
     */
    private final Map<String, CounterGuard> COUNTER_GUARD_MAP = Maps.newConcurrentMap();

    /**
     * 拦截率熔断器
     */
    private final Map<String, InterceptRateGuard> INTERCEPT_RATE_GUARD_MAP = Maps.newConcurrentMap();

    /**
     * 获取熔断器
     *
     * @param chainId   链ID
     * @param guardMode 熔断器模式
     * @return 熔断器
     */
    public ValidationGuard getGuard(String chainId, GuardMode guardMode) {
        if (guardMode == GuardMode.COUNTER) {
            if (COUNTER_GUARD_MAP.containsKey(chainId)) {
                return COUNTER_GUARD_MAP.get(chainId);
            }
            return globalCounterGuard;
        } else if (guardMode == GuardMode.RATE) {
            if (INTERCEPT_RATE_GUARD_MAP.containsKey(chainId)) {
                return INTERCEPT_RATE_GUARD_MAP.get(chainId);
            }
            return globalInterceptRateGuard;
        }
        return COUNTER_GUARD_MAP.get(chainId);
    }

    /**
     * 注册熔断器
     *
     * @param guard 熔断器
     */
    public void register(ValidationGuard guard) {
        List<String> chainIdList = guard.chainId();
        if (guard instanceof CounterGuard) {
            if (CollectionUtils.isNotEmpty(chainIdList)) {
                chainIdList.forEach(chainId -> COUNTER_GUARD_MAP.put(chainId, (CounterGuard) guard));
            } else {
                globalCounterGuard = (CounterGuard) guard;
            }
        } else if (guard instanceof InterceptRateGuard) {
            if (CollectionUtils.isNotEmpty(chainIdList)) {
                chainIdList.forEach(chainId -> INTERCEPT_RATE_GUARD_MAP.put(chainId, (InterceptRateGuard) guard));
            } else {
                globalInterceptRateGuard = (InterceptRateGuard) guard;
            }
        }
    }

    /**
     * 移除熔断器
     *
     * @param chainId 链ID
     */
    public void remove(String chainId) {
        COUNTER_GUARD_MAP.remove(chainId);
        INTERCEPT_RATE_GUARD_MAP.remove(chainId);
    }

    /**
     * 清空熔断器
     */
    public void clear() {
        COUNTER_GUARD_MAP.clear();
        INTERCEPT_RATE_GUARD_MAP.clear();
    }

    private GuardManager() {
    }

    public static GuardManager getInstance() {
        if (instance == null) {
            synchronized (GuardManager.class) {
                if (instance == null) {
                    instance = new GuardManager();
                }
            }
        }
        return instance;
    }
}
