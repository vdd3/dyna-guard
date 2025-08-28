package cn.easygd.dynaguard.core.guard.counter;

import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.exception.GuardException;
import cn.easygd.dynaguard.domain.guard.CounterThreshold;
import cn.easygd.dynaguard.domain.guard.GuardThreshold;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 本地计数器
 *
 * @author VD
 * @version v 0.1 2025/8/12 22:21
 */
public class LocalCounterGuard implements CounterGuard {

    /**
     * 缓存：key -> 原子计数器
     */
    private Cache<String, AtomicLong> counterCache;

    /**
     * 链路ID
     */
    private String chainId;

    private LocalCounterGuard() {
    }

    /**
     * 构造函数
     *
     * @param chainId 链路ID
     */
    public LocalCounterGuard(String chainId, Long guardExpire) {
        this.chainId = chainId;
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
        return Lists.newArrayList(chainId);
    }

    /**
     * 是否超过阈值
     *
     * @param chainId        流程id
     * @param guardThreshold 熔断阈值
     * @return true 超过阈值
     */
    @Override
    public Boolean isExceedThreshold(String chainId, GuardThreshold guardThreshold) {
        if (guardThreshold instanceof CounterThreshold) {
            CounterThreshold counterThreshold = (CounterThreshold) guardThreshold;
            return getCount(chainId) >= counterThreshold.getThreshold();
        }
        return true;
    }

    /**
     * 降级操作
     *
     * @param chainId 链路ID
     * @param context 上下文
     */
    @Override
    public void fallBack(String chainId, ValidationContext context) {
        throw new GuardException(String.format("chain : %s trigger fuse", chainId));
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
}
