package cn.easygd.dynaguard.core.engine.qle.operator;

import cn.easygd.dynaguard.domain.exception.ValidationChainEngineException;
import com.ql.util.express.Operator;

/**
 * 基础操作符
 *
 * @author VD
 * @date 2025/8/11 19:56
 */
public abstract class BaseOperator extends Operator {

    /**
     * 执行方法
     *
     * @param list 参数列表
     * @return 执行结果
     * @throws Exception 抛出
     */
    @Override
    public Object executeInner(Object[] list) throws Exception {
        try {
            return execute(list);
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
    protected void checkParamsSize(Integer size, Integer expectedSize) {
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
