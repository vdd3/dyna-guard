package cn.easygd.dynaguard.domain.enums;

/**
 * 框架类型枚举
 *
 * @author VD
 * @date 2025/8/13 20:09
 */
public enum FreeMarkTypeEnum {

    /**
     * 本地
     */
    LOCAL("local"),

    /**
     * Spring
     */
    SPRING("spring"),

    ;

    /**
     * 语言
     */
    private final String type;

    FreeMarkTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
