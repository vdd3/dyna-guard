package cn.easygd.dynaguard.core.metrics;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 校验指标
 *
 * @author VD
 */
public class ValidationMetrics implements Serializable {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = -9025568983899446862L;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 拦截条件
     */
    private String condition;

    /**
     * 拦截次数
     */
    private Long count;

    /**
     * 节点拦截率
     */
    private BigDecimal nodeRate;

    /**
     * 拦截条件拦截率
     */
    private BigDecimal conditionRate;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getNodeRate() {
        return nodeRate;
    }

    public void setNodeRate(BigDecimal nodeRate) {
        this.nodeRate = nodeRate;
    }

    public BigDecimal getConditionRate() {
        return conditionRate;
    }

    public void setConditionRate(BigDecimal conditionRate) {
        this.conditionRate = conditionRate;
    }
}
