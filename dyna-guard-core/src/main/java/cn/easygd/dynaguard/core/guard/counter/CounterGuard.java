package cn.easygd.dynaguard.core.guard.counter;

import cn.easygd.dynaguard.core.guard.ValidationGuard;

/**
 * 计数熔断，本地实现的熔断器无法支持多机器部署，所以我们更加建议您自己建立自己的熔断逻辑以及计数逻辑
 *
 * @author VD
 * @version v 0.1 2025/8/12 22:07
 */
public interface CounterGuard extends ValidationGuard {

    /**
     * 计数
     *
     * @param chainId 链路ID
     * @return 当前计数
     */
    Long increment(String chainId);

    /**
     * 获取指定流程当前的计数
     *
     * @param chainId 链路ID
     * @return 当前计数（不存在则返回0）
     */
    Long getCount(String chainId);

    /**
     * 清空
     *
     * @param chainId 链路ID
     */
    void clear(String chainId);
}
