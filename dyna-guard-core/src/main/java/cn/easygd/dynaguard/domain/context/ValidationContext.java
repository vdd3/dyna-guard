package cn.easygd.dynaguard.domain.context;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 校验器上下文
 *
 * @author VD
 * @date 2025/7/27 20:52
 */
public abstract class ValidationContext {

    /**
     * 参数
     */
    protected final Map<String, Object> parameters = Maps.newHashMap();

    /**
     * 构建执行上下文
     *
     * @return 上下文
     */
    public abstract Consumer<Map<String, Object>> buildExecuteContext();

    /**
     * 获取参数
     *
     * @return 参数
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * 获取参数
     *
     * @param key key
     * @param <T> 泛型
     * @return 值
     */
    public <T> T get(String key) {
        return (T) parameters.get(key);
    }

    /**
     * 获取参数
     *
     * @param <T>          泛型
     * @param key          key
     * @param defaultValue 默认值
     * @return 值
     */
    public <T> T get(String key, T defaultValue) {
        return Optional.ofNullable(parameters.get(key))
                .map(source -> (T) source)
                .orElse(defaultValue);
    }

    /**
     * 设置参数
     *
     * @param key   key
     * @param value 值
     */
    public void put(String key, Object value) {
        // TODO 需要将对象设置为安全不可变的
        parameters.put(key, value);
    }

    /**
     * 转为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "ValidationContext{" +
                "parameters=" + parameters +
                '}';
    }
}
