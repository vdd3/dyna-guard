package cn.easygd.dynaguard.domain.enums;

/**
 * 熔断模式
 *
 * @author VD
 */
public enum GuardMode {

    /**
     * 计数模式
     */
    COUNTER,

    /**
     * 拦截率模式
     */
    RATE
}
