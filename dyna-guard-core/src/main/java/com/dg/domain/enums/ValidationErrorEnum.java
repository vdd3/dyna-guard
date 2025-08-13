package com.dg.domain.enums;

/**
 * 验证错误枚举
 *
 * @author VD
 * @date 2025/7/30 22:01
 */
public enum ValidationErrorEnum {

    /**
     * 脚本执行错误
     */
    SCRIPT_EXECUTE_ERROR("10001", "脚本执行错误", "验证执行"),

    /**
     * 验证失败
     */
    FAIL("10002", "验证失败", "验证执行"),

    ;

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * 错误场景
     */
    private final String errorMessage;

    /**
     * 错误场景
     */
    private final String scene;

    ValidationErrorEnum(String errorCode, String errorMessage, String scene) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.scene = scene;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getScene() {
        return scene;
    }
}
