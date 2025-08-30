package cn.easygd.dynaguard.core.listener;

import cn.easygd.dynaguard.domain.constants.ParserTypeEnumerable;

/**
 * 验证流程监听器
 *
 * @author VD
 */
public interface ValidationChainListener {

    /**
     * 注册
     */
    void register();

    /**
     * 类型
     *
     * @return 类型
     */
    ParserTypeEnumerable type();
}
