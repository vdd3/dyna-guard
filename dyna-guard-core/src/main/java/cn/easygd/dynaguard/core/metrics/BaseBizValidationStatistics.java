package cn.easygd.dynaguard.core.metrics;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

/**
 * 基础业务验证统计
 *
 * @author VD
 * @version 2025/8/23 23:50 1.0
 */
public abstract class BaseBizValidationStatistics implements BizValidationStatistics {

    /**
     * 获取通过率
     *
     * @param chainId 链ID
     * @return 通过率
     */
    @Override
    public BigDecimal passedRate(String chainId) {
        Long passedCount = getDefault(passedCount(chainId));
        Long count = getDefault(count(chainId));
        if (count > 0) {
            return BigDecimal.valueOf(passedCount).divide(BigDecimal.valueOf(count), 4, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ONE;
    }

    /**
     * 获取熔断率
     *
     * @param chainId 链ID
     * @return 熔断率
     */
    @Override
    public BigDecimal guardRate(String chainId) {
        Long count = getDefault(count(chainId));
        Long guardCount = getDefault(guardCount(chainId));
        if (count > 0) {
            return BigDecimal.valueOf(guardCount).divide(BigDecimal.valueOf(count), 4, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取拦截率
     *
     * @param chainId   链ID
     * @param nodeName  节点名称
     * @param condition 拦截条件
     * @return 拦截率
     */
    @Override
    public BigDecimal validationRate(String chainId, String nodeName, String condition) {
        Long count = getDefault(count(chainId));
        Long validationCount = getDefault(validationCount(chainId, nodeName, condition));
        if (count > 0) {
            return BigDecimal.valueOf(validationCount).divide(BigDecimal.valueOf(count), 4, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 节点拦截率
     *
     * @param chainId  链ID
     * @param nodeName 节点名称
     * @return 节点拦截率
     */
    @Override
    public Map<String, BigDecimal> nodeValidationRate(String chainId, String nodeName) {
        Map<String, Long> nodeMap = nodeValidationCount(chainId, nodeName);
        Map<String, BigDecimal> result = Maps.newHashMap();
        nodeMap.forEach((k, v) -> {
            Long count = getDefault(count(chainId));
            BigDecimal conditionRate = BigDecimal.ZERO;
            if (count > 0) {
                conditionRate = BigDecimal.valueOf(v).divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP);
            }
            result.put(k, conditionRate);
        });
        return result;
    }

    /**
     * 获取默认值
     *
     * @param value 值
     * @return 默认值
     */
    protected Long getDefault(Long value) {
        return Optional.ofNullable(value).orElse(0L);
    }
}
