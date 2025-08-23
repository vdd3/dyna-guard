package cn.easygd.dynaguard.core.engine.qle.operator.common;

import cn.easygd.dynaguard.core.engine.qle.operator.QlEBaseFunction;
import cn.easygd.dynaguard.utils.CustomCheckUtils;

/**
 * 非空判断关键字
 *
 * @author VD
 * @date 2025/8/10 22:09
 */
public class NotNullFunctionQlE extends QlEBaseFunction {

    /**
     * 执行方法
     *
     * @param list 参数列表
     * @return 执行结果
     * @throws Exception 抛出
     */
    @Override
    protected Boolean execute(Object[] list) throws Exception {
        checkParamsSize(list.length, 1);
        Object target = list[0];
        return CustomCheckUtils.notNull(target);
    }
}
