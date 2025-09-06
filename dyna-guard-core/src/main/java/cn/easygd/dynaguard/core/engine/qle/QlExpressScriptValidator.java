package cn.easygd.dynaguard.core.engine.qle;

import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.core.engine.qle.function.bean.InvokeBeanMethodFunctionQlE;
import cn.easygd.dynaguard.core.engine.qle.function.common.NotNullFunctionQlE;
import cn.easygd.dynaguard.core.engine.qle.function.common.PhoneValidatorFunctionQlE;
import cn.easygd.dynaguard.core.engine.qle.function.range.*;
import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.core.trace.BizTracker;
import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import cn.easygd.dynaguard.domain.constants.CustomFunctionConstants;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;
import cn.easygd.dynaguard.utils.QLExpressTraceUtils;
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
    protected Object validate(Object script, ValidationContext context) throws Exception {
        // 根据配置选择是否开启业务追踪
        ValidationChainConfig config = ChainConfigHolder.getConfig();
        Boolean enableBizTrace = config.getEnableBizTrace();

        // 参数传递
        Map<String, Object> params = buildParam(context);
        params.remove("beanContext");

        // 设置运行参数,开启脚本缓存
        QLOptions.Builder builder = QLOptions.builder().cache(true);
        if (enableBizTrace) {
            builder.traceExpression(true);
        }
        QLOptions qlOptions = builder.build();
        QLResult qlResult = EXPRESS_4_RUNNER.execute((String) script, params, qlOptions);

        List<ExpressionTrace> expressionTraces = qlResult.getExpressionTraces();

        if (enableBizTrace) {
            String condition = buildCondition(expressionTraces);
            BizTracker.recordTriggerCondition(condition);
        }
        return qlResult.getResult();
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

    /**
     * 构建触发条件
     *
     * @param expressionTraces 表达式链路
     * @return 触发条件
     */
    private String buildCondition(List<ExpressionTrace> expressionTraces) {
        ExpressionTrace trace;
        // 如果都是短路节点代表最后一个节点校验没有通过
        if (expressionTraces.stream().allMatch(ExpressionTrace::isEvaluated)) {
            trace = expressionTraces.get(expressionTraces.size() - 1);
        } else {
            // 获取执行节点的下一个节点
            int lastIndex = expressionTraces.stream().sorted(Comparator.comparing(ExpressionTrace::getPosition).reversed())
                    .filter(ExpressionTrace::isEvaluated)
                    .findFirst()
                    .map(expressionTraces::indexOf)
                    .orElse(0);
            int index = lastIndex == 0 ? lastIndex : lastIndex + 1;
            if (index > expressionTraces.size() - 1) {
                trace = expressionTraces.get(lastIndex);
            } else {
                trace = expressionTraces.get(index);
            }
        }
        return QLExpressTraceUtils.getCondition(trace, new StringBuilder()).toString();
    }
}
