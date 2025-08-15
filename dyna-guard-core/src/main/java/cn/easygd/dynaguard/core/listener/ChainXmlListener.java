package cn.easygd.dynaguard.core.listener;

import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.domain.config.ChainXmlConfig;
import cn.easygd.dynaguard.domain.config.LocalChainDataConfig;
import cn.easygd.dynaguard.domain.constants.ParserTypeEnumerable;
import cn.easygd.dynaguard.domain.enums.ParserTypeEnum;

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
