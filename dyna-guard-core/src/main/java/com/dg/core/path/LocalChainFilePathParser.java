package com.dg.core.path;

import com.dg.domain.enums.FreeMarkTypeEnum;
import com.dg.utils.FileUtils;
import com.google.common.collect.Lists;

import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * 本地文件路径解析器
 *
 * @author VD
 * @date 2025/8/9 16:46
 */
public class LocalChainFilePathParser implements ChainFilePathParser {

    /**
     * 解析流程文件路径
     *
     * @param pathList 路径列表
     * @return 绝对路径列表
     */
    @Override
    public List<String> parse(List<String> pathList) {
        List<String> result = Lists.newArrayList();
        for (String path : pathList) {
            // 获取当前文件匹配规则
            String wildcardMatch = FileUtils.getWildcardMatch(path);
            if (FileUtils.isAbsolutePath(path)) {
                result.addAll(FileUtils.getFileAbsolutePath(path, wildcardMatch));
            } else {
                // 如果不是绝对路径，则判断是否为classPath路径，如果是则按照当前线程的类加载器获取
                String dirPath = path.replace(FileUtils.CLASS_PATH_PREFIX, "");
                if (dirPath.contains(FileUtils.WILD_CARD)) {
                    dirPath = FileUtils.getDirPath(dirPath);
                }
                String dir = Optional.ofNullable(Thread.currentThread().getContextClassLoader().getResource(dirPath))
                        .map(URL::getPath)
                        .orElse(null);
                result.addAll(FileUtils.getFileAbsolutePath(dir, wildcardMatch));
            }
        }
        return result;
    }

    /**
     * 获取解析器名称
     *
     * @return 解析器名称
     */
    @Override
    public String getParserName() {
        return FreeMarkTypeEnum.LOCAL.getType();
    }
}
