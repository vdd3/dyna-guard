package com.dg.core.guard;

import java.util.List;

/**
 * 计数熔断，本地实现的熔断器无法支持多机器部署，所以我们更加建议您自己建立自己的熔断逻辑以及计数逻辑
 *
 * @author VD
 * @date 2025/8/12 22:07
 */
public interface CounterGuard {

    /**
     * 链路ID
     *
     * @return 链路ID
     */
    List<String> chainId();

    /**
     * 是否超出阈值
     *
     * @param chainId   链路ID
     * @param threshold 阈值
     * @return 是否超出阈值
     */
    Boolean isExceedThreshold(String chainId, Long threshold);

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

    /**
     * 降级操作
     *
     * @param chainId 链路ID
     */
    void rollback(String chainId);
}
