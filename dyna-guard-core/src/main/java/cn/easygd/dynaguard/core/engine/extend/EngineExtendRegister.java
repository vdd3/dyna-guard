package cn.easygd.dynaguard.core.engine.extend;

import cn.easygd.dynaguard.core.engine.extend.function.EngineExtendFunction;

import java.util.List;

/**
 * 引擎扩展注册
 *
 * @author VD
 */
public interface EngineExtendRegister {

    /**
     * 获取扩展函数
     *
     * @return 扩展函数
     */
    List<EngineExtendFunction> extendFunctions();

    /**
     * 获取语言
     *
     * @return 语言
     */
    String getLanguage();
}
