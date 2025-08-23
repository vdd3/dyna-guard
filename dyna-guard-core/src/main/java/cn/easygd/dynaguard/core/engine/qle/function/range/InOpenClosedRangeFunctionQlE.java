package cn.easygd.dynaguard.core.engine.qle.function.range;

import cn.easygd.dynaguard.core.engine.qle.function.QlEBaseFunction;
import cn.easygd.dynaguard.utils.CustomCheckUtils;

/**
 * 在开闭区间内
 *
 * @author VD
 * @date 2025/8/11 21:25
 */
public class InOpenClosedRangeFunctionQlE extends QlEBaseFunction {

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
        return CustomCheckUtils.inOpenClosedRange(list[0], list[1], list[2]);
    }
}
