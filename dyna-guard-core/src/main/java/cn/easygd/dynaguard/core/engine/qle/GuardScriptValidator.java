package cn.easygd.dynaguard.core.engine.qle;

import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.core.engine.qle.operator.bean.InvokeBeanMethodOperator;
import cn.easygd.dynaguard.core.engine.qle.operator.common.NotNullOperator;
import cn.easygd.dynaguard.core.engine.qle.operator.common.PhoneValidatorOperator;
import cn.easygd.dynaguard.core.engine.qle.operator.range.*;
import cn.easygd.dynaguard.domain.constants.CustomFunctionConstants;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

import java.util.Map;

/**
 * 熔断脚本验证器
 *
 * @author VD
 * @date 2025/7/30 22:14
 */
public class GuardScriptValidator extends BaseValidator {
    /**
     * Qle解析引擎
     */
    public final static ExpressRunner EXPRESS_RUNNER = new ExpressRunner();

    /**
     * 编译
     *
     * @param script 脚本
     * @return 编译结果
     * @throws Exception 编译异常
     */
    @Override
    public Object compile(String script) throws Exception {
        return script;
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
        // 参数传递
        Map<String, Object> params = buildParam(context);
        // 对于qle来说无法推断复杂类型的参数，所以无法直接获取bean使用
        params.remove("beanContext");

        // 创建脚本上下文
        DefaultContext<String, Object> expressContext = new DefaultContext<>();
        expressContext.putAll(params);

        // 执行
        Object result = EXPRESS_RUNNER.execute((String) script, expressContext, null, true, true);

        return checkResult(result);
    }

    /**
     * 获取语言
     *
     * @return 语言
     */
    @Override
    public String getLanguage() {
        return RuleEngineEnum.GUARD_SCRIPT.getLanguageName();
    }


    public GuardScriptValidator() {
        // 注册
        EXPRESS_RUNNER.addFunction(CustomFunctionConstants.NOT_NULL, new NotNullOperator());
        EXPRESS_RUNNER.addFunction(CustomFunctionConstants.IN_CLOSED_RANGE, new InClosedRangeOperator());
        EXPRESS_RUNNER.addFunction(CustomFunctionConstants.IN_OPEN_RANGE, new InOpenRangeOperator());
        EXPRESS_RUNNER.addFunction(CustomFunctionConstants.IN_OPEN_CLOSED_RANGE, new InClosedRangeOperator());
        EXPRESS_RUNNER.addFunction(CustomFunctionConstants.IN_CLOSED_OPEN_RANGE, new InOpenRangeOperator());
        EXPRESS_RUNNER.addFunction(CustomFunctionConstants.IN_AT_LEAST_RANGE, new InAtLeastRangeOperator());
        EXPRESS_RUNNER.addFunction(CustomFunctionConstants.IN_AT_MOST_RANGE, new InAtMostRangeOperator());
        EXPRESS_RUNNER.addFunction(CustomFunctionConstants.IN_GREATER_THAN_RANGE, new InGreaterThanRangeOperator());
        EXPRESS_RUNNER.addFunction(CustomFunctionConstants.IN_LESS_THAN_RANGE, new InLessThanRangeOperator());
        EXPRESS_RUNNER.addFunction(CustomFunctionConstants.IS_PHONE, new PhoneValidatorOperator());
        EXPRESS_RUNNER.addFunction(CustomFunctionConstants.INVOKE_BEAN_METHOD, new InvokeBeanMethodOperator());
    }
}
