package com.dg.core.engine.groovy;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.util.Collection;
import java.util.Map;

/**
 * groovy元素只读
 *
 * @author VD
 * @date 2025/7/28 20:49
 */
public class ReadOnlyMetaClass extends DelegatingMetaClass {

    /**
     * 创建只读元类
     *
     * @param delegate 元类
     */
    public ReadOnlyMetaClass(MetaClass delegate) {
        super(delegate);
    }

    /**
     * @see groovy.lang.MetaClass#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object[])
     */
    @Override
    public Object invokeMethod(Object object, String methodName, Object[] arguments) {
        if (object instanceof Collection) {
            if (methodName.startsWith("add")) {
                throw new SecurityException("禁止修改对象属性: " + methodName);
            }
        } else if (object instanceof Map) {
            if (methodName.startsWith("put")) {
                throw new SecurityException("禁止修改对象属性: " + methodName);
            }
        } else if (methodName.startsWith("set") && arguments.length == 1) {
            throw new SecurityException("禁止修改对象属性: " + methodName);
        }

        // 允许其他方法调用
        return super.invokeMethod(object, methodName, arguments);
    }

    /**
     * 注册只读元类
     *
     * @param clazz 类
     */
    public static void registerReadOnlyMetaClass(Class<?> clazz) {
        MetaClassRegistry registry = InvokerHelper.getMetaRegistry();
        MetaClass originalMetaClass = registry.getMetaClass(clazz);
        MetaClass readOnlyMetaClass = new ReadOnlyMetaClass(originalMetaClass);
        registry.setMetaClass(clazz, readOnlyMetaClass);
    }
}