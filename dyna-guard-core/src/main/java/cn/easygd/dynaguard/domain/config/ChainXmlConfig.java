package cn.easygd.dynaguard.domain.config;

/**
 * 流程xml配置
 *
 * @author VD
 */
public class ChainXmlConfig extends LocalChainDataConfig {

    /**
     * 链路字段
     */
    private String chainField = "chain";

    /**
     * 链路ID字段
     */
    private String chainIdField = "id";

    /**
     * 节点名称字段
     */
    private String nodeNameField = "name";

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

    public String getChainField() {
        return chainField;
    }

    public void setChainField(String chainField) {
        this.chainField = chainField;
    }

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

    public String getNodeNameField() {
        return nodeNameField;
    }

    public void setNodeNameField(String nodeNameField) {
        this.nodeNameField = nodeNameField;
    }
}
