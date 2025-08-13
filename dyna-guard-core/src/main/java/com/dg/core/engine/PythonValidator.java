package com.dg.core.engine;

import com.dg.domain.ValidationContext;
import com.dg.domain.enums.RuleEngineEnum;
import com.dg.domain.exception.ResultTypeIllegalException;
import com.dg.domain.exception.ValidationChainEngineException;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * python 规则引擎
 *
 * @author VD
 * @date 2025/7/29 21:24
 */
public class PythonValidator extends BaseValidator {

    /**
     * 脚本引擎管理器
     */
    private final ScriptEngineManager manager = new ScriptEngineManager();

    /**
     * 执行
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 是否符合规则
     */
    @Override
    public Boolean validate(String script, ValidationContext context) {
        Object result;
        try {
            ScriptEngine engine = manager.getEngineByName("python");
            // 配置引擎，允许访问 Java 对象
            engine.put("polyglot.python.allowAllAccess", true);
            // 创建脚本上下文
            Bindings bindings = engine.createBindings();
            context.getParameters().forEach(bindings::put);
            bindings.put("beanContext", context.getGlobalBeanContext());
            // 执行表达式
            result = engine.eval(script, bindings);
        } catch (Exception e) {
            throw new ValidationChainEngineException("Python execute exception : " + e.getMessage(), e);
        }
        // 验证结果类型
        if (!(result instanceof Boolean)) {
            throw new ResultTypeIllegalException();
        }
        return (Boolean) result;
    }

    /**
     * 获取语言
     *
     * @return 语言
     */
    @Override
    public String getLanguage() {
        return RuleEngineEnum.PYTHON.getLanguageName();
    }
}
