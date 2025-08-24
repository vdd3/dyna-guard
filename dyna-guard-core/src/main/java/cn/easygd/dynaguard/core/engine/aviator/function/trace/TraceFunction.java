package cn.easygd.dynaguard.core.engine.aviator.function.trace;

import cn.easygd.dynaguard.core.trace.BizTracker;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * 追踪函数
 *
 * @author VD
 * @version v 0.1 2025/8/23 20:48
 */
public class TraceFunction extends AbstractFunction {

    /**
     * 函数
     *
     * @param env  环境
     * @param arg1 参数1
     * @return 函数返回值
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        // 参数：条件文本、行号、是否成立
        String condition = FunctionUtils.getStringValue(arg1, env);

        // 记录触发信息
        BizTracker.recordTriggerCondition(condition);
        return null;
    }

    /**
     * 函数名
     *
     * @return 函数名
     */
    @Override
    public String getName() {
        return "trace";
    }
}
