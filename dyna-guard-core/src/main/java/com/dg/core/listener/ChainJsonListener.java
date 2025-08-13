package com.dg.core.listener;

import com.dg.domain.config.LocalChainDataConfig;
import com.dg.domain.constants.ParserTypeEnumerable;
import com.dg.domain.enums.ParserTypeEnum;

/**
 * 流程json监听
 *
 * @author VD
 * @date 2025/8/13 19:26
 */
public class ChainJsonListener extends LocalChainListener {
    /**
     * 获取配置
     *
     * @return 配置
     */
    @Override
    protected LocalChainDataConfig getConfig() {
        return null;
    }

    /**
     * 类型
     *
     * @return 类型
     */
    @Override
    public ParserTypeEnumerable type() {
        return ParserTypeEnum.JSON;
    }
}
