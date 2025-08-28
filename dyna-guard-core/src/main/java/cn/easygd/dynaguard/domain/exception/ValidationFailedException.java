package cn.easygd.dynaguard.domain.exception;

import cn.easygd.dynaguard.domain.enums.ValidationErrorEnum;

/**
 * 验证失败异常
 *
 * @author VD
 * @version v 0.1 2025/7/28 21:45
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
     * 构造函数
     *
     * @param errorEnum 错误枚举
     * @param cause     异常
     */
    public ValidationFailedException(ValidationErrorEnum errorEnum, Throwable cause) {
        this(errorEnum.getErrorCode(), errorEnum.getErrorMessage(), cause);
    }

    /**
     * 构造函数
     *
     * @param errorEnum 错误枚举
     */
    public ValidationFailedException(ValidationErrorEnum errorEnum) {
        this(errorEnum.getErrorCode(), errorEnum.getErrorMessage());
    }

    /**
     * 构造函数
     *
     * @param errorCode    错误码
     * @param errorMessage 错误消息
     */
    public ValidationFailedException(String errorCode,
                                     String errorMessage) {
        this(errorCode, errorMessage, null);
    }

    /**
     * 构造函数
     *
     * @param errorCode    错误码
     * @param errorMessage 错误消息
     * @param cause        异常
     */
    public ValidationFailedException(String errorCode,
                                     String errorMessage,
                                     Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
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
}
