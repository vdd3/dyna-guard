package cn.easygd.dynaguard.core.trace;

/**
 * 业务追踪
 *
 * @author VD
 * @date 2025/8/21 20:56
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
        RETURN_INFO_HOLDER.get().reset();
    }

    /**
     * 语言
     *
     * @param language 语言
     */
    public static void language(String language) {
        RETURN_INFO_HOLDER.get().setLanguage(language);
    }

    /**
     * 记录行号
     *
     * @param lineNumber 行号
     */
    public static void recordLineNumber(Integer lineNumber) {
        RETURN_INFO_HOLDER.get().setLineNumber(lineNumber);
    }

    /**
     * 记录触发条件
     *
     * @param condition 条件
     */
    public static void recordTriggerCondition(String condition) {
        RETURN_INFO_HOLDER.get().setTriggerCondition(condition);
    }

    /**
     * 记录变量
     *
     * @param varName 变量名
     * @param value   值
     */
    public static void recordVariable(String varName, Object value) {
        RETURN_INFO_HOLDER.get().getVariables().put(varName, value);
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
