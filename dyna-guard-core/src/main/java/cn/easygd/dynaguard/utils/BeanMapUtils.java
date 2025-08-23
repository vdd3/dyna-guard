package cn.easygd.dynaguard.utils;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author VD
 * @date 2025/8/5 21:13
 */
public class BeanMapUtils {

    /**
     * map 转 java bean
     *
     * @param map    赋值map
     * @param target 需要转换为的bean
     * @param <T>    bean的类型
     * @return 转换后的bean
     */
    public static <T> T map2Bean(Map<String, String> map, Class<T> target) {
        return map2Bean(map, Maps.newHashMap(), target);
    }

    /**
     * map 转 java bean
     *
     * @param map          赋值map
     * @param converterMap 转换器
     * @param target       需要转换为的bean
     * @param <T>          bean的类型
     * @return 转换后的bean
     */
    public static <T> T map2Bean(Map<String, String> map, Map<String, Function<String, Object>> converterMap, Class<T> target) {
        if (MapUtils.isEmpty(map) || target == null) {
            return null;
        }
        try {
            T t = target.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(target, Object.class);
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                String fieldName = propertyDescriptor.getName();
                if (map.containsKey(fieldName)) {
                    String value = map.get(fieldName);
                    Function<String, Object> converter = converterMap.get(fieldName);
                    if (Objects.nonNull(converter)) {
                        Object finalValue = converter.apply(value);
                        propertyDescriptor.getWriteMethod().invoke(t, finalValue);
                    } else {
                        propertyDescriptor.getWriteMethod().invoke(t, value);
                    }

                }
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException("map to bean exception : " + e.getMessage(), e);
        }
    }
}
