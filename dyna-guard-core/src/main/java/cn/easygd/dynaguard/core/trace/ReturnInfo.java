package cn.easygd.dynaguard.core.trace;

/**
 * 返回详情
 *
 * @author VD
 * @version v 0.1 2025/8/21 20:58
 */
public class ReturnInfo {

    /**
     * 语言
     */
    private String language;

    /**
     * 触发条件
     */
    private String triggerCondition;

    /**
     * 重置
     */
    public void reset() {
        language = null;
        triggerCondition = null;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTriggerCondition() {
        return triggerCondition;
    }

    public void setTriggerCondition(String triggerCondition) {
        this.triggerCondition = triggerCondition;
    }
}
