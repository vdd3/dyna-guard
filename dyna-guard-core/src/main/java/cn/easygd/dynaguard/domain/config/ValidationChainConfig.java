package cn.easygd.dynaguard.domain.config;

import cn.easygd.dynaguard.domain.enums.GuardMode;
import cn.easygd.dynaguard.domain.enums.ParserTypeEnum;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * 验证链配置
 *
 * @author VD
 */
public class ValidationChainConfig {

    /**
     * 解析器列表，默认使用xml，json，sql解析器
     */
    private List<String> parserList = Lists.newArrayList(ParserTypeEnum.SQL.getType(), ParserTypeEnum.XML.getType(), ParserTypeEnum.JSON.getType());

    /**
     * 路径解析器名称，默认使用local
     */
    private String pathParserName = "local";

    /**
     * 验证方法列表，如果希望自动拦截验证方法，请将将要增强的类名添加到此列表中，支持通配符
     */
    private List<String> validationMethodList = Lists.newArrayList();

    /**
     * 验证链文件路径
     */
    private List<String> chainFilePath = Lists.newArrayList("classpath:chain/*Chain.xml", "classpath:chain/*Chain.json");

    /**
     * 是否开启安全策略
     */
    private Boolean enableSecurityStrategy = false;

    /**
     * 是否开启业务追踪
     */
    private Boolean enableBizTrace = false;

    /**
     * 是否开启熔断
     */
    private Boolean enableGuard = false;

    /**
     * 熔断模式
     */
    private GuardMode guardMode = GuardMode.COUNTER;

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

    public List<String> getParserList() {
        return parserList;
    }

    public void setParserList(List<String> parserList) {
        this.parserList = parserList;
    }

    public List<String> getValidationMethodList() {
        return validationMethodList;
    }

    public void setValidationMethodList(List<String> validationMethodList) {
        this.validationMethodList = validationMethodList;
    }

    public List<String> getChainFilePath() {
        return chainFilePath;
    }

    public void setChainFilePath(List<String> chainFilePath) {
        this.chainFilePath = chainFilePath;
    }

    public Map<String, String> getSqlChainDataMap() {
        return sqlChainDataMap;
    }

    public void setSqlChainDataMap(Map<String, String> sqlChainDataMap) {
        this.sqlChainDataMap = sqlChainDataMap;
    }

    public String getPathParserName() {
        return pathParserName;
    }

    public void setPathParserName(String pathParserName) {
        this.pathParserName = pathParserName;
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

    public Boolean getEnableGuard() {
        return enableGuard;
    }

    public void setEnableGuard(Boolean enableGuard) {
        this.enableGuard = enableGuard;
    }

    public GuardMode getGuardMode() {
        return guardMode;
    }

    public void setGuardMode(GuardMode guardMode) {
        this.guardMode = guardMode;
    }
}
