package cn.easygd.dynaguard.core.engine.qle.extend;

import cn.easygd.dynaguard.core.engine.extend.EngineExtendRegister;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;
import com.alibaba.qlexpress4.api.QLFunctionalVarargs;
import com.alibaba.qlexpress4.runtime.function.CustomFunction;

import java.util.Map;

/**
 * qle 扩展注册
 *
 * @author VD
 */
public abstract class QlEngineExtendRegister implements EngineExtendRegister {

    /**
     * 获取varargs函数
     *
     * @return key 函数名 value 函数
     */
    public abstract Map<String, QLFunctionalVarargs> varargsFunctions();

    /**
     * 获取自定义函数
     *
     * @return key 函数名 value 函数
     */
    public abstract Map<String, CustomFunction> customFunctions();

    /**
     * 获取语言
     *
     * @return 语言
     */
    @Override
    public String getLanguage() {
        return RuleEngineEnum.QLEXPRESS4.getLanguageName();
    }
}
