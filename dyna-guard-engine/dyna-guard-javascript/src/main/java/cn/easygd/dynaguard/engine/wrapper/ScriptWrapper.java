package cn.easygd.dynaguard.engine.wrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 脚本包装类
 *
 * @author VD
 */
public class ScriptWrapper {

    public static final String HELPER_FUNCTIONS = "var __return_site = null;\n" +
            "    var __return_value = null;\n" +
            "    \n" +
            "    function traceReturn(value, site) {\n" +
            "        __return_site = site;\n" +
            "        __return_value = value;\n" +
            "        return value;\n" +
            "    }";

    private static final Pattern RETURN_PATTERN = Pattern.compile("(?<=return\\s\\+)([^;]+)(?=;)",
            Pattern.MULTILINE
    );

    public static String wrapScript(String script) {
        // 先清理注释
        String cleaned = script
                .replaceAll("//.*", "")
                .replaceAll("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/", "");

        Matcher m = RETURN_PATTERN.matcher(cleaned);
        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            String expr = m.group(1);
            // 转义双引号
            String safeExpr = expr.replace("\"", "\\\"");
            m.appendReplacement(sb, "traceReturn(" + expr + ", \"" + safeExpr + "\")");
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
