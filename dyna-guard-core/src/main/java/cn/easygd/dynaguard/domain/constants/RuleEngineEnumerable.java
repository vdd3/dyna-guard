package cn.easygd.dynaguard.domain.constants;

/**
 * 规则引擎枚举能力接口
 *
 * @author VD
 * @version v 0.1 2025/7/28 20:53
 */
public interface RuleEngineEnumerable<E extends Enum<E>> {

    /**
     * 获取枚举
     *
     * @return 枚举
     */
    E getRuleEngineEnum();

    /**
     * 获取语言名称
     *
     * @return 语言
     */
    String getLanguageName();
}
