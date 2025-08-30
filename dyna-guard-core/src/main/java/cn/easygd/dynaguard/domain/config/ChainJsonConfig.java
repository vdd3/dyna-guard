package cn.easygd.dynaguard.domain.config;

/**
 * 流程json配置
 *
 * @author VD
 */
public class ChainJsonConfig extends LocalChainDataConfig {

    /**
     * 链路ID字段
     */
    private String chainIdField = "chainId";

    /**
     * 节点字段
     */
    private String nodeField = "node";

    /**
     * 语言字段
     */
    private String languageField = "language";

    /**
     * 顺序字段
     */
    private String orderField = "order";

    /**
     * 消息字段
     */
    private String messageField = "message";

    /**
     * 快速失败字段
     */
    private String fastFailField = "fastFail";

    /**
     * 脚本字段
     */
    private String scriptField = "script";

    public String getChainIdField() {
        return chainIdField;
    }

    public void setChainIdField(String chainIdField) {
        this.chainIdField = chainIdField;
    }

    public String getNodeField() {
        return nodeField;
    }

    public void setNodeField(String nodeField) {
        this.nodeField = nodeField;
    }

    public String getLanguageField() {
        return languageField;
    }

    public void setLanguageField(String languageField) {
        this.languageField = languageField;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getMessageField() {
        return messageField;
    }

    public void setMessageField(String messageField) {
        this.messageField = messageField;
    }

    public String getFastFailField() {
        return fastFailField;
    }

    public void setFastFailField(String fastFailField) {
        this.fastFailField = fastFailField;
    }

    public String getScriptField() {
        return scriptField;
    }

    public void setScriptField(String scriptField) {
        this.scriptField = scriptField;
    }
}
