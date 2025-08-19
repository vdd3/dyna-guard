package cn.easygd.dynaguard.core.engine;

import cn.easygd.dynaguard.domain.ValidationResult;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.exception.ResultTypeIllegalException;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * 基础规则引擎
 *
 * @author VD
 * @date 2025/7/28 20:45
 */
public abstract class BaseValidator implements Validator {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(BaseValidator.class);

    /**
     * 脚本缓存
     */
    protected final Map<String, Object> SCRIPT_CACHE = Maps.newConcurrentMap();

    /**
     * 执行
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 验证结果
     */
    @Override
    public ValidationResult execute(String script, ValidationContext context) {
        try {
            // 获取缓存中是否存在脚本
            Object scriptCache = SCRIPT_CACHE.get(script);
            if (Objects.isNull(scriptCache)) {
                // 编译脚本
                scriptCache = compile(script);
                SCRIPT_CACHE.put(script, scriptCache);
            }

            Boolean flag = validate(scriptCache, context);
            return flag ? ValidationResult.success() : ValidationResult.fail();
        } catch (ResultTypeIllegalException e) {
            throw e;
        } catch (Exception e) {
            log.warn("validate catch exception , script : [{}] , context : [{}]", script, context, e);
            return ValidationResult.fail(e.getMessage(), e);
        }
    }

    /**
     * 安全参数
     *
     * @param param 参数
     * @return 安全参数
     */
    @Override
    public Object securityParam(Object param) {
        // TODO 默认将参数设置为不可变参数


        return null;
    }

    /**
     * 编译
     *
     * @param script 脚本
     * @return 编译结果
     */
    public abstract Object compile(String script) throws Exception;

    /**
     * 验证
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 是否成功
     */
    protected abstract Boolean validate(Object script, ValidationContext context) throws Exception;
}
