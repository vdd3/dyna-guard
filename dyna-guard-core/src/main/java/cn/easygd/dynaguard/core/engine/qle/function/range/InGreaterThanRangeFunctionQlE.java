package cn.easygd.dynaguard.core.engine.qle.function.range;

import cn.easygd.dynaguard.core.engine.qle.function.QlEBaseFunction;
import cn.easygd.dynaguard.utils.CustomCheckUtils;

/**
 * 最小值判断
 *
 * @author VD
 */
public class InGreaterThanRangeFunctionQlE extends QlEBaseFunction {

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
        return CustomCheckUtils.greaterThan(list[0], list[1]);
    }
}
