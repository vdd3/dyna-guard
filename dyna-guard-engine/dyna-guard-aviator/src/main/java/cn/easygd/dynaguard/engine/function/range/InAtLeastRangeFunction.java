package cn.easygd.dynaguard.engine.function.range;

import cn.easygd.dynaguard.domain.constants.CustomFunctionConstants;
import cn.easygd.dynaguard.utils.CustomCheckUtils;
import cn.easygd.dynaguard.utils.TypeConvertUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * 最小值判断函数
 *
 * @author VD
 */
public class InAtLeastRangeFunction extends AbstractFunction {

    /**
     * 函数
     *
     * @param env  环境
     * @param arg1 参数1
     * @param arg2 参数2
     * @return 函数结果
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Object value = FunctionUtils.getJavaObject(arg2, env);
        Object var1 = TypeConvertUtils.convert(arg1.getValue(env), value.getClass());
        return AviatorBoolean.valueOf(CustomCheckUtils.atLeast(var1, value));
    }

    /**
     * 获取函数名称
     *
     * @return 函数名称
     */
    @Override
    public String getName() {
        return CustomFunctionConstants.IN_AT_LEAST_RANGE;
    }
}
