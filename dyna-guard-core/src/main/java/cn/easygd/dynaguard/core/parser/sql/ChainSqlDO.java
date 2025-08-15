package cn.easygd.dynaguard.core.parser.sql;

import cn.easygd.dynaguard.domain.ValidationNode;

import java.util.Date;

/**
 * 流程sqlDO
 *
 * @author VD
 * @date 2025/8/5 21:35
 */
public class ChainSqlDO {

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 流程ID
     */
    private String chainId;

    /**
     * 删除状态
     */
    private Boolean deleted;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 快速失败
     */
    private Boolean fastFail;

    /**
     * 语言
     */
    private String language;

    /**
     * 脚本
     */
    private String script;

    /**
     * 熔断计数器过期时间，默认10秒
     */
    private Long guardExpire;

    /**
     * 熔断计数器阈值，默认100
     */
    private Long guardThreshold;

    /**
     * 转换为流程节点
     *
     * @return 节点
     */
    public ValidationNode converter2node() {
        ValidationNode node = new ValidationNode();
        node.setLanguage(this.getLanguage());
        node.setScript(this.getScript());
        node.setOrder(this.getOrder());
        node.setMessage(this.getMessage());
        node.setFastFail(this.getFastFail());
        return node;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
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

    public Boolean getFastFail() {
        return fastFail;
    }

    public void setFastFail(Boolean fastFail) {
        this.fastFail = fastFail;
    }

    public Long getGuardExpire() {
        return guardExpire;
    }

    public void setGuardExpire(Long guardExpire) {
        this.guardExpire = guardExpire;
    }

    public Long getGuardThreshold() {
        return guardThreshold;
    }

    public void setGuardThreshold(Long guardThreshold) {
        this.guardThreshold = guardThreshold;
    }
}
