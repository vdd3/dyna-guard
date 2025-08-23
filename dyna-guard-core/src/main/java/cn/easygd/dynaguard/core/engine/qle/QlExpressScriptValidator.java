package cn.easygd.dynaguard.core.engine.qle;

import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.core.engine.qle.operator.bean.InvokeBeanMethodFunctionQlE;
import cn.easygd.dynaguard.core.engine.qle.operator.common.NotNullFunctionQlE;
import cn.easygd.dynaguard.core.engine.qle.operator.common.PhoneValidatorFunctionQlE;
import cn.easygd.dynaguard.core.engine.qle.operator.range.*;
import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.core.trace.BizTracker;
import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import cn.easygd.dynaguard.domain.constants.CustomFunctionConstants;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;
import com.alibaba.qlexpress4.Express4Runner;
import com.alibaba.qlexpress4.InitOptions;
import com.alibaba.qlexpress4.QLOptions;
import com.alibaba.qlexpress4.QLResult;
import com.alibaba.qlexpress4.runtime.trace.ExpressionTrace;
import com.alibaba.qlexpress4.security.QLSecurityStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * QlExpress脚本验证器
 *
 * @author VD
 * @date 2025/7/30 22:14
 */
public class QlExpressScriptValidator extends BaseValidator {

    /**
     * 运行器
     */
    public final Express4Runner EXPRESS_4_RUNNER;

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
        // 根据配置选择是否开启业务追踪
        ValidationChainConfig config = ChainConfigHolder.getConfig();
        Boolean enableBizTrace = config.getEnableBizTrace();

        // 参数传递
        Map<String, Object> params = buildParam(context);
        // 对于qle来说无法推断复杂类型的参数，所以无法直接获取bean使用
        params.remove("beanContext");

        // 设置运行参数,开启脚本缓存
        QLOptions.Builder builder = QLOptions.builder().cache(true);
        if (enableBizTrace) {
            builder.traceExpression(true);
        }
        QLOptions qlOptions = builder.build();
        QLResult qlResult = EXPRESS_4_RUNNER.execute((String) script, params, qlOptions);

        List<ExpressionTrace> expressionTraces = qlResult.getExpressionTraces();

        Boolean result = checkResult(qlResult.getResult());
        if (!result && enableBizTrace) {
            // 设置业务追踪信息
            expressionTraces.stream().sorted(Comparator.comparing(ExpressionTrace::getPosition).reversed())
                    .filter(ExpressionTrace::isEvaluated)
                    .findFirst()
                    .ifPresent(trace -> {
                        BizTracker.recordLineNumber(trace.getLine());
                    });
        }
        return result;
    }

    /**
     * 获取语言
     *
     * @return 语言
     */
    @Override
    public String getLanguage() {
        return RuleEngineEnum.QLEXPRESS4.getLanguageName();
    }

    /**
     * 构造函数
     */
    public QlExpressScriptValidator() {
        // 根据配置选择是否开启业务追踪
        ValidationChainConfig config = ChainConfigHolder.getConfig();

        // 开启开放安全策略
        InitOptions.Builder builder = InitOptions.builder().securityStrategy(QLSecurityStrategy.open());
        if (config.getEnableBizTrace()) {
            builder.traceExpression(true);
        }

        EXPRESS_4_RUNNER = new Express4Runner(builder.build());

        // 注册自定义操作符
        EXPRESS_4_RUNNER.addVarArgsFunction(CustomFunctionConstants.NOT_NULL, new NotNullFunctionQlE());
        EXPRESS_4_RUNNER.addVarArgsFunction(CustomFunctionConstants.IN_CLOSED_RANGE, new InClosedRangeFunctionQlE());
        EXPRESS_4_RUNNER.addVarArgsFunction(CustomFunctionConstants.IN_OPEN_RANGE, new InOpenRangeFunctionQlE());
        EXPRESS_4_RUNNER.addVarArgsFunction(CustomFunctionConstants.IN_OPEN_CLOSED_RANGE, new InClosedRangeFunctionQlE());
        EXPRESS_4_RUNNER.addVarArgsFunction(CustomFunctionConstants.IN_CLOSED_OPEN_RANGE, new InOpenRangeFunctionQlE());
        EXPRESS_4_RUNNER.addVarArgsFunction(CustomFunctionConstants.IN_AT_LEAST_RANGE, new InAtLeastRangeFunctionQlE());
        EXPRESS_4_RUNNER.addVarArgsFunction(CustomFunctionConstants.IN_AT_MOST_RANGE, new InAtMostRangeFunctionQlE());
        EXPRESS_4_RUNNER.addVarArgsFunction(CustomFunctionConstants.IN_GREATER_THAN_RANGE, new InGreaterThanRangeFunctionQlE());
        EXPRESS_4_RUNNER.addVarArgsFunction(CustomFunctionConstants.IN_LESS_THAN_RANGE, new InLessThanRangeFunctionQlE());
        EXPRESS_4_RUNNER.addVarArgsFunction(CustomFunctionConstants.IS_PHONE, new PhoneValidatorFunctionQlE());
        EXPRESS_4_RUNNER.addVarArgsFunction(CustomFunctionConstants.INVOKE_BEAN_METHOD, new InvokeBeanMethodFunctionQlE());
    }
}
