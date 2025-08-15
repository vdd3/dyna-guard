package cn.easygd.dynaguard.core.engine.qle.operator.range;

import com.google.common.collect.Range;

/**
 * 在开区间内
 *
 * @author VD
 * @date 2025/8/11 20:07
 */
public class InOpenRangeOperator extends BaseRangeOperator {

    /**
     * 执行方法
     *
     * @param list 参数列表
     * @return 执行结果
     * @throws Exception 抛出
     */
    @Override
    protected Boolean execute(Object[] list) throws Exception {
        checkParamsSize(list.length, 3);
        Comparable<?> lower = convertToComparable(list[0]);
        Comparable<?> up = convertToComparable(list[1]);
        Comparable<?> target = convertToComparable(list[2]);
        Range<Comparable<?>> range = Range.open(lower, up);
        return range.contains(target);
    }
}
