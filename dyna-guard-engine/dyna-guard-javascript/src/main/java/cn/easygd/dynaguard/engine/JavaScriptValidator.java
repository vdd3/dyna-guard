package cn.easygd.dynaguard.engine;

import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;

import javax.script.*;
import java.util.Map;

/**
 * JavaScript脚本验证器
 *
 * @author VD
 * @date 2025/8/19 20:41
 */
public class JavaScriptValidator extends BaseValidator {

    /**
     * 脚本引擎
     */
    private final ScriptEngine scriptEngine;

    /**
     * 编译
     *
     * @param script 脚本
     * @return 编译结果
     * @throws Exception 编译异常
     */
    @Override
    public Object compile(String script) throws Exception {
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

        // 执行脚本
        Object result = compiledScript.eval(bindings);

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
    }
}
