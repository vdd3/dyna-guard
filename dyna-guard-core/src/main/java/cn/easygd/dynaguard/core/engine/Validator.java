package cn.easygd.dynaguard.core.engine;

import cn.easygd.dynaguard.domain.context.ValidationContext;

import java.util.Map;

/**
 * 规则引擎
 *
 * @author VD
 */
public interface Validator {

    /**
     * 执行
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 验证结果
     * @throws Exception 验证异常
     */
    Object execute(String script, ValidationContext context) throws Exception;

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
