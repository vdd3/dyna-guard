package cn.easygd.dynaguard.core.engine.qle.operator.common;

import com.ql.util.express.Operator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author VD
 * @date 2025/8/10 22:09
 */
public class NotNullOperator extends Operator {


    @Override
    public Object executeInner(Object[] list) throws Exception {
        Object target = list[0];
        return Optional.ofNullable(target)
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
}
