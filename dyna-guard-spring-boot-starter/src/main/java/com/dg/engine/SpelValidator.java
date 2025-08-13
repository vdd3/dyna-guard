package com.dg.engine;

import com.dg.core.engine.BaseValidator;
import com.dg.core.holder.GlobalBeanContextHolder;
import com.dg.domain.ValidationContext;
import com.dg.domain.enums.RuleEngineEnum;
import com.dg.domain.exception.DynamicValidationException;
import com.dg.domain.exception.ResultTypeIllegalException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * spel脚本验证
 *
 * @author VD
 * @date 2025/8/12 20:27
 */
public class SpelValidator extends BaseValidator {
    /**
     * SpEL表达式解析器（线程安全，可全局共享）
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 执行SpEL表达式验证
     *
     * @param script  执行的SpEL脚本
     * @param context 验证上下文
     * @return 是否符合规则
     */
    @Override
    public Boolean validate(String script, ValidationContext context) {
        Object result;
        try {
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

            ApplicationContext applicationContext = (ApplicationContext) GlobalBeanContextHolder.getBeanContext().getBeanManager();
            evaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));

            // 注入上下文
            context.getParameters().forEach(evaluationContext::setVariable);

            // 解析并执行SpEL表达式
            Expression expression = parser.parseExpression(script);
            result = expression.getValue(evaluationContext);
        } catch (Exception e) {
            // 保持与JavaScriptValidator一致的异常类型和错误码
            throw new DynamicValidationException("1", "SpEL表达式执行失败: " + e.getMessage());
        }

        // 验证结果类型是否为布尔值
        if (!(result instanceof Boolean)) {
            throw new ResultTypeIllegalException();
        }

        return (Boolean) result;
    }

    /**
     * 获取当前规则引擎使用的语言
     *
     * @return 语言名称
     */
    @Override
    public String getLanguage() {
        return RuleEngineEnum.SPEl.getLanguageName();
    }
}
