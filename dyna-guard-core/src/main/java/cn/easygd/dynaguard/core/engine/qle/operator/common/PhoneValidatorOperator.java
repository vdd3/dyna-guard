package cn.easygd.dynaguard.core.engine.qle.operator.common;

import cn.easygd.dynaguard.core.engine.qle.operator.BaseOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 手机号验证
 *
 * @author VD
 * @date 2025/8/11 21:51
 */
public class PhoneValidatorOperator extends BaseOperator {

    /**
     * 手机号正则表达式
     */
    private static final Pattern PATTERN_PATH_ABSOLUTE = Pattern.compile("^1[3-9]\\\\d{9}$");

    /**
     * 手机号正则表达式
     */
    private static final Map<String, Pattern> PATTERNS = new HashMap<String, Pattern>() {{
        put("+86", PATTERN_PATH_ABSOLUTE);
        put("+1", Pattern.compile("^\\(?([2-9][0-8][0-9])\\)?[-. ]?([2-9][0-9]{2})[-. ]?([0-9]{4})$"));
        put("+44", Pattern.compile("^\\+?44[ ]?7[0-9]{3}[ ]?[0-9]{6}$"));
        put("+81", Pattern.compile("^(\\+81|0)[789]0[0-9]{8}$"));
        put("+82", Pattern.compile("^(\\+82|0)1[0-9]{8,9}$"));
        put("+33", Pattern.compile("^(\\+33|0)[67]\\d{8}$"));
        put("+91", Pattern.compile("^(\\+91|0)?[6789]\\d{9}$"));
        put("+7", Pattern.compile("^\\+?7[ ]?[0-9]{3}[ ]?[0-9]{3}[ ]?[0-9]{2}[ ]?[0-9]{2}$"));
        put("+61", Pattern.compile("^(\\+61|0)?[45]\\d{8}$"));
    }};

    /**
     * 执行方法
     *
     * @param list 参数列表
     * @return 执行结果
     * @throws Exception 抛出
     */
    @Override
    protected Boolean execute(Object[] list) throws Exception {
        checkParamsSize(list.length, 1);
        String phone = (String) list[0];
        if (list.length > 1) {
            String country = (String) list[1];
            return PATTERNS.get(country).matcher(phone).matches();
        }
        return PATTERN_PATH_ABSOLUTE.matcher(phone).matches();
    }
}
