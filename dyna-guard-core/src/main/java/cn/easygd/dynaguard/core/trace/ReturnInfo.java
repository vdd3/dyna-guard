package cn.easygd.dynaguard.core.trace;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 返回详情
 *
 * @author VD
 * @date 2025/8/21 20:58
 */
public class ReturnInfo {

    /**
     * 语言
     */
    private String language;

    /**
     * 行号
     */
    private Integer lineNumber = -1;

    /**
     * 触发条件
     */
    private String triggerCondition;

    /**
     * 变量
     */
    private Map<String, Object> variables = Maps.newConcurrentMap();

    /**
     * 重置
     */
    public void reset() {
        language = null;
        lineNumber = -1;
        triggerCondition = null;
        variables.clear();
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getTriggerCondition() {
        return triggerCondition;
    }

    public void setTriggerCondition(String triggerCondition) {
        this.triggerCondition = triggerCondition;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "ReturnInfo{" +
                "language='" + language + '\'' +
                ", lineNumber=" + lineNumber +
                ", triggerCondition='" + triggerCondition + '\'' +
                ", variables=" + variables +
                '}';
    }
}
