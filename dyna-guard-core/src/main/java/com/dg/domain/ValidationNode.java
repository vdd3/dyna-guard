package com.dg.domain;

import com.dg.core.engine.Validator;

/**
 * 验证节点
 *
 * @author VD
 * @date 2025/7/29 21:50
 */
public class ValidationNode {

    /**
     * 执行脚本
     */
    private String script;

    /**
     * 语言
     */
    private String language;

    /**
     * 顺序
     */
    private Integer order = 10;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 验证器
     */
    private Validator validator;

    /**
     * 快速失败
     */
    private Boolean fastFail = true;


    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public Boolean getFastFail() {
        return fastFail;
    }

    public void setFastFail(Boolean fastFail) {
        this.fastFail = fastFail;
    }
}
