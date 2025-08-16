package cn.easygd.dynaguard;


import cn.easygd.dynaguard.core.annotation.DynamicGuard;
import cn.easygd.dynaguard.core.chain.ValidationChain;
import cn.easygd.dynaguard.core.chain.ValidationChainManager;
import cn.easygd.dynaguard.domain.SpringValidationContext;
import cn.easygd.dynaguard.domain.context.ValidationContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 验证方法拦截器
 *
 * @author VD
 * @date 2025/8/3 12:33
 */
public class ValidationMethodInterceptor implements MethodInterceptor {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ValidationMethodInterceptor.class);

    /**
     * 验证链管理器
     */
    private ValidationChainManager validationChainManager;

    /**
     * 方法拦截
     *
     * @param invocation 方法调用
     * @return 方法执行结果
     * @throws Throwable 抛出
     */
    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        // 1. 获取目标方法和注解信息
        Method method = invocation.getMethod();
        DynamicGuard dynamicGuard = method.getAnnotation(DynamicGuard.class);
        if (dynamicGuard == null) {
            log.info("dynamicGuard annotation can't find skip validation");
            return invocation.proceed();
        }

        // 2.获取验证链id，如果注解中没有，则按照方法路径获取
        String group = dynamicGuard.group();
        String chainId = StringUtils.defaultIfBlank(dynamicGuard.chainId(), generateDefaultChainId(method));

        // 3.获取对应的校验链
        ValidationChain chain = validationChainManager.getChain(group, chainId);
        if (chain == null) {
            log.info("chain can't find group : [{}] , chainId : [{}]", group, chainId);
            return invocation.proceed();
        }

        // 4.构建验证上下文
        ValidationContext context = new SpringValidationContext();
        // 这里是为了获取入参的名称
        Parameter[] parameters = method.getParameters();
        // 真实的入参
        Object[] arguments = invocation.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            context.put(parameters[i].getName(), arguments[i]);
        }

        // 5.执行验证链
        if (dynamicGuard.enableGuard()) {
            chain.executeGuard(context);
        } else {
            chain.execute(context);
        }

        // 6.验证通过
        return invocation.proceed();
    }

    /**
     * 生成默认 chainId：包名.类名.方法名
     */
    private String generateDefaultChainId(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        // 包名
        String packageName = declaringClass.getPackage().getName();
        // 类名
        String className = declaringClass.getSimpleName();
        // 方法名
        String methodName = method.getName();
        return String.format("%s.%s.%s", packageName, className, methodName);
    }

    /**
     * 注入验证链管理器
     *
     * @param validationChainManager 验证链管理器
     */
    public void setValidationChainManager(ValidationChainManager validationChainManager) {
        this.validationChainManager = validationChainManager;
    }
}
