package cn.easygd.dynaguard.core.guard;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 计数熔断器管理器
 *
 * @author VD
 * @date 2025/8/12 22:16
 */
public class CounterGuardManager {

    /**
     * 单例
     */
    private static volatile CounterGuardManager instance;

    /**
     * 熔断器
     */
    private static final Map<String, CounterGuard> GUARD_MAP = Maps.newConcurrentMap();

    /**
     * 获取熔断器
     *
     * @param chainId 链ID
     * @return 熔断器
     */
    public CounterGuard getGuard(String chainId) {
        return GUARD_MAP.get(chainId);
    }

    /**
     * 注册熔断器
     *
     * @param chainId 链ID
     * @param guard   熔断器
     */
    public void register(String chainId, CounterGuard guard) {
        GUARD_MAP.put(chainId, guard);
    }

    /**
     * 移除熔断器
     *
     * @param chainId 链ID
     */
    public void remove(String chainId) {
        GUARD_MAP.remove(chainId);
    }

    /**
     * 清空熔断器
     */
    public void clear() {
        GUARD_MAP.clear();
    }

    private CounterGuardManager() {
    }

    public static CounterGuardManager getInstance() {
        if (instance == null) {
            synchronized (CounterGuardManager.class) {
                if (instance == null) {
                    instance = new CounterGuardManager();
                }
            }
        }
        return instance;
    }
}
