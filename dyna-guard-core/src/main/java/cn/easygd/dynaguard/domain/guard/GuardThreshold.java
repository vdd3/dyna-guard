package cn.easygd.dynaguard.domain.guard;

/**
 * 熔断阈值
 *
 * @author VD
 */
public interface GuardThreshold {

    /**
     * 是否失败
     *
     * @return true:失败
     */
    Boolean isFail();
}
