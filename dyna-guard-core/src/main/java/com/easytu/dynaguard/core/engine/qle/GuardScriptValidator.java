package com.easytu.dynaguard.core.engine.qle;

import com.easytu.dynaguard.core.engine.BaseValidator;
import com.easytu.dynaguard.domain.context.ValidationContext;
import com.easytu.dynaguard.domain.enums.RuleEngineEnum;

/**
 * 熔断脚本验证器
 *
 * @author VD
 * @date 2025/7/30 22:14
 */
public class GuardScriptValidator extends BaseValidator {

    /**
     * 熔断脚本执行器
     */
    private static final GuardScriptExecutor GUARD_SCRIPT_EXECUTOR = GuardScriptExecutor.init();

    /**
     * 验证
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 是否成功
     */
    @Override
    protected Boolean validate(String script, ValidationContext context) {
        return GUARD_SCRIPT_EXECUTOR.execute(script, context);
    }

    /**
     * 获取语言
     *
     * @return 语言
     */
    @Override
    public String getLanguage() {
        return RuleEngineEnum.GUARD_SCRIPT.getLanguageName();
    }
}
