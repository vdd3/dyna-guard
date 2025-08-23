package cn.easygd.dynaguard.utils;

import com.google.common.collect.Maps;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;

/**
 * 安全参数工具类
 *
 * @author VD
 * @date 2025/8/20 21:37
 */
public class SecurityParamUtils {

    /**
     * set方法前缀
     */
    private static final String SET_METHOD_PREFIX = "set";

    /**
     * 代理缓存
     */
    private static final Map<Class<?>, Enhancer> ENHANCER_CACHE = Maps.newConcurrentMap();

    /**
     * 创建安全代理
     *
     * @param param 参数
     * @return 安全代理
     */
    public static Enhancer createSecureProxy(Object param) {
        Enhancer enhancer = ENHANCER_CACHE.get(param.getClass());
        if (Objects.isNull(enhancer)) {
            enhancer = new Enhancer();
            enhancer.setSuperclass(param.getClass());
        }
        // 使用CGLIB代理具体类,这里只在每次创建代理对象时设置回调
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            // 禁止调用setter方法
            if (method.getName().startsWith(SET_METHOD_PREFIX)) {
                throw new UnsupportedOperationException("禁止修改参数信息");
            }
            return method.invoke(param, args);
        });
        ENHANCER_CACHE.put(param.getClass(), enhancer);
        return enhancer;
    }

    /**
     * 判断是否可以使用CGLIB代理
     *
     * @param clazz 类
     * @return 是否可以代理
     */
    public static boolean canProxyWithCglib(Class<?> clazz) {
        // 检查类是否为final
        if (Modifier.isFinal(clazz.getModifiers())) {
            return false;
        }

        // 检查类是否为接口或注解
        if (clazz.isInterface() || clazz.isAnnotation()) {
            return false;
        }

        // 检查类是否为数组或枚举
        if (clazz.isArray() || clazz.isEnum()) {
            return false;
        }

        // 判断是否已经被代理
        if (Enhancer.isEnhanced(clazz) || Proxy.isProxyClass(clazz)) {
            return false;
        }


        // 检查是否有可访问的构造函数
        try {
            clazz.getConstructor();
            return true;
        } catch (NoSuchMethodException e) {
            // 检查是否有其他公共构造函数
            for (Constructor<?> constructor : clazz.getConstructors()) {
                if (Modifier.isPublic(constructor.getModifiers())) {
                    return true;
                }
            }
            return false;
        }
    }
}
