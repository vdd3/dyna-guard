package cn.easygd.dynaguard.core.engine.qle.operator.bean;

import cn.easygd.dynaguard.core.holder.GlobalBeanContextHolder;
import com.ql.util.express.Operator;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.util.Objects;

/**
 * 调用Bean方法
 *
 * @author VD
 * @date 2025/8/16 18:14
 */
public class InvokeBeanMethodOperator extends Operator {
    @Override
    public Object executeInner(Object[] list) throws Exception {
        if (list.length < 2) {
            throw new IllegalArgumentException("参数不足 , 至少需要两个参数");
        }
        String beanName = (String) list[0];
        String methodName = (String) list[1];

        // 获取对应的bean
        Object bean = GlobalBeanContextHolder.getContext().getBean(beanName);
        if (Objects.isNull(bean)) {
            throw new IllegalArgumentException("未找到bean : " + beanName);
        }

        // 通过内省获取类信息，兼容cglb代理的bean
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
        MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
        for (MethodDescriptor methodDescriptor : methodDescriptors) {
            if (methodDescriptor.getName().equals(methodName)) {
                // 无参调用
                if (list.length == 2) {
                    return methodDescriptor.getMethod().invoke(bean);
                } else {
                    Object[] args = new Object[list.length - 2];
                    // 第三个开始是参数,如果不存在参数直接跳过
                    for (int i = 0; i < args.length; i++) {
                        args[0] = list[i + 2];
                    }
                    return methodDescriptor.getMethod().invoke(bean, args);
                }
            }
        }
        // 如果没有匹配到方法，返回null
        return null;
    }
}
