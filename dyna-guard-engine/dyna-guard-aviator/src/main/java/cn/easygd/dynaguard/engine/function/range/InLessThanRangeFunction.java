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
 * 判断最大值函数
 *
 * @author VD
 * @version v 0.1 2025/8/20 20:07
 */
public class InLessThanRangeFunction extends AbstractFunction {

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
        Object value = FunctionUtils.getJavaObject(arg2, env);
        Object var1 = TypeConvertUtils.convert(arg1.getValue(env), value.getClass());
        return AviatorBoolean.valueOf(CustomCheckUtils.lessThan(var1, value));
    }

    /**
     * 函数名称
     *
     * @return 函数名称
     */
    @Override
    public String getName() {
        return CustomFunctionConstants.IN_LESS_THAN_RANGE;
    }
}
