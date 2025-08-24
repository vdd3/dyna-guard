package cn.easygd.dynaguard.config;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * 验证bean选择器
 *
 * @author VD
 * @version v 0.1 2025/8/15 19:15
 */
public class ValidationBeanSelector extends AdviceModeImportSelector<EnableValidation> {

    /**
     * Determine which classes should be imported based on the given {@code AdviceMode}.
     * <p>Returning {@code null} from this method indicates that the {@code AdviceMode}
     * could not be handled or was unknown and that an {@code IllegalArgumentException}
     * should be thrown.
     *
     * @param adviceMode the value of the {@linkplain #getAdviceModeAttributeName()
     *                   advice mode attribute} for the annotation specified via generics.
     * @return array containing classes to import (empty array if none;
     * {@code null} if the given {@code AdviceMode} is unknown)
     */
    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return new String[]{
                "cn.easygd.dynaguard.config.ValidationChainAutoConfig",
                "cn.easygd.dynaguard.config.ValidationChainPropertyAutoConfiguration"
        };
    }
}
