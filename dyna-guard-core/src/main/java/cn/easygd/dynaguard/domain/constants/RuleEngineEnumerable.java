package cn.easygd.dynaguard.domain.constants;

/**
 * 规则引擎枚举能力接口
 *
 * @author VD
 *
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
