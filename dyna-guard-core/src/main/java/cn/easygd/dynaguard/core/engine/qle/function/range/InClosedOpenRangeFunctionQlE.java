package cn.easygd.dynaguard.core.engine.qle.function.range;

import cn.easygd.dynaguard.core.engine.qle.function.QlEBaseFunction;
import cn.easygd.dynaguard.utils.CustomCheckUtils;

/**
 * 在闭开区间内
 *
 * @author VD
 * @version v 0.1 2025/8/11 21:28
 */
public class InClosedOpenRangeFunctionQlE extends QlEBaseFunction {

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
        return CustomCheckUtils.inClosedOpenRange(list[0], list[1], list[2]);
    }
}
