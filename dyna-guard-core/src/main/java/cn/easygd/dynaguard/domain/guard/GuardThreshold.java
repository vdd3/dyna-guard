package cn.easygd.dynaguard.domain.guard;

/**
 * 熔断阈值
 *
 * @author VD
 * @version v 0.1 2025/8/27 22:41
 */
public interface GuardThreshold {

    /**
     * 是否失败
     *
     * @return true:失败
     */
    Boolean isFail();
}
