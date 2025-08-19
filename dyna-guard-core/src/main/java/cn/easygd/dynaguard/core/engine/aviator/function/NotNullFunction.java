package cn.easygd.dynaguard.core.engine.aviator.function;

import cn.easygd.dynaguard.domain.constants.CustomFunctionConstants;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author VD
 * @date 2025/8/19 21:44
 */
public class NotNullFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        Object object = FunctionUtils.getJavaObject(arg1, env);
        return AviatorBoolean.valueOf(Optional.ofNullable(object)
                .map(source -> {
                    if (source instanceof String) {
                        return StringUtils.isNotBlank((String) source);
                    } else if (source instanceof Collection) {
                        return CollectionUtils.isNotEmpty((Collection) source);
                    } else if (source instanceof Map) {
                        return MapUtils.isNotEmpty((Map) source);
                    } else {
                        return true;
                    }
                }).orElse(false));
    }

    @Override
    public String getName() {
        return CustomFunctionConstants.NOT_NULL;
    }
}
