package com.easytu.dynaguard.core.guard;

import com.easytu.dynaguard.domain.exception.GuardException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 本地计数器
 *
 * @author VD
 * @date 2025/8/12 22:21
 */
public class LocalCounterGuard implements CounterGuard {

    /**
     * 缓存：key -> 原子计数器
     */
    private Cache<String, AtomicLong> counterCache;


    private LocalCounterGuard() {
    }

    /**
     * 构造函数
     *
     * @param chainId 链路ID
     */
    public LocalCounterGuard(String chainId, Long guardExpire) {
        this.counterCache = CacheBuilder.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(guardExpire, TimeUnit.SECONDS)
                .build();
        this.counterCache.put(chainId, new AtomicLong(0L));
    }

    /**
     * 链路ID
     *
     * @return 链路ID
     */
    @Override
    public List<String> chainId() {
        // 本地熔断是个空实现，因为他不是单例，每个流程有自己的熔断器
        return null;
    }

    /**
     * 是否超出阈值
     *
     * @param chainId   链路ID
     * @param threshold 阈值
     * @return 是否超出阈值
     */
    @Override
    public Boolean isExceedThreshold(String chainId, Long threshold) {
        return getCount(chainId) >= threshold;
    }

    /**
     * 计数
     *
     * @param chainId 链路ID
     * @return 当前计数
     */
    @Override
    public Long increment(String chainId) {
        AtomicLong counter = counterCache.getIfPresent(chainId);
        if (counter == null) {
            counter = new AtomicLong(0);
            AtomicLong existing = counterCache.asMap().putIfAbsent(chainId, counter);
            if (existing != null) {
                counter = existing;
            }
        }
        return counter.incrementAndGet();
    }

    /**
     * 获取指定流程当前的计数
     *
     * @param chainId 链路ID
     * @return 当前计数（不存在则返回0）
     */
    @Override
    public Long getCount(String chainId) {
        AtomicLong counter = counterCache.getIfPresent(chainId);
        return counter != null ? counter.get() : 0;
    }

    /**
     * 清空
     *
     * @param chainId 链路ID
     */
    @Override
    public void clear(String chainId) {
        counterCache.invalidate(chainId);
    }

    /**
     * 降级操作
     *
     * @param chainId 链路ID
     */
    @Override
    public void rollback(String chainId) {
        throw new GuardException("rollback not support");
    }
}
