package cn.easygd.dynaguard.engine;

import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;
import cn.easygd.dynaguard.engine.function.common.NotNullFunction;
import cn.easygd.dynaguard.engine.function.range.*;
import cn.easygd.dynaguard.engine.function.trace.TraceFunction;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;

import java.util.Map;

/**
 * Aviator脚本验证器
 *
 * @author VD
 * @version v 0.1 2025/8/19 21:23
 */
public class AviatorValidator extends BaseValidator {

    /**
     * Aviator实例
     */
    private static final AviatorEvaluatorInstance ENGINE = AviatorEvaluator.getInstance();

    /**
     * 编译
     *
     * @param script 脚本
     * @return 编译结果
     * @throws Exception 编译异常
     */
    @Override
    public Object compile(String script) throws Exception {
        return ENGINE.compile(script);
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

    /**
     * 构造方法
     */
    public AviatorValidator() {
        // 注册函数
        ENGINE.addFunction(new NotNullFunction());
        ENGINE.addFunction(new InOpenRangeFunction());
        ENGINE.addFunction(new InOpenClosedRangeFunction());
        ENGINE.addFunction(new InClosedRangeFunction());
        ENGINE.addFunction(new InClosedOpenRangeFunction());
        ENGINE.addFunction(new InAtLeastRangeFunction());
        ENGINE.addFunction(new InAtMostRangeFunction());
        ENGINE.addFunction(new InGreaterThanRangeFunction());
        ENGINE.addFunction(new InLessThanRangeFunction());
        ENGINE.addFunction(new TraceFunction());
    }
}
