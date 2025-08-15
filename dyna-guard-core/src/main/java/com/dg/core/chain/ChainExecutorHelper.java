package com.dg.core.chain;

import com.dg.core.holder.GlobalBeanContextHolder;
import com.dg.domain.context.ValidationContext;

import java.util.Objects;

/**
 * 验证链执行器辅助类
 *
 * @author VD
 * @date 2025/8/3 12:31
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
        return GlobalBeanContextHolder.getContext().getChainManager().getChain(chainId);
    }

    /**
     * 验证
     *
     * @param group   分组
     * @param chainId 链ID
     * @param context 上下文
     */
    public static void validateHere(String group, String chainId, ValidationContext context) {
        ValidationChain chain = GlobalBeanContextHolder.getContext().getChainManager().getChain(group, chainId);
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
        ValidationChain chain = GlobalBeanContextHolder.getContext().getChainManager().getChain(chainId);
        if (Objects.nonNull(chain)) {
            chain.execute(context);
        }
    }
}
