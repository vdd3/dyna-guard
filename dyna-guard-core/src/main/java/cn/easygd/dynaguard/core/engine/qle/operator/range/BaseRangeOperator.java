package cn.easygd.dynaguard.core.engine.qle.operator.range;

import cn.easygd.dynaguard.core.engine.qle.operator.BaseOperator;

/**
 * 范围基础运算符
 *
 * @author VD
 * @date 2025/8/10 22:16
 */
public abstract class BaseRangeOperator extends BaseOperator {

    /**
     * 将参数转换为可比较类型（Comparable），支持常见数字类型
     */
    protected Comparable<?> convertToComparable(Object param) {
        if (param instanceof Comparable) {
            return (Comparable<?>) param;
        }
        throw new IllegalArgumentException("参数类型不支持比较：" + param.getClass().getSimpleName());
    }
}
