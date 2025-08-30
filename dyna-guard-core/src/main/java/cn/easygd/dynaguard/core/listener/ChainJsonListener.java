package cn.easygd.dynaguard.core.listener;

import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.domain.config.ChainJsonConfig;
import cn.easygd.dynaguard.domain.config.LocalChainDataConfig;
import cn.easygd.dynaguard.domain.constants.ParserTypeEnumerable;
import cn.easygd.dynaguard.domain.enums.ParserTypeEnum;

/**
 * 流程json监听
 *
 * @author VD
 */
public class ChainJsonListener extends LocalChainListener {
    /**
     * 获取配置
     *
     * @return 配置
     */
    @Override
    protected LocalChainDataConfig getConfig() {
        return (ChainJsonConfig) ChainConfigHolder.getDataConfig(type().getType());
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
