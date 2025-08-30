package cn.easygd.dynaguard.core.guard;

import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.guard.GuardThreshold;

import java.util.List;

/**
 * 验证熔断
 *
 * @author VD
 */
public interface ValidationGuard {

    /**
     * 链路ID，如果返回为空则代表全局的熔断器
     *
     * @return 链路ID
     */
    List<String> chainId();

    /**
     * 是否超过阈值
     *
     * @param chainId        流程id
     * @param guardThreshold 熔断阈值
     * @return true 超过阈值
     */
    Boolean isExceedThreshold(String chainId, GuardThreshold guardThreshold);

    /**
     * 降级操作
     *
     * @param chainId 链路ID
     * @param context 上下文
     */
    void fallBack(String chainId, ValidationContext context);
}
