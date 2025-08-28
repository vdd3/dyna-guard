package cn.easygd.dynaguard.engine;

import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;
import cn.easygd.dynaguard.domain.exception.ValidationChainEngineException;
import cn.easygd.dynaguard.engine.wrapper.ScriptWrapper;

import javax.script.*;
import java.util.Map;

/**
 * JavaScript脚本验证器
 *
 * @author VD
 * @version v 0.1 2025/8/19 20:41
 */
public class JavaScriptValidator extends BaseValidator {

    /**
     * 脚本引擎
     */
    private final ScriptEngine scriptEngine;

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
        script = ScriptWrapper.wrapScript(script);
        // 判断用户是否书写了函数并且执行了函数
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
    protected Boolean validate(Object script, ValidationContext context) throws Exception {
        CompiledScript compiledScript = (CompiledScript) script;

        // 创建脚本上下文
        Map<String, Object> params = buildParam(context);
        Bindings bindings = new SimpleBindings();
        params.forEach(bindings::put);

        bindings.put("__return_site", null);
        bindings.putAll(globalBindings);


        // 执行脚本
        Object result = compiledScript.eval(bindings);

        Object infoObj = bindings.get("__return_site");
        if (infoObj instanceof Bindings) {
            Bindings infoBindings = (Bindings) infoObj;

        } else {
            // fallback：没有 track 到，说明是最后一个 return 没被替换（比如没有分号）

        }

        return checkResult(result);
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
//        globalBindings.put("__return_site", null);
        scriptEngine.setBindings(globalBindings, ScriptContext.GLOBAL_SCOPE);
        try {
            scriptEngine.eval(ScriptWrapper.HELPER_FUNCTIONS, globalBindings);
        } catch (ScriptException e) {
            throw new ValidationChainEngineException("Failed to inject helper functions", e);
        }
    }
}
