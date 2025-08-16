package cn.easygd.dynaguard.domain;

/**
 * 验证结果
 *
 * @author VD
 * @date 2025/7/27 22:16
 */
public class ValidationResult {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 是否异常
     */
    private Boolean exception = false;

    /**
     * 异常
     */
    private Throwable throwable;

    private ValidationResult(Boolean success, String message, Boolean exception) {
        this.success = success;
        this.message = message;
        this.exception = exception;
    }

    private ValidationResult(Boolean success, String message, Boolean exception, Throwable throwable) {
        this.success = success;
        this.message = message;
        this.exception = exception;
        this.throwable = throwable;
    }

    /**
     * 成功
     *
     * @return 验证结果
     */
    public static ValidationResult success() {
        return new ValidationResult(true, null, false);
    }

    /**
     * 失败
     *
     * @return 验证结果
     */
    public static ValidationResult fail() {
        return new ValidationResult(false, null, false);
    }

    /**
     * 失败
     *
     * @param message   错误信息
     * @param throwable 异常
     * @return 验证结果
     */
    public static ValidationResult fail(String message, Throwable throwable) {
        return new ValidationResult(false, message, true, throwable);
    }

    /**
     * 失败
     *
     * @param message 错误信息
     * @return 验证结果
     */
    public static ValidationResult fail(String message) {
        return new ValidationResult(false, message, false, null);
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getException() {
        return exception;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
