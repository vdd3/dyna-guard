package cn.easygd.dynaguard.core.chain;

import cn.easygd.dynaguard.core.bean.GlobalBeanContext;
import cn.easygd.dynaguard.core.engine.Validator;
import cn.easygd.dynaguard.core.guard.CounterGuard;
import cn.easygd.dynaguard.core.guard.CounterGuardManager;
import cn.easygd.dynaguard.core.guard.LocalCounterGuard;
import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.core.holder.GlobalBeanContextHolder;
import cn.easygd.dynaguard.core.metrics.BizValidationStatistics;
import cn.easygd.dynaguard.core.trace.BizTracker;
import cn.easygd.dynaguard.core.trace.ReturnInfo;
import cn.easygd.dynaguard.domain.ValidationNode;
import cn.easygd.dynaguard.domain.ValidationResult;
import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.ValidationErrorEnum;
import cn.easygd.dynaguard.domain.exception.ValidationFailedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * 验证链
 *
 * @author VD
 * @version v 0.1 2025/7/29 21:38
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
     * 熔断计数器过期时间，默认10秒
     */
    private Long guardExpire = 10L;

    /**
     * 熔断计数器阈值，默认100
     */
    private Long guardThreshold = 100L;

    /**
     * 执行验证链
     *
     * @param context 上下文
     */
    public void execute(ValidationContext context) {
        ValidationResult validationResult = executeResult(context);
        if (!validationResult.getSuccess()) {
            throw new ValidationFailedException(ValidationErrorEnum.FAIL.getErrorCode(), validationResult.getMessage());
        }
    }

    /**
     * 执行验证链
     *
     * @param context 上下文
     */
    public void executeGuard(ValidationContext context) {
        ValidationResult result = executeGuardResult(context);
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
        return executeTemplate(context, false);
    }

    /**
     * 执行验证链
     *
     * @param context 上下文
     */
    public ValidationResult executeGuardResult(ValidationContext context) {
        return executeTemplate(context, true);
    }

    /**
     * 执行模板
     *
     * @param context     上下文
     * @param enableGuard 是否开启熔断
     * @return 验证结果
     */
    private ValidationResult executeTemplate(ValidationContext context,
                                             Boolean enableGuard) {
        // 获取配置
        ValidationChainConfig config = ChainConfigHolder.getConfig();
        Boolean enableBizTrace = config.getEnableBizTrace();

        // 全局bean管理
        GlobalBeanContext globalBeanContext = GlobalBeanContextHolder.getContext();

        // 获取业务统计器
        BizValidationStatistics statistics = globalBeanContext.getBizValidationStatistics();

        // 获取熔断计数器
        CounterGuardManager guardManager = globalBeanContext.getCounterGuardManager();
        CounterGuard guard = guardManager.getGuard(this.chainId);
        if (enableGuard) {
            if (Objects.isNull(guard)) {
                log.info("guard is null , register local guard : [{}]", this.chainId);
                guard = new LocalCounterGuard(this.chainId, this.guardExpire);
                guardManager.register(this.chainId, guard);
            } else {
                // 如果触发熔断直接返回失败结果
                if (guard.isExceedThreshold(this.chainId, this.guardThreshold)) {
                    log.info("guard exceed threshold : [{}=={}]", this.chainId, this.guardThreshold);
                    if (enableBizTrace) {
                        statistics.incrementCount(this.chainId);
                        statistics.incrementGuardCount(this.chainId);
                    }
                    guard.rollback(this.chainId);

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
                result = executeTemplate(context);

                if (!result.getSuccess()) {
                    // 获取跟踪信息
                    ReturnInfo returnInfo = BizTracker.get();
                    // 增加未通过次数
                    statistics.incrementValidationCount(this.chainId, result.getNodeName(), returnInfo.getTriggerCondition());
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
            result = executeTemplate(context);
        }

        if (enableGuard) {
            if (!result.getSuccess()) {
                // 自增
                guard.increment(chainId);
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
    private ValidationResult executeTemplate(ValidationContext context) {
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

    public Long getGuardExpire() {
        return guardExpire;
    }

    public void setGuardExpire(Long guardExpire) {
        this.guardExpire = guardExpire;
    }

    public Long getGuardThreshold() {
        return guardThreshold;
    }

    public void setGuardThreshold(Long guardThreshold) {
        this.guardThreshold = guardThreshold;
    }
}
