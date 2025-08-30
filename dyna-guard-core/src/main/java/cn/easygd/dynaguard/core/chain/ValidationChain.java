package cn.easygd.dynaguard.core.chain;

import cn.easygd.dynaguard.core.bean.GlobalBeanContext;
import cn.easygd.dynaguard.core.engine.Validator;
import cn.easygd.dynaguard.core.guard.GuardManager;
import cn.easygd.dynaguard.core.guard.ValidationGuard;
import cn.easygd.dynaguard.core.guard.counter.CounterGuard;
import cn.easygd.dynaguard.core.guard.counter.LocalCounterGuard;
import cn.easygd.dynaguard.core.guard.interceptrate.LocalInterceptRateGuard;
import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.core.holder.GlobalBeanContextHolder;
import cn.easygd.dynaguard.core.metrics.BizValidationStatistics;
import cn.easygd.dynaguard.core.trace.BizTracker;
import cn.easygd.dynaguard.core.trace.ReturnInfo;
import cn.easygd.dynaguard.domain.ValidationNode;
import cn.easygd.dynaguard.domain.ValidationResult;
import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import cn.easygd.dynaguard.domain.context.ChainOptions;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.GuardMode;
import cn.easygd.dynaguard.domain.enums.ValidationErrorEnum;
import cn.easygd.dynaguard.domain.exception.ValidationFailedException;
import cn.easygd.dynaguard.domain.guard.CounterThreshold;
import cn.easygd.dynaguard.domain.guard.GuardThreshold;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * 验证链
 *
 * @author VD
 */
public class ValidationChain {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ValidationChain.class);

    /**
     * 验证链组
     */
    private String group;

    /**
     * 验证链ID
     */
    private String chainId;

    /**
     * 节点列表
     */
    private List<ValidationNode> nodes;

    /**
     * 执行验证链
     *
     * @param context 上下文
     */
    public void execute(ValidationContext context) {
        ValidationResult result = executeResult(context);
        if (!result.getSuccess()) {
            throw new ValidationFailedException(ValidationErrorEnum.FAIL.getErrorCode(), result.getMessage());
        }
    }

    /**
     * 执行验证链
     *
     * @param context 上下文
     * @return 验证结果
     */
    public ValidationResult executeResult(ValidationContext context) {
        return executeTemplate(context);
    }

    /**
     * 执行模板
     *
     * @param context 上下文
     * @return 验证结果
     */
    private ValidationResult executeTemplate(ValidationContext context) {
        ChainOptions chainOptions = context.getChainOptions();
        Boolean enableGuard = chainOptions.getEnableGuard();
        GuardThreshold guardThreshold = chainOptions.getGuardThreshold();
        GuardMode guardMode = chainOptions.getGuardMode();

        // 获取配置
        ValidationChainConfig config = ChainConfigHolder.getConfig();
        Boolean enableBizTrace = config.getEnableBizTrace();

        GlobalBeanContext globalBeanContext = GlobalBeanContextHolder.getContext();

        // 业务统计器
        BizValidationStatistics statistics = globalBeanContext.getBizValidationStatistics();

        ValidationGuard guard = getGuard(globalBeanContext, guardMode, guardThreshold);
        if (enableGuard) {
            // 如果这个地方熔断器还是空，那么一定有问题
            if (Objects.isNull(guard)) {
                throw new ValidationFailedException(ValidationErrorEnum.GUARD_MISS);
            }

            // 如果触发熔断直接返回失败结果
            if (guard.isExceedThreshold(this.chainId, guardThreshold)) {
                log.info("guard exceed threshold : [{}]", this.chainId);
                if (enableBizTrace) {
                    statistics.incrementCount(this.chainId);
                    statistics.incrementGuardCount(this.chainId);
                }

                // 降级
                guard.fallBack(this.chainId, context);

                if (guardThreshold.isFail()) {
                    return ValidationResult.fail(String.format("%s guard exceed threshold", this.chainId));
                }
            }
        }

        // 执行流程
        ValidationResult result;

        if (enableBizTrace) {
            try {
                // 初始化跟踪信息
                BizTracker.init();

                // 执行
                result = process(context);

                if (!result.getSuccess()) {
                    // 获取跟踪信息
                    ReturnInfo returnInfo = BizTracker.get();
                    // 增加未通过次数
                    String condition = StringUtils.defaultIfBlank(returnInfo.getTriggerCondition(), "unknown");
                    statistics.incrementValidationCount(this.chainId, result.getNodeName(), condition);
                    result.setReturnInfo(returnInfo);
                } else {
                    // 增加通过次数
                    statistics.incrementPassedCount(this.chainId);
                }
            } finally {
                // 增加调用次数
                statistics.incrementCount(this.chainId);
                // 清理跟踪信息
                BizTracker.clear();
            }
        } else {
            result = process(context);
        }


        if (!result.getSuccess()) {
            if (enableGuard && GuardMode.COUNTER == guardMode) {
                ((CounterGuard) guard).increment(this.chainId);
            }
        }

        return result;
    }

    /**
     * 执行模板
     *
     * @param context 上下文
     * @return 执行结果
     */
    private ValidationResult process(ValidationContext context) {
        log.info("validation chain start : [{}=={}] , context : [{}]", group, chainId, context);

        for (ValidationNode node : this.nodes) {
            String script = node.getScript();
            // 执行验证
            Validator validator = node.getValidator();
            if (Objects.isNull(validator)) {
                log.warn("validator is null , language : {}", node.getLanguage());
                continue;
            }

            ValidationResult result = validator.execute(script, context);

            if (!result.getSuccess()) {
                if (node.getFastFail()) {
                    // 需要判断是否是因为执行异常导致的验证失败
                    if (result.getException()) {
                        throw new ValidationFailedException(ValidationErrorEnum.SCRIPT_EXECUTE_ERROR, result.getThrowable());
                    } else {
                        String nodeName = StringUtils.defaultIfBlank(node.getNodeName(), node.getLanguage() + "@@" + node.getOrder());
                        return ValidationResult.fail(node.getMessage(), nodeName);
                    }
                } else {
                    // 否则打印日志
                    log.info("validation fail but skip");
                }
            }
        }

        return ValidationResult.success();
    }

    /**
     * 获取熔断器
     *
     * @param globalBeanContext 全局bean容器
     * @param guardMode         熔断模式
     * @param guardThreshold    熔断阈值
     * @return 熔断器
     */
    private ValidationGuard getGuard(GlobalBeanContext globalBeanContext, GuardMode guardMode, GuardThreshold guardThreshold) {
        GuardManager guardManager = globalBeanContext.getGuardManager();
        ValidationGuard guard = guardManager.getGuard(this.chainId, guardMode);
        if (Objects.isNull(guard)) {
            if (guardMode == GuardMode.COUNTER) {
                CounterThreshold counterThreshold = (CounterThreshold) guardThreshold;
                log.info("counter guard is null , register local guard : [{}]", this.chainId);
                guard = new LocalCounterGuard(this.chainId, counterThreshold.getPeriod());
                guardManager.register(guard);
            } else if (guardMode == GuardMode.RATE) {
                // 这个地方会尝试从spring的容器中获取
                log.info("rate guard is null , use local guard : [{}]", this.chainId);
                guard = (LocalInterceptRateGuard) globalBeanContext.getBean("localInterceptRateGuard");
            }
        }
        return guard;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public List<ValidationNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ValidationNode> nodes) {
        this.nodes = nodes;
    }
}
