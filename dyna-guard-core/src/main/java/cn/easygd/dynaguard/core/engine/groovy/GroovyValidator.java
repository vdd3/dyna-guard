package cn.easygd.dynaguard.core.engine.groovy;

import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;

import java.util.Map;

/**
 * groovy规则引擎
 *
 * @author VD
 * @date 2025/7/28 20:49
 */
public class GroovyValidator extends BaseValidator {

    /**
     * 类加载器
     */
    private final GroovyClassLoader groovyClassLoader = new GroovyClassLoader();

    /**
     * 编译
     *
     * @param script 脚本
     * @return 编译结果
     * @throws Exception 编译异常
     */
    @Override
    public Object compile(String script) throws Exception {
        Class<?> scriptClass = groovyClassLoader.parseClass(script);
        return scriptClass.getDeclaredConstructor().newInstance();
    }

    /**
     * 执行
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 是否符合规则
     */
    @Override
    public Boolean validate(Object script, ValidationContext context) {
        Script finalScript = (Script) script;

        // 参数传递
        Map<String, Object> params = buildParam(context);
        Binding binding = new Binding();
        params.forEach(binding::setVariable);

        // 执行
        finalScript.setBinding(binding);
        Object result = finalScript.run();

        return checkResult(result);
    }

    /**
     * 获取语言
     *
     * @return 语言
     */
    @Override
    public String getLanguage() {
        return RuleEngineEnum.GROOVY.getLanguageName();
    }
}
