package cn.easygd.dynaguard.core.engine;

import cn.easygd.dynaguard.domain.ValidationResult;
import cn.easygd.dynaguard.domain.context.ValidationContext;

import java.util.Map;

/**
 * 规则引擎
 *
 * @author VD
 * @version v 0.1 2025/7/27 20:12
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
     * 构建参数
     *
     * @param context 上下文
     * @return 参数
     */
    Map<String, Object> buildParam(ValidationContext context);

    /**
     * 获取语言
     *
     * @return 语言
     */
    String getLanguage();
}
