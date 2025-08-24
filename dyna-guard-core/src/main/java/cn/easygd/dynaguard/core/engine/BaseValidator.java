package cn.easygd.dynaguard.core.engine;

import cn.easygd.dynaguard.core.bean.GlobalBeanContext;
import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.core.trace.BizTracker;
import cn.easygd.dynaguard.domain.ValidationResult;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import cn.easygd.dynaguard.domain.exception.ResultTypeIllegalException;
import cn.easygd.dynaguard.utils.SecurityParamUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 基础规则引擎
 *
 * @author VD
 * @version v 0.1 2025/7/28 20:45
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
            // 对具体脚本返回值重置
            BizTracker.reset();
            // 设置具体的语言
            BizTracker.language(getLanguage());

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
     * 检查结果类型
     *
     * @param result 结果
     * @return 是否成功
     */
    protected Boolean checkResult(Object result) {
        if (!(result instanceof Boolean)) {
            throw new ResultTypeIllegalException();
        }
        return (Boolean) result;
    }

    /**
     * 构建参数
     *
     * @param context 上下文
     * @return 参数
     */
    @Override
    public Map<String, Object> buildParam(ValidationContext context) {
        Map<String, Object> params = Maps.newHashMap();
        context.buildExecuteContext().accept(params);
        // 根据安全策略创建安全参数
        params.forEach((key, value) -> params.put(key, createSecurityParam(value)));
        return params;
    }

    /**
     * 参数安全处理
     *
     * @param param 参数
     * @return 安全参数
     */
    private Object createSecurityParam(Object param) {
        Boolean enableSecurityStrategy = ChainConfigHolder.getConfig().getEnableSecurityStrategy();
        if (enableSecurityStrategy) {
            if (param instanceof List) {
                return ImmutableList.copyOf((List) param);
            } else if (param instanceof Map) {
                return ImmutableMap.copyOf((Map) param);
            } else if (param instanceof Set) {
                return ImmutableSet.copyOf((Set) param);
            } else if (param.getClass().getClassLoader() == null) {
                // 这里是java自己的类，不需要代理
                return param;
            } else if (param instanceof GlobalBeanContext) {
                return param;
            } else {
                // 这里使用CGLIB代理具体类
                if (SecurityParamUtils.canProxyWithCglib(param.getClass())) {
                    return SecurityParamUtils.createSecureProxy(param).create();
                }
                return param;
            }
        } else {
            return param;
        }
    }

    /**
     * 编译
     *
     * @param script 脚本
     * @return 编译结果
     * @throws Exception 编译异常
     */
    public abstract Object compile(String script) throws Exception;

    /**
     * 验证
     *
     * @param script  执行脚本
     * @param context 上下文
     * @return 是否成功
     * @throws Exception 执行异常
     */
    protected abstract Boolean validate(Object script, ValidationContext context) throws Exception;
}
