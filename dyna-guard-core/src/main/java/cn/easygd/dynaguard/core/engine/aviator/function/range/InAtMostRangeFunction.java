package cn.easygd.dynaguard.core.engine.aviator.function.range;

import cn.easygd.dynaguard.domain.constants.CustomFunctionConstants;
import cn.easygd.dynaguard.utils.CustomCheckUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * 最大值判断函数
 *
 * @author VD
 * @date 2025/8/20 19:58
 */
public class InAtMostRangeFunction extends AbstractFunction {

    /**
     * 函数
     *
     * @param env  环境
     * @param arg1 参数1
     * @param arg2 参数2
     * @return 函数返回值
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        super.call(env, arg1, arg2);
        Object var1 = FunctionUtils.getJavaObject(arg1, env);
        Object value = FunctionUtils.getJavaObject(arg2, env);
        return AviatorBoolean.valueOf(CustomCheckUtils.atMost(var1, value));
    }

    /**
     * 函数名
     *
     * @return 函数名
     */
    @Override
    public String getName() {
        return CustomFunctionConstants.IN_AT_MOST_RANGE;
    }
}
