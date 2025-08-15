package com.easytu.dynaguard.domain.exception;

import com.easytu.dynaguard.domain.enums.ValidationErrorEnum;

/**
 * 验证失败异常
 *
 * @author VD
 * @date 2025/7/28 21:45
 */
public class ValidationFailedException extends RuntimeException {

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * 错误消息
     */
    private final String errorMessage;

    /**
     * 触发异常的规则表达式
     */
    private final String ruleExpression;

    /**
     * 构造函数
     *
     * @param errorEnum 错误枚举
     * @param cause     异常
     */
    public ValidationFailedException(ValidationErrorEnum errorEnum, Throwable cause) {
        this(errorEnum.getErrorCode(), errorEnum.getErrorMessage(), null, cause);
    }

    /**
     * 构造函数
     *
     * @param errorCode    错误码
     * @param errorMessage 错误消息
     */
    public ValidationFailedException(String errorCode, String errorMessage) {
        this(errorCode, errorMessage, null, null);
    }

    /**
     * 构造函数
     *
     * @param errorCode    错误码
     * @param errorMessage 错误消息
     * @param cause        异常
     */
    public ValidationFailedException(String errorCode, String errorMessage, Throwable cause) {
        this(errorCode, errorMessage, null, cause);
    }

    /**
     * 构造函数
     *
     * @param errorCode      错误码
     * @param errorMessage   错误消息
     * @param ruleExpression 触发异常的规则表达式
     */
    public ValidationFailedException(String errorCode,
                                     String errorMessage,
                                     String ruleExpression) {
        this(errorCode, errorMessage, ruleExpression, null);
    }

    /**
     * 构造函数
     *
     * @param errorCode      错误码
     * @param errorMessage   错误消息
     * @param ruleExpression 触发异常的规则表达式
     * @param cause          异常
     */
    public ValidationFailedException(String errorCode,
                                     String errorMessage,
                                     String ruleExpression,
                                     Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.ruleExpression = ruleExpression;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 获取错误消息
     *
     * @return 错误消息
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 获取触发异常的规则表达式
     *
     * @return 触发异常的规则表达式
     */
    public String getRuleExpression() {
        return ruleExpression;
    }
}
