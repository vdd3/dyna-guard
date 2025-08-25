package cn.easygd.dynaguard.engine.function.common;

import cn.easygd.dynaguard.domain.constants.CustomFunctionConstants;
import cn.easygd.dynaguard.utils.CustomCheckUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * 非空函数
 *
 * @author VD
 * @version v 0.1 2025/8/19 21:44
 */
public class NotNullFunction extends AbstractFunction {

    /**
     * 判断参数是否非空
     *
     * @param env  环境
     * @param arg1 参数1
     * @return 是否非空
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        Object object = FunctionUtils.getJavaObject(arg1, env);
        return AviatorBoolean.valueOf(CustomCheckUtils.notNull(object));
    }

    /**
     * 获取函数名
     *
     * @return 函数名
     */
    @Override
    public String getName() {
        return CustomFunctionConstants.NOT_NULL;
    }
}
