package cn.easygd.dynaguard.domain.guard;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 拦截率阈值
 *
 * @author VD
 */
public class InterceptRateThreshold implements GuardThreshold, Serializable {

    private static final long serialVersionUID = 807021346149769386L;

    /**
     * 拦截率阈值
     */
    private BigDecimal threshold = new BigDecimal("90");

    /**
     * 拦截开始次数
     */
    private Long startCount = 100L;

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

    public InterceptRateThreshold(BigDecimal threshold, String nodeName) {
        this.threshold = threshold;
        this.nodeName = nodeName;
    }

    public InterceptRateThreshold(BigDecimal threshold, String nodeName, String condition) {
        this.threshold = threshold;
        this.nodeName = nodeName;
        this.condition = condition;
    }

    public InterceptRateThreshold(BigDecimal threshold, Long startCount, String nodeName, String condition) {
        this.threshold = threshold;
        this.startCount = startCount;
        this.nodeName = nodeName;
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

    public Long getStartCount() {
        return startCount;
    }

    public void setStartCount(Long startCount) {
        this.startCount = startCount;
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
