package cn.easygd.dynaguard.core.chain;

import cn.easygd.dynaguard.core.holder.GlobalBeanContextHolder;
import cn.easygd.dynaguard.domain.ValidationResult;
import cn.easygd.dynaguard.domain.context.ValidationContext;

import java.util.Objects;

/**
 * 验证链执行器辅助类
 *
 * @author VD
 */
public class ChainExecutorHelper {

    /**
     * 获取验证链
     *
     * @param group   分组
     * @param chainId 链ID
     * @return 验证链
     */
    public static ValidationChain getChain(String group, String chainId) {
        return GlobalBeanContextHolder.getContext().getChainManager().getChain(group, chainId);
    }

    /**
     * 获取验证链
     *
     * @param chainId 链ID
     * @return 验证链
     */
    public static ValidationChain getChain(String chainId) {
        return getChain(null, chainId);
    }

    /**
     * 验证
     *
     * @param group   分组
     * @param chainId 链ID
     * @param context 上下文
     */
    public static void validateHere(String group, String chainId, ValidationContext context) {
        ValidationChain chain = getChain(group, chainId);
        if (Objects.nonNull(chain)) {
            chain.execute(context);
        }
    }

    /**
     * 验证
     *
     * @param chainId 链ID
     * @param context 上下文
     */
    public static void validateHere(String chainId, ValidationContext context) {
        validateHere(null, chainId, context);
    }

    /**
     * 验证
     *
     * @param chainId 链ID
     * @param context 上下文
     * @return 验证结果
     */
    public static ValidationResult validateHereResult(String chainId, ValidationContext context) {
        return validateHereResult(null, chainId, context);
    }

    /**
     * 验证
     *
     * @param group   分组
     * @param chainId 链ID
     * @param context 上下文
     * @return 验证结果
     */
    public static ValidationResult validateHereResult(String group, String chainId, ValidationContext context) {
        ValidationChain chain = getChain(group, chainId);
        if (Objects.nonNull(chain)) {
            return chain.executeResult(context);
        }
        return ValidationResult.success(null, null);
    }
}
