package cn.easygd.dynaguard.domain.guard;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 拦截率阈值
 *
 * @author VD
 * @version v 0.1 2025/8/27 22:54
 */
public class InterceptRateThreshold implements GuardThreshold, Serializable {

    /**
     * 拦截率阈值
     */
    private BigDecimal threshold = new BigDecimal("90");

    /**
     * 节点名称
     */
    private String nodeName = null;

    /**
     * 拦截条件
     */
    private String condition = null;

    /**
     * 是否失败
     */
    private Boolean fail = true;

    public InterceptRateThreshold() {
    }

    public InterceptRateThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public InterceptRateThreshold(BigDecimal threshold, String condition) {
        this.threshold = threshold;
        this.condition = condition;
    }

    public InterceptRateThreshold(BigDecimal threshold, String condition, Boolean fail) {
        this.threshold = threshold;
        this.condition = condition;
        this.fail = fail;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setFail(Boolean fail) {
        this.fail = fail;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * 是否失败
     *
     * @return true:失败
     */
    @Override
    public Boolean isFail() {
        return null;
    }
}
