package com.easytu.dynaguard.core.engine;

import com.easytu.dynaguard.domain.ValidationResult;
import com.easytu.dynaguard.domain.context.ValidationContext;

/**
 * 规则引擎
 *
 * @author VD
 * @date 2025/7/27 20:12
 */
public interface Validator {

    /**
     * 执行
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 验证结果
     */
    ValidationResult execute(String script, ValidationContext context);

    /**
     * 获取语言
     *
     * @return 语言
     */
    String getLanguage();
}
