package cn.easygd.dynaguard.domain.enums;

import cn.easygd.dynaguard.domain.constants.ParserTypeEnumerable;

/**
 * 解析器类型枚举
 *
 * @author VD
 */
public enum ParserTypeEnum implements ParserTypeEnumerable {

    /**
     * json
     */
    JSON("json"),

    /**
     * xml
     */
    XML("xml"),

    /**
     * nacos
     */
    NACOS("nacos"),

    /**
     * sql
     */
    SQL("sql"),

    ;


    /**
     * 语言
     */
    private final String type;

    ParserTypeEnum(String type) {
        this.type = type;
    }

    /**
     * 获取解析器类型
     *
     * @return 解析器类型
     */
    @Override
    public String getType() {
        return type;
    }
}
