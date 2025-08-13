package com.dg.core.chain;

import com.dg.core.engine.Validator;
import com.dg.core.guard.CounterGuard;
import com.dg.core.guard.CounterGuardManager;
import com.dg.core.guard.LocalCounterGuard;
import com.dg.core.holder.GlobalBeanContextHolder;
import com.dg.domain.ValidationContext;
import com.dg.domain.ValidationNode;
import com.dg.domain.ValidationResult;
import com.dg.domain.enums.ValidationErrorEnum;
import com.dg.domain.exception.ValidationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * 验证链
 *
 * @author VD
 * @date 2025/7/29 21:38
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
                // 如果快速失败则抛出异常
                if (node.getFastFail()) {
                    // 需要判断是否是因为执行异常导致的验证失败
                    if (result.getException()) {
                        throw new ValidationFailedException(ValidationErrorEnum.SCRIPT_EXECUTE_ERROR, result.getThrowable());
                    } else {
                        throw new ValidationFailedException(ValidationErrorEnum.FAIL.getErrorCode(), node.getMessage(), script);
                    }
                } else {
                    // 否则打印日志
                    log.info("validation fail but skip");
                }
            }
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
            if (result.getException()) {
                throw new ValidationFailedException(ValidationErrorEnum.SCRIPT_EXECUTE_ERROR, result.getThrowable());
            } else {
                throw new ValidationFailedException(ValidationErrorEnum.FAIL.getErrorCode(), result.getMessage());
            }
        }

    }

    /**
     * 执行验证链
     *
     * @param context 上下文
     * @return 验证结果
     */
    public ValidationResult executeResult(ValidationContext context) {
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
                // 如果快速失败则抛出异常
                if (node.getFastFail()) {
                    return result;
                } else {
                    // 否则打印日志
                    log.info("validation fail but skip");
                }
            }
        }
        return ValidationResult.success();
    }

    /**
     * 执行验证链
     *
     * @param context 上下文
     */
    public ValidationResult executeGuardResult(ValidationContext context) {
        CounterGuardManager guardManager = GlobalBeanContextHolder.getCounterGuardManager();
        CounterGuard guard = guardManager.getGuard(this.chainId);
        if (Objects.isNull(guard)) {
            log.info("guard is null , register local guard : [{}]", this.chainId);
            guard = new LocalCounterGuard(this.chainId, this.guardExpire);
            guardManager.register(this.chainId, guard);
        } else {
            // 如果触发熔断直接返回失败结果
            if (guard.isExceedThreshold(this.chainId, this.guardThreshold)) {
                log.info("guard exceed threshold : [{}=={}]", this.chainId, this.guardThreshold);
                guard.rollback(this.chainId);
                return ValidationResult.fail(String.format("%s guard exceed threshold", this.chainId));
            }
        }

        ValidationResult result = executeResult(context);

        if (!result.getSuccess()) {
            // 自增并且判断是否超过阈值
            guard.increment(chainId);
            if (guard.isExceedThreshold(this.chainId, this.guardThreshold)) {
                log.info("guard exceed threshold : [{}=={}]", this.chainId, this.guardThreshold);
                guard.rollback(this.chainId);
            }
        }

        return result;
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
