package cn.easygd.dynaguard.core.engine.aviator;

import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.core.engine.aviator.function.common.NotNullFunction;
import cn.easygd.dynaguard.core.engine.aviator.function.range.*;
import cn.easygd.dynaguard.core.engine.aviator.function.trace.TraceFunction;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;

import java.util.Map;

/**
 * Aviator脚本验证器
 *
 * @author VD
 * @date 2025/8/19 21:23
 */
public class AviatorValidator extends BaseValidator {

    /**
     * Aviator实例
     */
    private static final AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();

    /**
     * 编译
     *
     * @param script 脚本
     * @return 编译结果
     * @throws Exception 编译异常
     */
    @Override
    public Object compile(String script) throws Exception {
        return instance.compile(script);
    }

    /**
     * 验证
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 是否成功
     */
    @Override
    protected Boolean validate(Object script, ValidationContext context) throws Exception {
        Expression expression = (Expression) script;

        // 参数传递
        Map<String, Object> params = buildParam(context);

        // 执行
        Object result = expression.execute(params);

        return checkResult(result);
    }

    /**
     * 获取语言
     *
     * @return 语言
     */
    @Override
    public String getLanguage() {
        return RuleEngineEnum.AVIATOR.getLanguageName();
    }

    public AviatorValidator() {
        // 注册函数
        instance.addFunction(new NotNullFunction());
        instance.addFunction(new InOpenRangeFunction());
        instance.addFunction(new InOpenClosedRangeFunction());
        instance.addFunction(new InClosedRangeFunction());
        instance.addFunction(new InClosedOpenRangeFunction());
        instance.addFunction(new InAtLeastRangeFunction());
        instance.addFunction(new InAtMostRangeFunction());
        instance.addFunction(new InGreaterThanRangeFunction());
        instance.addFunction(new InLessThanRangeFunction());
        instance.addFunction(new TraceFunction());
    }
}
