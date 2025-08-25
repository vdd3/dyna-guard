package cn.easygd.dynaguard.engine;


import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;
import cn.easygd.dynaguard.engine.ast.GroovyTrackingTransformation;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;

import java.util.Map;

/**
 * groovy规则引擎
 *
 * @author VD
 * @version v 0.1 2025/7/28 20:49
 */
public class GroovyValidator extends BaseValidator {

    /**
     * 类加载器
     */
    private final GroovyClassLoader groovyClassLoader;

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

    /**
     * 构造函数
     */
    public GroovyValidator() {
        // 根据配置选择是否开启业务追踪
        ValidationChainConfig validationChainConfig = ChainConfigHolder.getConfig();

        if (validationChainConfig.getEnableBizTrace()) {
            // 设置编译内容增强收集业务追踪信息
            ASTTransformationCustomizer customizer = new ASTTransformationCustomizer(new GroovyTrackingTransformation());
            CompilerConfiguration config = new CompilerConfiguration();
            config.addCompilationCustomizers(customizer);
            groovyClassLoader = new GroovyClassLoader(GroovyValidator.class.getClassLoader(), config);
        } else {
            groovyClassLoader = new GroovyClassLoader();
        }
    }
}
