package cn.easygd.dynaguard.core.engine.aviator.function.range;

import cn.easygd.dynaguard.domain.constants.CustomFunctionConstants;
import cn.easygd.dynaguard.utils.CustomCheckUtils;
import cn.easygd.dynaguard.utils.TypeConvertUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * 在闭开区间内判断函数
 *
 * @author VD
 * @date 2025/8/20 20:00
 */
public class InClosedOpenRangeFunction extends AbstractFunction {

    /**
     * 函数名称
     *
     * @param env  运行环境
     * @param arg1 参数1
     * @param arg2 参数2
     * @param arg3 参数3
     * @return 函数结果
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        Object value = FunctionUtils.getJavaObject(arg3, env);
        Object var1 = TypeConvertUtils.convert(arg1.getValue(env), value.getClass());
        Object var2 = TypeConvertUtils.convert(arg2.getValue(env), value.getClass());
        return AviatorBoolean.valueOf(CustomCheckUtils.inClosedOpenRange(var1, var2, value));
    }

    /**
     * 函数名
     *
     * @return 函数名
     */
    @Override
    public String getName() {
        return CustomFunctionConstants.IN_CLOSED_OPEN_RANGE;
    }
}
