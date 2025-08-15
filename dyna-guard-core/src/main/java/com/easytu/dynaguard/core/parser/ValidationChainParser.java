package com.easytu.dynaguard.core.parser;

import com.easytu.dynaguard.core.chain.ValidationChain;
import com.easytu.dynaguard.domain.config.ValidationChainConfig;
import com.easytu.dynaguard.domain.constants.ParserTypeEnumerable;

import java.util.List;

/**
 * 验证流程解析器
 *
 * @author VD
 * @date 2025/8/1 19:48
 */
public interface ValidationChainParser {

    /**
     * 初始化
     *
     * @param config 配置
     */
    void init(ValidationChainConfig config);

    /**
     * 解析
     *
     * @param config 配置
     * @return 验证链
     */
    List<ValidationChain> parse(ValidationChainConfig config);

    /**
     * 解析
     *
     * @param info 内容
     * @return 验证链
     */
    List<ValidationChain> parse(String info);

    /**
     * 类型
     *
     * @return 类型
     */
    ParserTypeEnumerable type();
}
