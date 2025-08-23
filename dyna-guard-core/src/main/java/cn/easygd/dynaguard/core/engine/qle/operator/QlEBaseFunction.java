package cn.easygd.dynaguard.core.engine.qle.operator;

import cn.easygd.dynaguard.domain.exception.ValidationChainEngineException;
import com.alibaba.qlexpress4.api.QLFunctionalVarargs;

/**
 * qle基础函数
 *
 * @author VD
 * @date 2025/8/11 19:56
 */
public abstract class QlEBaseFunction implements QLFunctionalVarargs {

    /**
     * 执行方法
     *
     * @param objects 参数对象
     * @return 执行结果
     */
    @Override
    public Object call(Object... objects) {
        try {
            return execute(objects);
        } catch (Exception e) {
            throw new ValidationChainEngineException("qle execute exception : " + e.getMessage(), e);
        }
    }

    /**
     * 检查参数个数
     *
     * @param size         参数个数
     * @param expectedSize 期望参数个数
     */
    protected static void checkParamsSize(Integer size, Integer expectedSize) {
        if (size < expectedSize) {
            throw new IllegalArgumentException("参数个数错误，期望" + expectedSize + "个，实际" + size + "个");
        }
    }

    /**
     * 执行方法
     *
     * @param list 参数列表
     * @return 执行结果
     * @throws Exception 抛出
     */
    protected abstract Boolean execute(Object[] list) throws Exception;
}
