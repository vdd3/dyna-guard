package cn.easygd.dynaguard.domain.enums;

/**
 * 熔断模式
 *
 * @author VD
 * @version v 0.1 2025/8/27 22:01
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
