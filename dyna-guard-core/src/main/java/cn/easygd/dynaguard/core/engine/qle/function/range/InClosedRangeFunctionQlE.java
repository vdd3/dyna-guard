package cn.easygd.dynaguard.core.engine.qle.function.range;

import cn.easygd.dynaguard.core.engine.qle.function.QlEBaseFunction;
import cn.easygd.dynaguard.utils.CustomCheckUtils;

/**
 * 在闭区间范围内
 *
 * @author VD
 */
public class InClosedRangeFunctionQlE extends QlEBaseFunction {
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
        return CustomCheckUtils.inClosedRange(list[0], list[1], list[2]);
    }
}
