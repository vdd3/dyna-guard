package cn.easygd.dynaguard.domain.guard;

import java.io.Serializable;

/**
 * 计数模式阈值
 *
 * @author VD
 */
public class CounterThreshold implements GuardThreshold, Serializable {

    /**
     * 阈值
     */
    private Long threshold = 100L;

    /**
     * 阈值周期(秒)
     */
    private Long period;

    /**
     * 是否失败
     */
    private Boolean fail = true;

    public CounterThreshold() {
    }

    public CounterThreshold(Long threshold) {
        this.threshold = threshold;
    }

    public CounterThreshold(Long threshold, Long period) {
        this.threshold = threshold;
        this.period = period;
    }

    public CounterThreshold(Long threshold, Long period, Boolean fail) {
        this.threshold = threshold;
        this.period = period;
        this.fail = fail;
    }

    public Long getThreshold() {
        return threshold;
    }

    public void setThreshold(Long threshold) {
        this.threshold = threshold;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public void setFail(Boolean fail) {
        this.fail = fail;
    }

    /**
     * 是否失败
     *
     * @return true:失败
     */
    @Override
    public Boolean isFail() {
        return fail;
    }
}
