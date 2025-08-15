package cn.easygd.dynaguard.domain.config;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 流程sql配置
 *
 * @author VD
 * @date 2025/8/5 21:04
 */
public class ChainSqlConfig extends ChainDataConfig {

    /**
     * 连接地址
     */
    private String url;

    /**
     * 驱动
     */
    private String driverClassName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 核心线程数
     */
    private Long corePoolSize = 1L;

    /**
     * 首次刷新监听器间隔时间（默认2分钟）
     */
    private Long firstListenerInterval = 2L * 60L;

    /**
     * 刷新监听器间隔时间（默认5分钟）
     */
    private Long listenerInterval = 5L * 60L;

    /**
     * 表名
     */
    private String tableName = "validation_chain";

    /**
     * 创建时间字段
     */
    private String createTimeField = "create_time";

    /**
     * 更新时间字段
     */
    private String updateTimeField = "update_time";

    /**
     * 流程ID字段
     */
    private String chainIdField = "chain_id";

    /**
     * 删除状态字段
     */
    private String deletedField = "deleted";

    /**
     * 排序字段
     */
    private String orderField = "order";

    /**
     * 提示信息字段
     */
    private String messageField = "message";

    /**
     * 快速失败字段
     */
    private String fastFailField = "fast_fail";

    /**
     * 语言字段
     */
    private String languageField = "language";

    /**
     * 脚本字段
     */
    private String scriptField = "script";

    /**
     * 熔断计数器过期时间
     */
    private String guardExpireField = "guard_expire";

    /**
     * 熔断计数器阈值，默认100
     */
    private String guardThresholdField = "guard_threshold";

    /**
     * 字段转换器
     *
     * @return 字段转换器
     */
    public static Consumer<Map<String, Function<String, Object>>> fieldConsumer() {
        return map -> {
            map.put("corePoolSize", source -> {
                if (StringUtils.isNotBlank(source)) {
                    return Long.parseLong(source);
                }
                return 1L;
            });
            map.put("listenerInterval", source -> {
                if (StringUtils.isNotBlank(source)) {
                    return Long.parseLong(source);
                }
                return 5L * 60L;
            });
            map.put("firstListenerInterval", source -> {
                if (StringUtils.isNotBlank(source)) {
                    return Long.parseLong(source);
                }
                return 2L * 60L;
            });
        };
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCreateTimeField() {
        return createTimeField;
    }

    public void setCreateTimeField(String createTimeField) {
        this.createTimeField = createTimeField;
    }

    public String getUpdateTimeField() {
        return updateTimeField;
    }

    public void setUpdateTimeField(String updateTimeField) {
        this.updateTimeField = updateTimeField;
    }

    public String getChainIdField() {
        return chainIdField;
    }

    public void setChainIdField(String chainIdField) {
        this.chainIdField = chainIdField;
    }

    public String getDeletedField() {
        return deletedField;
    }

    public void setDeletedField(String deletedField) {
        this.deletedField = deletedField;
    }

    public String getLanguageField() {
        return languageField;
    }

    public void setLanguageField(String languageField) {
        this.languageField = languageField;
    }

    public String getScriptField() {
        return scriptField;
    }

    public void setScriptField(String scriptField) {
        this.scriptField = scriptField;
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

    public Long getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Long corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Long getFirstListenerInterval() {
        return firstListenerInterval;
    }

    public void setFirstListenerInterval(Long firstListenerInterval) {
        this.firstListenerInterval = firstListenerInterval;
    }

    public Long getListenerInterval() {
        return listenerInterval;
    }

    public void setListenerInterval(Long listenerInterval) {
        this.listenerInterval = listenerInterval;
    }

    public String getGuardExpireField() {
        return guardExpireField;
    }

    public void setGuardExpireField(String guardExpireField) {
        this.guardExpireField = guardExpireField;
    }

    public String getGuardThresholdField() {
        return guardThresholdField;
    }

    public void setGuardThresholdField(String guardThresholdField) {
        this.guardThresholdField = guardThresholdField;
    }
}
