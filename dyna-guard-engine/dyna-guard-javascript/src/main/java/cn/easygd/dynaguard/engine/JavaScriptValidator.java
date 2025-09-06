package cn.easygd.dynaguard.engine;

import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.core.trace.BizTracker;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;

import javax.script.*;
import java.util.Map;

/**
 * JavaScript脚本验证器
 *
 * @author VD
 */
public class JavaScriptValidator extends BaseValidator {

    /**
     * 脚本引擎
     */
    private final ScriptEngine scriptEngine;

    /**
     * 全局绑定
     */
    private final Bindings globalBindings;

    /**
     * 编译
     *
     * @param script 脚本
     * @return 编译结果
     * @throws Exception 编译异常
     */
    @Override
    public Object compile(String script) throws Exception {
        return ((Compilable) scriptEngine).compile(script);
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
        CompiledScript compiledScript = (CompiledScript) script;

        // 创建脚本上下文
        Map<String, Object> params = buildParam(context);
        Bindings bindings = new SimpleBindings();
        params.forEach(bindings::put);
        bindings.putAll(globalBindings);

        return compiledScript.eval(bindings);
    }

    /**
     * 获取语言
     *
     * @return 语言
     */
    @Override
    public String getLanguage() {
        return RuleEngineEnum.JAVA_SCRIPT.getLanguageName();
    }

    public JavaScriptValidator() {
        scriptEngine = new ScriptEngineManager().getEngineByName(getLanguage());
        globalBindings = new SimpleBindings();
        globalBindings.put("Trace", BizTracker.class);
        scriptEngine.setBindings(globalBindings, ScriptContext.GLOBAL_SCOPE);
    }
}
