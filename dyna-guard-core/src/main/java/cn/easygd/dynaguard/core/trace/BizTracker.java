package cn.easygd.dynaguard.core.trace;

import java.util.Optional;

/**
 * 业务追踪
 *
 * @author VD
 */
public class BizTracker {

    /**
     * 返回信息
     */
    private static final ThreadLocal<ReturnInfo> RETURN_INFO_HOLDER = new ThreadLocal<>();

    /**
     * 初始化
     */
    public static void init() {
        RETURN_INFO_HOLDER.set(new ReturnInfo());
    }

    /**
     * 重置
     */
    public static void reset() {
        Optional.ofNullable(get()).ifPresent(ReturnInfo::reset);
    }

    /**
     * 语言
     *
     * @param language 语言
     */
    public static void language(String language) {
        Optional.ofNullable(get()).ifPresent(source -> source.setLanguage(language));
    }

    /**
     * 记录触发条件
     *
     * @param condition 条件
     */
    public static void recordTriggerCondition(String condition) {
        Optional.ofNullable(get()).ifPresent(source -> source.setTriggerCondition(condition));
    }

    /**
     * 获取
     *
     * @return 返回信息
     */
    public static ReturnInfo get() {
        return RETURN_INFO_HOLDER.get();
    }

    /**
     * 清理
     */
    public static void clear() {
        RETURN_INFO_HOLDER.remove();
    }
}
