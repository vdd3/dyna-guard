package cn.easygd.dynaguard.utils;

import com.google.common.collect.Range;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * 自定义检测工具类
 *
 * @author VD
 * @date 2025/8/20 19:31
 */
public class CustomCheckUtils {

    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return 是否为空
     */
    public static Boolean notNull(Object obj) {
        return Optional.ofNullable(obj)
                .map(source -> {
                    if (source instanceof String) {
                        return StringUtils.isNotBlank((String) source);
                    } else if (source instanceof Collection) {
                        return CollectionUtils.isNotEmpty((Collection) source);
                    } else if (source instanceof Map) {
                        return MapUtils.isNotEmpty((Map) source);
                    } else {
                        return true;
                    }
                }).orElse(false);
    }

    /**
     * 判断是否在闭区间内
     *
     * @param left  最小值
     * @param right 最大值
     * @param value 待判断的值
     * @return 是否在闭区间内
     */
    public static Boolean inClosedRange(Object left, Object right, Object value) {
        Comparable<?> lower = convertToComparable(left);
        Comparable<?> up = convertToComparable(right);
        Comparable<?> target = convertToComparable(value);
        Range<Comparable<?>> range = Range.closed(lower, up);
        return range.contains(target);
    }

    /**
     * 判断value是否在区间内
     *
     * @param left  左边界
     * @param right 右边界
     * @param value 待判断的值
     * @return 是否在区间内
     */
    public static Boolean inOpenRange(Object left, Object right, Object value) {
        Comparable<?> lower = convertToComparable(left);
        Comparable<?> up = convertToComparable(right);
        Comparable<?> target = convertToComparable(value);
        Range<Comparable<?>> range = Range.open(lower, up);
        return range.contains(target);
    }

    /**
     * 判断value是否在区间内
     *
     * @param left  左边界
     * @param right 右边界
     * @param value 待判断的值
     * @return 是否在区间内
     */
    public static Boolean inClosedOpenRange(Object left, Object right, Object value) {
        Comparable<?> lower = convertToComparable(left);
        Comparable<?> up = convertToComparable(right);
        Comparable<?> target = convertToComparable(value);
        Range<Comparable<?>> range = Range.closedOpen(lower, up);
        return range.contains(target);
    }

    /**
     * 判断value是否在区间内
     *
     * @param left  左边界
     * @param right 右边界
     * @param value 待判断的值
     * @return 是否在区间内
     */
    public static Boolean inOpenClosedRange(Object left, Object right, Object value) {
        Comparable<?> lower = convertToComparable(left);
        Comparable<?> up = convertToComparable(right);
        Comparable<?> target = convertToComparable(value);
        Range<Comparable<?>> range = Range.openClosed(lower, up);
        return range.contains(target);
    }

    /**
     * 判断value是否在区间内
     *
     * @param left  左边界
     * @param value 待判断的值
     * @return 是否在区间内
     */
    public static Boolean atLeast(Object left, Object value) {
        Comparable<?> critical = convertToComparable(left);
        Comparable<?> target = convertToComparable(value);
        Range<Comparable<?>> range = Range.atLeast(critical);
        return range.contains(target);
    }

    /**
     * 判断value是否在区间内
     *
     * @param left  左边界
     * @param value 待判断的值
     * @return 是否在区间内
     */
    public static Boolean greaterThan(Object left, Object value) {
        Comparable<?> critical = convertToComparable(left);
        Comparable<?> target = convertToComparable(value);
        Range<Comparable<?>> range = Range.greaterThan(critical);
        return range.contains(target);
    }

    /**
     * 判断 value 是否在区间内
     *
     * @param right 右边界
     * @param value 待判断的值
     * @return 是否在区间内
     */
    public static Boolean atMost(Object right, Object value) {
        Comparable<?> critical = convertToComparable(right);
        Comparable<?> target = convertToComparable(value);
        Range<Comparable<?>> range = Range.atMost(critical);
        return range.contains(target);
    }

    /**
     * 判断 value 是否在区间内
     *
     * @param right 右边界
     * @param value 待判断的值
     * @return 是否在区间内
     */
    public static Boolean lessThan(Object right, Object value) {
        Comparable<?> critical = convertToComparable(right);
        Comparable<?> target = convertToComparable(value);
        Range<Comparable<?>> range = Range.lessThan(critical);
        return range.contains(target);
    }


    /**
     * 将参数转换为可比较类型（Comparable），支持常见数字类型
     *
     * @return 可比较类型
     */
    private static Comparable<?> convertToComparable(Object param) {
        if (param instanceof Comparable) {
            return (Comparable<?>) param;
        }
        throw new IllegalArgumentException("参数类型不支持比较：" + param.getClass().getSimpleName());
    }
}
