package cn.easygd.dynaguard.core.parser.local;

import cn.easygd.dynaguard.core.chain.ValidationChain;
import cn.easygd.dynaguard.core.holder.ChainFilePathParserHolder;
import cn.easygd.dynaguard.core.parser.ValidationChainParser;
import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import cn.easygd.dynaguard.utils.FileUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地流程文件解析器
 *
 * @author VD
 * @version v 0.1 2025/8/9 13:17
 */
public abstract class LocalChainFileParser implements ValidationChainParser {

    /**
     * 解析
     *
     * @param config 配置
     * @return 验证链
     */
    @Override
    public List<ValidationChain> parse(ValidationChainConfig config) {
        // 获取当前需要过滤文件后缀
        String suffix = FileUtils.FILE_EXTENSION_SEPARATOR + type().getType();
        // 获取路径下所有的文件
        List<String> chainFilePath = config.getChainFilePath();
        // 获取文件绝对路径列表
        List<String> pathList = ChainFilePathParserHolder.getParser().parse(chainFilePath);
        // 文件转为string
        List<String> fileInfoList = pathList.stream()
                .filter(path -> path.endsWith(suffix))
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
