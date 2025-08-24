package cn.easygd.dynaguard.core.path;

import java.util.List;

/**
 * 流程文件路径解析器
 *
 * @author VD
 * @version v 0.1 2025/8/9 16:31
 */
public interface ChainFilePathParser {

    /**
     * 解析流程文件路径
     *
     * @param pathList 路径列表
     * @return 绝对路径列表
     */
    List<String> parse(List<String> pathList);

    /**
     * 获取解析器名称
     *
     * @return 解析器名称
     */
    String getParserName();
}
