package com.dg.core.listener;

import com.dg.core.holder.ChainConfigHolder;
import com.dg.domain.config.ChainXmlConfig;
import com.dg.domain.config.LocalChainDataConfig;
import com.dg.domain.constants.ParserTypeEnumerable;
import com.dg.domain.enums.ParserTypeEnum;

/**
 * xml流程监听器
 *
 * @author VD
 * @date 2025/8/10 14:51
 */
public class ChainXmlListener extends LocalChainListener {

    /**
     * 类型
     *
     * @return 类型
     */
    @Override
    public ParserTypeEnumerable type() {
        return ParserTypeEnum.XML;
    }

    /**
     * 获取配置
     *
     * @return 配置
     */
    @Override
    protected LocalChainDataConfig getConfig() {
        return (ChainXmlConfig) ChainConfigHolder.getDataConfig(type().getType());
    }
}
