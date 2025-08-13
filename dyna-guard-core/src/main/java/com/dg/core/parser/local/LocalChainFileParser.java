package com.dg.core.parser.local;

import com.dg.core.chain.ValidationChain;
import com.dg.core.holder.ChainFilePathParserHolder;
import com.dg.core.parser.ValidationChainParser;
import com.dg.domain.config.ValidationChainConfig;
import com.dg.utils.FileUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地流程文件解析器
 *
 * @author VD
 * @date 2025/8/9 13:17
 */
public abstract class LocalChainFileParser implements ValidationChainParser {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(LocalChainFileParser.class);

    /**
     * 解析
     *
     * @param config 配置
     * @return 验证链
     */
    @Override
    public List<ValidationChain> parse(ValidationChainConfig config) {
        // 获取路径下所有的文件
        List<String> chainFilePath = config.getChainFilePath();
        // 获取文件绝对路径列表
        List<String> pathList = ChainFilePathParserHolder.getParser().parse(chainFilePath);
        // 文件转为string
        List<String> fileInfoList = pathList.stream()
                .map(FileUtils::getFileInfo)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        return parseFile(fileInfoList);
    }

    /**
     * 解析
     *
     * @param info 内容
     * @return 验证链
     */
    @Override
    public List<ValidationChain> parse(String info) {
        return parseFile(Lists.newArrayList(info));
    }

    /**
     * 解析文件
     *
     * @param fileInfoList 文件信息
     * @return 验证链
     */
    protected abstract List<ValidationChain> parseFile(List<String> fileInfoList);
}
