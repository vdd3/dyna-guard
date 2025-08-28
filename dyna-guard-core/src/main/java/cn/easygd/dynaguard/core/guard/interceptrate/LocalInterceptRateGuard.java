package cn.easygd.dynaguard.core.guard.interceptrate;

import cn.easygd.dynaguard.core.metrics.BizValidationStatistics;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.exception.GuardException;
import cn.easygd.dynaguard.domain.guard.GuardThreshold;
import cn.easygd.dynaguard.domain.guard.InterceptRateThreshold;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 本地拦截频率熔断
 *
 * @author VD
 * @version v 0.1 2025/8/28 20:06
 */
public class LocalInterceptRateGuard implements InterceptRateGuard {

    /**
     * 统计器
     */
    private BizValidationStatistics bizValidationStatistics;

    /**
     * 链路ID
     *
     * @return 链路ID
     */
    @Override
    public List<String> chainId() {
        return null;
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
        InterceptRateThreshold interceptRateThreshold = (InterceptRateThreshold) guardThreshold;
        // 获取对应的拦截率
        String nodeName = interceptRateThreshold.getNodeName();
        String condition = interceptRateThreshold.getCondition();
        BigDecimal threshold = interceptRateThreshold.getThreshold();

        // TODO 这个地方计算的有问题，如果第一次直接被拦截的话就会被判断判定为熔断

        BigDecimal interceptRate;
        if (StringUtils.isNotBlank(nodeName) && StringUtils.isNotBlank(condition)) {
            interceptRate = bizValidationStatistics.validationRate(chainId, nodeName, condition);
        } else if (StringUtils.isNotBlank(nodeName)) {
            interceptRate = bizValidationStatistics.nodeValidationRate(chainId, nodeName);
        } else {
            interceptRate = bizValidationStatistics.chainValidationRate(chainId);
        }

        return interceptRate.compareTo(threshold) >= 0;
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

    public void setBizValidationStatistics(BizValidationStatistics bizValidationStatistics) {
        this.bizValidationStatistics = bizValidationStatistics;
    }
}
