package cn.easygd.dynaguard.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 类型转换工具类
 *
 * @author VD
 * @version v 0.1 2025/8/23 19:48
 */
public class TypeConvertUtils {

    /**
     * 将源对象转换为目标类型
     *
     * @param source     源对象
     * @param targetType 目标类型
     * @return 转换后的对象（失败返回null）
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object source, Class<T> targetType) {
        if (source == null) {
            return null;
        }

        // 如果源类型已与目标类型一致，直接返回
        if (targetType.isInstance(source)) {
            return (T) source;
        }

        // 处理数字类型转换（最常见场景）
        if (Number.class.isAssignableFrom(targetType)) {
            return (T) convertNumber((Number) source, targetType);
        }

        // 处理字符串类型
        if (targetType == String.class) {
            return (T) source.toString();
        }

        return null;
    }

    /**
     * 数字类型转换
     */
    private static Number convertNumber(Number source, Class<?> targetType) {
        if (targetType == Integer.class || targetType == int.class) {
            return source.intValue();
        } else if (targetType == Long.class || targetType == long.class) {
            return source.longValue();
        } else if (targetType == Double.class || targetType == double.class) {
            return source.doubleValue();
        } else if (targetType == Float.class || targetType == float.class) {
            return source.floatValue();
        } else if (targetType == BigDecimal.class) {
            return new BigDecimal(source.toString());
        } else if (targetType == BigInteger.class) {
            return BigInteger.valueOf(source.longValue());
        } else {
            return null;
        }
    }
}
