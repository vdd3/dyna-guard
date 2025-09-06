package cn.easygd.dynaguard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * spring boot 配置
 *
 * @author VD
 */
@ConfigurationProperties(prefix = "validation")
public class ValidationProperty {

    /**
     * 解析器列表，默认使用xml，json，sql解析器
     */
    private List<String> parser;

    /**
     * 路径解析器名称，默认使用spring
     */
    private String pathParserName;

    /**
     * 验证方法列表，如果希望自动拦截验证方法，请将将要增强的类名添加到此列表中，支持通配符
     */
    private List<String> validationMethod;

    /**
     * 验证链文件路径
     */
    private String chainFilePath;

    /**
     * 是否开启安全策略
     */
    private Boolean enableSecurityStrategy;

    /**
     * 是否开启业务追踪
     */
    private Boolean enableBizTrace;

    /**
     * sql验证链数据map
     */
    private Map<String, String> sqlChainDataMap;

    /**
     * xml验证链数据map
     */
    private Map<String, String> xmlChainDataMap;

    /**
     * json验证链数据map
     */
    private Map<String, String> jsonChainDataMap;

    public List<String> getParser() {
        return parser;
    }

    public void setParser(List<String> parser) {
        this.parser = parser;
    }

    public String getPathParserName() {
        return pathParserName;
    }

    public void setPathParserName(String pathParserName) {
        this.pathParserName = pathParserName;
    }

    public String getChainFilePath() {
        return chainFilePath;
    }

    public void setChainFilePath(String chainFilePath) {
        this.chainFilePath = chainFilePath;
    }

    public Map<String, String> getSqlChainDataMap() {
        return sqlChainDataMap;
    }

    public void setSqlChainDataMap(Map<String, String> sqlChainDataMap) {
        this.sqlChainDataMap = sqlChainDataMap;
    }

    public Map<String, String> getXmlChainDataMap() {
        return xmlChainDataMap;
    }

    public void setXmlChainDataMap(Map<String, String> xmlChainDataMap) {
        this.xmlChainDataMap = xmlChainDataMap;
    }

    public Map<String, String> getJsonChainDataMap() {
        return jsonChainDataMap;
    }

    public void setJsonChainDataMap(Map<String, String> jsonChainDataMap) {
        this.jsonChainDataMap = jsonChainDataMap;
    }

    public Boolean getEnableSecurityStrategy() {
        return enableSecurityStrategy;
    }

    public void setEnableSecurityStrategy(Boolean enableSecurityStrategy) {
        this.enableSecurityStrategy = enableSecurityStrategy;
    }

    public Boolean getEnableBizTrace() {
        return enableBizTrace;
    }

    public void setEnableBizTrace(Boolean enableBizTrace) {
        this.enableBizTrace = enableBizTrace;
    }

    public List<String> getValidationMethod() {
        return validationMethod;
    }

    public void setValidationMethod(List<String> validationMethod) {
        this.validationMethod = validationMethod;
    }
}
