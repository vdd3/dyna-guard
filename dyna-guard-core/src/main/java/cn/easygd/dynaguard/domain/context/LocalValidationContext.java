package cn.easygd.dynaguard.domain.context;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 本地验证上下文
 *
 * @author VD
 * @date 2025/8/15 20:46
 */
public class LocalValidationContext extends ValidationContext {

    /**
     * 构建执行上下文
     *
     * @return 上下文
     */
    @Override
    public Consumer<Map<String, Object>> buildExecuteContext() {
        // TODO 构建用户的上下文时，需要将参数设置为安全不可变的


        return map -> map.putAll(this.getParameters());
    }
}
