package cn.easygd.dynaguard.core.metrics;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 业务验证统计接口
 *
 * @author VD
 * @version v 0.1 2025/8/23 22:05
 */
public interface BizValidationStatistics {

    /**
     * 调用次数加1
     *
     * @param chainId 链ID
     */
    void incrementCount(String chainId);

    /**
     * 调用成功次数加1
     *
     * @param chainId 链ID
     */
    void incrementPassedCount(String chainId);

    /**
     * 熔断次数加1
     *
     * @param chainId 链ID
     */
    void incrementGuardCount(String chainId);

    /**
     * 拦截次数加1
     *
     * @param chainId   链ID
     * @param nodeName  节点名称
     * @param condition 拦截条件
     */
    void incrementValidationCount(String chainId, String nodeName, String condition);

    /**
     * 获取调用次数
     *
     * @param chainId 链ID
     * @return 调用次数
     */
    Long count(String chainId);

    /**
     * 获取通过次数
     *
     * @param chainId 链ID
     * @return 通过次数
     */
    Long passedCount(String chainId);

    /**
     * 获取熔断次数
     *
     * @param chainId 链ID
     * @return 拦截次数
     */
    Long guardCount(String chainId);

    /**
     * 获取拦截次数
     *
     * @param chainId   链ID
     * @param nodeName  节点名称
     * @param condition 拦截条件
     * @return 拦截次数
     */
    Long validationCount(String chainId, String nodeName, String condition);

    /**
     * 节点拦截次数
     *
     * @param chainId  链ID
     * @param nodeName 节点名称
     * @return key 拦截条件 value 拦截次数
     */
    Map<String, Long> nodeValidationCount(String chainId, String nodeName);

    /**
     * 链拦截指标
     *
     * @param chainId 链ID
     * @return 拦截指标
     */
    List<ValidationMetrics> validationMetrics(String chainId);

    /**
     * 获取通过率
     *
     * @param chainId 链ID
     * @return 通过率
     */
    BigDecimal passedRate(String chainId);

    /**
     * 获取熔断率
     *
     * @param chainId 链ID
     * @return 熔断率
     */
    BigDecimal guardRate(String chainId);

    /**
     * 获取拦截率
     *
     * @param chainId   链ID
     * @param nodeName  节点名称
     * @param condition 拦截条件
     * @return 拦截率
     */
    BigDecimal validationRate(String chainId, String nodeName, String condition);

    /**
     * 节点拦截率
     *
     * @param chainId  链ID
     * @param nodeName 节点名称
     * @return 节点拦截率
     */
    Map<String, BigDecimal> nodeValidationRate(String chainId, String nodeName);
}
