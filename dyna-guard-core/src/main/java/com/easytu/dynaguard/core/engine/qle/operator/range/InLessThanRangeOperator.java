package com.easytu.dynaguard.core.engine.qle.operator.range;

import com.google.common.collect.Range;

/**
 * @author VD
 * @date 2025/8/11 21:38
 */
public class InLessThanRangeOperator extends BaseRangeOperator {
    /**
     * 执行方法
     *
     * @param list 参数列表
     * @return 执行结果
     * @throws Exception 抛出
     */
    @Override
    protected Boolean execute(Object[] list) throws Exception {
        checkParamsSize(list.length, 2);
        Comparable<?> up = convertToComparable(list[0]);
        Comparable<?> target = convertToComparable(list[1]);
        Range<Comparable<?>> range = Range.lessThan(up);
        return range.contains(target);
    }
}
