package cn.easygd.dynaguard.core.metrics;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * 本地业务验证统计
 *
 * @author VD
 * @version v 0.1 2025/8/24 0:00
 */
public class LocalBizValidationStatistics extends BaseBizValidationStatistics {

    /**
     * 实例
     */
    private static volatile LocalBizValidationStatistics instance;

    /**
     * 调用次数
     */
    private static final Map<String, Long> COUNT = Maps.newConcurrentMap();

    /**
     * 通过次数
     */
    private static final Map<String, Long> PASSED_COUNT = Maps.newConcurrentMap();

    /**
     * 熔断次数
     */
    private static final Map<String, Long> GUARD_COUNT = Maps.newConcurrentMap();

    /**
     * 验证次数
     */
    private static final Map<String, Map<String, Map<String, Long>>> VALIDATION_COUNT = Maps.newConcurrentMap();

    /**
     * 调用次数加1
     *
     * @param chainId 链ID
     */
    @Override
    public void incrementCount(String chainId) {
        COUNT.put(chainId, COUNT.getOrDefault(chainId, 0L) + 1);
    }

    /**
     * 调用成功次数加1
     *
     * @param chainId 链ID
     */
    @Override
    public void incrementPassedCount(String chainId) {
        PASSED_COUNT.put(chainId, PASSED_COUNT.getOrDefault(chainId, 0L) + 1);
    }

    /**
     * 熔断次数加1
     *
     * @param chainId 链ID
     */
    @Override
    public void incrementGuardCount(String chainId) {
        GUARD_COUNT.put(chainId, GUARD_COUNT.getOrDefault(chainId, 0L) + 1);
    }

    /**
     * 拦截次数加1
     *
     * @param chainId   链ID
     * @param nodeName  节点名称
     * @param condition 拦截条件
     */
    @Override
    public void incrementValidationCount(String chainId, String nodeName, String condition) {
        VALIDATION_COUNT.computeIfAbsent(chainId, k -> Maps.newConcurrentMap())
                .computeIfAbsent(nodeName, k -> Maps.newConcurrentMap())
                .compute(condition, (k, v) -> v == null ? 1L : v + 1);
    }

    /**
     * 获取调用次数
     *
     * @param chainId 链ID
     * @return 调用次数
     */
    @Override
    public Long count(String chainId) {
        return COUNT.getOrDefault(chainId, 0L);
    }

    /**
     * 获取通过次数
     *
     * @param chainId 链ID
     * @return 通过次数
     */
    @Override
    public Long passedCount(String chainId) {
        return PASSED_COUNT.getOrDefault(chainId, 0L);
    }

    /**
     * 获取熔断次数
     *
     * @param chainId 链ID
     * @return 拦截次数
     */
    @Override
    public Long guardCount(String chainId) {
        return GUARD_COUNT.getOrDefault(chainId, 0L);
    }

    /**
     * 获取拦截次数
     *
     * @param chainId   链ID
     * @param nodeName  节点名称
     * @param condition 拦截条件
     * @return 拦截次数
     */
    @Override
    public Long validationCount(String chainId, String nodeName, String condition) {
        return VALIDATION_COUNT.getOrDefault(chainId, Maps.newHashMap())
                .getOrDefault(nodeName, Maps.newHashMap())
                .getOrDefault(condition, 0L);
    }

    /**
     * 节点拦截次数
     *
     * @param chainId  链ID
     * @param nodeName 节点名称
     * @return key 拦截条件 value 拦截次数
     */
    @Override
    public Map<String, Long> nodeValidationCount(String chainId, String nodeName) {
        Map<String, Long> dataMap = VALIDATION_COUNT.getOrDefault(chainId, Maps.newHashMap())
                .getOrDefault(nodeName, Maps.newHashMap());
        Map<String, Long> resultMap = Maps.newHashMap();
        resultMap.putAll(dataMap);
        return resultMap;
    }

    /**
     * 链拦截率
     *
     * @param chainId 链ID
     * @return 拦截率
     */
    @Override
    public BigDecimal chainValidationRate(String chainId) {
        Map<String, Map<String, Long>> dataMap = VALIDATION_COUNT.getOrDefault(chainId, Maps.newHashMap());
        Long count = count(chainId);
        if (count > 0) {
            Long chainValidationCount = dataMap.values().stream()
                    .map(map -> Lists.newArrayList(map.values()))
                    .flatMap(List::stream)
                    .reduce(0L, Long::sum);
            return BigDecimal.valueOf(chainValidationCount).divide(BigDecimal.valueOf(count), 4, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 链拦截指标
     *
     * @param chainId 链ID
     * @return 拦截指标
     */
    @Override
    public List<ValidationMetrics> validationMetrics(String chainId) {
        // 调用总数
        Long allCount = count(chainId);
        Map<String, Map<String, Long>> chainMap = VALIDATION_COUNT.getOrDefault(chainId, Maps.newHashMap());
        List<ValidationMetrics> resultList = Lists.newArrayList();
        chainMap.forEach((nodeName, dataMap) -> {
            Long allConditionCount = dataMap.values().stream().reduce(Long::sum).orElse(0L);
            BigDecimal nodeRate;
            if (allCount > 0) {
                nodeRate = BigDecimal.valueOf(allConditionCount).divide(BigDecimal.valueOf(allCount), 4, BigDecimal.ROUND_HALF_UP);
            } else {
                return;
            }
            BigDecimal finalNodeRate = nodeRate;
            dataMap.forEach((condition, count) -> {
                ValidationMetrics metrics = new ValidationMetrics();
                metrics.setNodeName(nodeName);
                metrics.setCondition(condition);
                metrics.setCount(count);
                metrics.setNodeRate(finalNodeRate);
                metrics.setConditionRate(BigDecimal.valueOf(count).divide(BigDecimal.valueOf(allCount), 4, RoundingMode.HALF_UP));
                resultList.add(metrics);
            });
        });
        return resultList;
    }

    /**
     * 获取实例
     */
    public static LocalBizValidationStatistics getInstance() {
        if (instance == null) {
            synchronized (LocalBizValidationStatistics.class) {
                if (instance == null) {
                    instance = new LocalBizValidationStatistics();
                }
            }
        }
        return instance;
    }
}
