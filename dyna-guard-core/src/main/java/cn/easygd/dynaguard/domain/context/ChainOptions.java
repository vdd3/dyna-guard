package cn.easygd.dynaguard.domain.context;

import cn.easygd.dynaguard.domain.enums.ChainRuleMode;
import cn.easygd.dynaguard.domain.enums.GuardMode;
import cn.easygd.dynaguard.domain.guard.CounterThreshold;
import cn.easygd.dynaguard.domain.guard.GuardThreshold;
import cn.easygd.dynaguard.domain.guard.InterceptRateThreshold;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 流程选择
 *
 * @author VD
 */
public class ChainOptions {

    /**
     * 默认配置
     */
    public static final ChainOptions DEFAULT = new Builder().build();

    /**
     * 是否开启熔断
     */
    private final Boolean enableGuard;

    /**
     * 熔断模式
     */
    private final GuardMode guardMode;

    /**
     * 链路规则模式
     */
    private final ChainRuleMode chainRuleMode;

    /**
     * 熔断阈值
     */
    private final GuardThreshold guardThreshold;

    public Boolean getEnableGuard() {
        return enableGuard;
    }

    public GuardMode getGuardMode() {
        return guardMode;
    }

    public GuardThreshold getGuardThreshold() {
        return guardThreshold;
    }

    public ChainRuleMode getChainRuleMode() {
        return chainRuleMode;
    }

    public static ChainOptions.Builder builder() {
        return new ChainOptions.Builder();
    }

    public ChainOptions(Boolean enableGuard, GuardMode guardMode, GuardThreshold guardThreshold, ChainRuleMode chainRuleMode) {
        this.enableGuard = enableGuard;
        this.guardMode = guardMode;
        this.guardThreshold = guardThreshold;
        this.chainRuleMode = chainRuleMode;
    }

    /**
     * Builder
     */
    public static class Builder {
        private Boolean enableGuard = false;
        private GuardMode guardMode = GuardMode.COUNTER;
        private ChainRuleMode chainRuleMode = ChainRuleMode.VALIDATION;
        private GuardThreshold guardThreshold = new CounterThreshold();

        public Builder enableGuard(Boolean enableGuard) {
            this.enableGuard = enableGuard;
            return this;
        }

        public Builder chainRuleMode(ChainRuleMode chainRuleMode) {
            this.chainRuleMode = chainRuleMode;
            return this;
        }

        public Builder guardMode(GuardMode guardMode) {
            this.guardMode = guardMode;
            return this;
        }

        public Builder guardThreshold(GuardThreshold guardThreshold) {
            this.guardThreshold = guardThreshold;
            return this;
        }

        public Builder guardThreshold(Long threshold) {
            this.guardThreshold = new CounterThreshold(threshold);
            return this;
        }

        public Builder guardThreshold(Long threshold, Long period) {
            this.guardThreshold = new CounterThreshold(threshold, period);
            return this;
        }

        public Builder guardThreshold(BigDecimal threshold, String nodeName) {
            this.guardThreshold = new InterceptRateThreshold(threshold, nodeName);
            return this;
        }

        public Builder guardThreshold(BigDecimal threshold, String nodeName, String condition) {
            this.guardThreshold = new InterceptRateThreshold(threshold, nodeName, condition);
            return this;
        }

        public Builder guardThreshold(BigDecimal threshold) {
            this.guardThreshold = new InterceptRateThreshold(threshold);
            return this;
        }

        public ChainOptions build() {
            if (enableGuard) {
                switch (guardMode) {
                    case COUNTER:
                        // 代表在一个时间区间内，超过阈值则熔断
                        guardThreshold = Optional.ofNullable(guardThreshold).orElse(new CounterThreshold());
                        break;
                    case RATE:
                        // 代表在超过当前流程的拦截率阈值则熔断
                        guardThreshold = Optional.ofNullable(guardThreshold).orElse(new InterceptRateThreshold());
                        break;
                    default:
                        break;
                }
            }
            return new ChainOptions(enableGuard, guardMode, guardThreshold, chainRuleMode);
        }
    }
}
