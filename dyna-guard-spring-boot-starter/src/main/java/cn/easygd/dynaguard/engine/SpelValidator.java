package cn.easygd.dynaguard.engine;

import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.core.holder.GlobalBeanContextHolder;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;
import cn.easygd.dynaguard.domain.exception.ResultTypeIllegalException;
import com.google.common.collect.Maps;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * spel脚本验证
 *
 * @author VD
 * @date 2025/8/12 20:27
 */
public class SpelValidator extends BaseValidator {

    /**
     * SpEL表达式解析器
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
    public Boolean validate(Object script, ValidationContext context) throws Exception {
        // 构建上下文
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

        Object beanManager = GlobalBeanContextHolder.getContext().getBeanManager();
        if (beanManager instanceof ApplicationContext) {
            ApplicationContext applicationContext = (ApplicationContext) beanManager;
            evaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
        }

        // 注入上下文
        Map<String, Object> params = Maps.newHashMap();
        context.buildExecuteContext().accept(params);
        params.forEach(evaluationContext::setVariable);

        // 解析并执行SpEL表达式
        Expression expression = (Expression) script;
        Object result = expression.getValue(evaluationContext);

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

    /**
     * 编译
     *
     * @param script 脚本
     * @return 编译结果
     */
    @Override
    public Object compile(String script) throws Exception {
        return parser.parseExpression(script);
    }
}
