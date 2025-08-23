package cn.easygd.dynaguard.domain.enums;

import cn.easygd.dynaguard.domain.constants.RuleEngineEnumerable;

/**
 * 规则引擎枚举
 *
 * @author VD
 * @date 2025/7/28 21:01
 */
public enum RuleEngineEnum implements RuleEngineEnumerable<RuleEngineEnum> {

    /**
     * Groovy
     */
    GROOVY("Groovy"),

    /**
     * JavaScript
     */
    JAVA_SCRIPT("JavaScript"),

    /**
     * SpEl
     */
    SPEl("SpEl"),

    /**
     * QLExpress4
     */
    QLEXPRESS4("QLExpress4"),

    /**
     * Aviator
     */
    AVIATOR("Aviator"),

    ;

    /**
     * 语言
     */
    private final String language;

    RuleEngineEnum(String language) {
        this.language = language;
    }

    /**
     * 获取枚举
     *
     * @return 枚举
     */
    @Override
    public RuleEngineEnum getRuleEngineEnum() {
        return this;
    }

    /**
     * 获取语言名称
     *
     * @return 语言
     */
    @Override
    public String getLanguageName() {
        return language;
    }
}
