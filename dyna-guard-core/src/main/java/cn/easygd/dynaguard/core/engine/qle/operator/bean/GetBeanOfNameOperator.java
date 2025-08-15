package cn.easygd.dynaguard.core.engine.qle.operator.bean;

import cn.easygd.dynaguard.core.bean.GlobalBeanContext;
import cn.easygd.dynaguard.core.holder.GlobalBeanContextHolder;
import com.ql.util.express.Operator;

import java.util.Objects;

/**
 * 获取Bean
 *
 * @author VD
 * @date 2025/8/12 21:22
 */
public class GetBeanOfNameOperator extends Operator {

    @Override
    public Object executeInner(Object[] list) throws Exception {
        if (list.length < 1) {
            throw new RuntimeException("参数不足");
        }
        String beanName = (String) list[0];
        GlobalBeanContext beanContext = GlobalBeanContextHolder.getContext();
        if (Objects.isNull(beanContext)) {
            throw new RuntimeException("未找到beanContext");
        }
        Object bean = beanContext.getBean(beanName);
        if (Objects.isNull(bean)) {
            throw new RuntimeException("未找到bean : " + beanName);
        }
        return bean;
    }
}
