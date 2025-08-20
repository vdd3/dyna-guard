package cn.easygd.dynaguard.core.engine.qle.operator.range;

import cn.easygd.dynaguard.core.engine.qle.operator.BaseOperator;
import cn.easygd.dynaguard.utils.CustomCheckUtils;

/**
 * 最小值判断
 *
 * @author VD
 * @date 2025/8/11 21:30
 */
public class InAtLeastRangeOperator extends BaseOperator {
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
        return CustomCheckUtils.atLeast(list[0], list[1]);
    }
}
