package cn.easygd.dynaguard.domain;

import cn.easygd.dynaguard.core.trace.ReturnInfo;

/**
 * 验证结果
 *
 * @author VD
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
     * 返回内容
     */
    private Object data;

    /**
     * 业务跟踪信息
     */
    private ReturnInfo returnInfo;

    /**
     * 节点名称，由脚本语言+顺序组成
     */
    private String nodeName;

    public ValidationResult(Boolean success, String message, Object data, ReturnInfo returnInfo, String nodeName) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.returnInfo = returnInfo;
        this.nodeName = nodeName;
    }

    /**
     * 成功
     *
     * @param data     数据
     * @param nodeName 节点名称
     * @return 验证结果
     */
    public static ValidationResult success(Object data, String nodeName) {
        return new ValidationResult(true, null, data, null, nodeName);
    }

    /**
     * 失败
     *
     * @param data    数据
     * @param message 错误信息
     * @return 验证结果
     */
    public static ValidationResult fail(Object data, String message) {
        return new ValidationResult(false, message, data, null, null);
    }

    /**
     * 失败
     *
     * @param data     数据
     * @param message  错误信息
     * @param nodeName 节点名称
     * @return 验证结果
     */
    public static ValidationResult fail(Object data, String message, String nodeName) {
        return new ValidationResult(false, message, data, null, nodeName);
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
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

    public Object getData() {
        return data;
    }
}
