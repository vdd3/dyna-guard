package com.dg.core.engine.qle;

import com.dg.core.engine.qle.operator.bean.GetBeanOfNameOperator;
import com.dg.core.engine.qle.operator.common.NotNullOperator;
import com.dg.core.engine.qle.operator.common.PhoneValidatorOperator;
import com.dg.core.engine.qle.operator.range.*;
import com.dg.domain.ValidationContext;
import com.dg.domain.constants.QleConstants;
import com.dg.domain.exception.ResultTypeIllegalException;
import com.dg.domain.exception.ValidationChainEngineException;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

/**
 * 熔断脚本执行器
 *
 * @author VD
 * @date 2025/8/10 22:02
 */
public class GuardScriptExecutor {

    /**
     * 单例
     */
    private static volatile GuardScriptExecutor instance;

    /**
     * Qle解析引擎
     */
    public final static ExpressRunner EXPRESS_RUNNER = new ExpressRunner();

    static {
        EXPRESS_RUNNER.addFunction(QleConstants.NOT_NULL, new NotNullOperator());
        EXPRESS_RUNNER.addFunction(QleConstants.IN_CLOSED_RANGE, new InClosedRangeOperator());
        EXPRESS_RUNNER.addFunction(QleConstants.IN_OPEN_RANGE, new InOpenRangeOperator());
        EXPRESS_RUNNER.addFunction(QleConstants.IN_OPEN_CLOSED_RANGE, new InClosedRangeOperator());
        EXPRESS_RUNNER.addFunction(QleConstants.IN_CLOSED_OPEN_RANGE, new InOpenRangeOperator());
        EXPRESS_RUNNER.addFunction(QleConstants.IN_AT_LEAST_RANGE, new InAtLeastRangeOperator());
        EXPRESS_RUNNER.addFunction(QleConstants.IN_AT_MOST_RANGE, new InAtMostRangeOperator());
        EXPRESS_RUNNER.addFunction(QleConstants.IN_GREATER_THAN_RANGE, new InGreaterThanRangeOperator());
        EXPRESS_RUNNER.addFunction(QleConstants.IN_LESS_THAN_RANGE, new InLessThanRangeOperator());
        EXPRESS_RUNNER.addFunction(QleConstants.IS_PHONE, new PhoneValidatorOperator());
        EXPRESS_RUNNER.addFunction(QleConstants.GET_BEAN, new GetBeanOfNameOperator());
    }

    /**
     * 执行脚本
     *
     * @param script  脚本
     * @param context 上下文
     * @return 执行结果
     */
    public Boolean execute(String script, ValidationContext context) {
        try {
            DefaultContext<String, Object> expressContext = new DefaultContext<>();
            expressContext.putAll(context.getParameters());
            Object result = EXPRESS_RUNNER.execute(script, expressContext, null, true, true);
            if (!(result instanceof Boolean)) {
                throw new ResultTypeIllegalException();
            }
            return (Boolean) result;
        } catch (ResultTypeIllegalException | ValidationChainEngineException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidationChainEngineException("qle execute exception : " + e.getMessage(), e);
        }
    }

    public static GuardScriptExecutor init() {
        if (instance == null) {
            synchronized (GuardScriptExecutor.class) {
                if (instance == null) {
                    instance = new GuardScriptExecutor();
                }
            }
        }
        return instance;
    }

    private GuardScriptExecutor() {
    }

}
