package com.easytu.dynaguard.core.engine;

import com.easytu.dynaguard.domain.ValidationResult;
import com.easytu.dynaguard.domain.context.ValidationContext;
import com.easytu.dynaguard.domain.exception.ResultTypeIllegalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础规则引擎
 *
 * @author VD
 * @date 2025/7/28 20:45
 */
public abstract class BaseValidator implements Validator {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(BaseValidator.class);

    /**
     * 执行
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 验证结果
     */
    @Override
    public ValidationResult execute(String script, ValidationContext context) {
        try {
            Boolean flag = validate(script, context);
            return flag ? ValidationResult.success() : ValidationResult.fail();
        } catch (ResultTypeIllegalException e) {
            throw e;
        } catch (Exception e) {
            log.warn("validate catch exception , script : [{}] , context : [{}]", script, context, e);
            return ValidationResult.fail(e.getMessage(), e);
        }
    }

    /**
     * 验证
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 是否成功
     */
    protected abstract Boolean validate(String script, ValidationContext context);
}
