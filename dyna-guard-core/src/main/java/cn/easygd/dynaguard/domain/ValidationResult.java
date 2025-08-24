package cn.easygd.dynaguard.domain;

import cn.easygd.dynaguard.core.trace.ReturnInfo;

/**
 * 验证结果
 *
 * @author VD
 * @version v 0.1 2025/7/27 22:16
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

    /**
     * 业务跟踪信息
     */
    private ReturnInfo returnInfo;

    /**
     * 节点名称，由脚本语言+顺序组成
     */
    private String nodeName;

    private ValidationResult(Boolean success, String message, Boolean exception) {
        this(success, message, exception, null);
    }

    private ValidationResult(Boolean success, String message, Boolean exception, Throwable throwable) {
        this(success, message, exception, throwable, null);
    }

    private ValidationResult(Boolean success, String message, Boolean exception, Throwable throwable, ReturnInfo returnInfo) {
        this(success, message, exception, throwable, returnInfo, null);
    }

    private ValidationResult(Boolean success, String message, Boolean exception, Throwable throwable, ReturnInfo returnInfo, String nodeName) {
        this.success = success;
        this.message = message;
        this.exception = exception;
        this.throwable = throwable;
        this.returnInfo = returnInfo;
        this.nodeName = nodeName;
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

    /**
     * 失败
     *
     * @param message 错误信息
     * @return 验证结果
     */
    public static ValidationResult fail(String message, String nodeName) {
        return new ValidationResult(false, message, false, null, null, nodeName);
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

    public ReturnInfo getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(ReturnInfo returnInfo) {
        this.returnInfo = returnInfo;
    }

    public String getNodeName() {
        return nodeName;
    }
}
