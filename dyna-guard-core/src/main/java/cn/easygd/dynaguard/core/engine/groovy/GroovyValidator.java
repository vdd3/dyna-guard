package cn.easygd.dynaguard.core.engine.groovy;

import cn.easygd.dynaguard.core.engine.BaseValidator;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.enums.RuleEngineEnum;
import cn.easygd.dynaguard.domain.exception.ResultTypeIllegalException;
import com.google.common.collect.Maps;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.Map;

/**
 * groovy规则引擎
 *
 * @author VD
 * @date 2025/7/28 20:49
 */
public class GroovyValidator extends BaseValidator {

    /**
     * 执行
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 是否符合规则
     */
    @Override
    public Boolean validate(String script, ValidationContext context) {
        // 参数传递
        Map<String, Object> params = Maps.newHashMap();
        context.buildExecuteContext().accept(params);
        Binding binding = new Binding();
        params.forEach(binding::setVariable);

        // 构建执行器
        GroovyShell shell = new GroovyShell(binding);
        Object evaluate = shell.evaluate(script);

        // 校验返回参数
        if (!(evaluate instanceof Boolean)) {
            throw new ResultTypeIllegalException();
        }
        return (Boolean) evaluate;
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
