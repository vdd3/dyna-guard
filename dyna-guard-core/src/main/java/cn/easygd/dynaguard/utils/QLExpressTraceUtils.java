package cn.easygd.dynaguard.utils;

import com.alibaba.qlexpress4.runtime.trace.ExpressionTrace;
import com.alibaba.qlexpress4.runtime.trace.TraceType;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * qlexpress4 脚本执行追踪工具类
 *
 * @author VD
 */
public class QLExpressTraceUtils {

    /**
     * 空格
     */
    private static final String SPACE = " ";

    /**
     * 点
     */
    private static final String POINT = ".";

    /**
     * 值分割符
     */
    private static final String VALUE_SPLIT = "V:";

    /**
     * 方法分割符
     */
    private static final String METHOD_SPLIT = "M:";

    /**
     * list分割符
     */
    private static final String LIST_SPLIT = "List:";

    /**
     * 获取条件
     *
     * @param trace     链路追踪
     * @param condition 条件构建
     * @return 条件
     */
    public static StringBuilder getCondition(ExpressionTrace trace, StringBuilder condition) {
        List<ExpressionTrace> traceChildren = trace.getChildren();
        TraceType type = trace.getType();
        switch (type) {
            case OPERATOR:
            case FUNCTION:
            case METHOD:
                condition.append(METHOD_SPLIT).append(trace.getToken()).append(" ( ");
                traceChildren.forEach(child -> getCondition(child, condition));
                condition.append(" )").append(SPACE);
                break;
            case FIELD:
                // field为属性，需要去下级的参数名称
                ExpressionTrace fieldTrace = traceChildren.get(0);
                getCondition(fieldTrace, condition).append(POINT);
                condition.append(trace.getToken()).append(SPACE);
                break;
            case LIST:
                // list为定义的变量
                condition.append(LIST_SPLIT).append("[");
                traceChildren.forEach(child -> getCondition(child, condition));
                condition.append("]");
                break;
            case IF:
                // if模块直接取条件
            case RETURN:
                // return模块直接取条件
                traceChildren.forEach(child -> getCondition(child, condition));
                break;
            case VARIABLE:
                // variable模块为变量，直接取变量名
                condition.append(VALUE_SPLIT);
                condition.append(trace.getToken());
                break;
            case VALUE:
                // value模块直接取token,替换掉掉引号
                condition.append(VALUE_SPLIT)
                        .append(trace.getToken()
                                .replace("'", "")
                                .replace("\"", ""))
                        .append(SPACE);
                break;
            case BLOCK:
                if (CollectionUtils.isNotEmpty(traceChildren)) {
                    // 需要排除return模块
                    traceChildren.stream().filter(child -> child.getType() != TraceType.RETURN)
                            .forEach(child -> getCondition(child, condition));
                }
                break;
            default:
                break;
        }
        return condition;
    }
}
