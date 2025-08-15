package cn.easygd.dynaguard.core.holder;

import cn.easygd.dynaguard.core.path.ChainFilePathParser;

/**
 * 流程文件路径解析器持有器
 *
 * @author VD
 * @date 2025/8/9 23:59
 */
public class ChainFilePathParserHolder {
    /**
     * 持有器
     */
    private volatile static ChainFilePathParserHolder holder;

    /**
     * 配置
     */
    private ChainFilePathParser parser;

    /**
     * 初始化
     *
     * @param parser 配置
     */
    public static void init(ChainFilePathParser parser) {
        if (holder == null) {
            synchronized (ChainFilePathParserHolder.class) {
                if (holder == null) {
                    holder = new ChainFilePathParserHolder();
                }
            }
        }
        holder.parser = parser;
    }

    /**
     * 获取
     *
     * @return ChainFilePathParser
     */
    public static ChainFilePathParser getParser() {
        return holder.parser;
    }

    private ChainFilePathParserHolder() {

    }
}
