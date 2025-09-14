package cn.easygd.dynaguard.core.engine.extend.function;

/**
 * 引擎扩展函数
 *
 * @author VD
 */
public interface EngineExtendFunction {

    /**
     * 函数执行
     *
     * @param objects 参数
     * @return 函数执行结果
     */
    Object call(Object... objects);

    /**
     * 函数名称
     *
     * @return 函数名称
     */
    String getName();
}
