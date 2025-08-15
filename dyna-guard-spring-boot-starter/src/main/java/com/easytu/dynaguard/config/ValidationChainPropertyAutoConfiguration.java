package com.easytu.dynaguard.config;

import com.easytu.dynaguard.domain.config.ValidationChainConfig;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 验证配置自动注册
 *
 * @author VD
 * @date 2025/8/3 12:03
 */
@Configuration
@EnableConfigurationProperties(ValidationProperty.class)
@PropertySource("classpath:validation.properties")
public class ValidationChainPropertyAutoConfiguration {

    /**
     * 验证配置
     *
     * @param property 验证属性
     * @return 验证配置
     */
    @Bean
    public ValidationChainConfig validationChainConfig(ValidationProperty property) {
        ValidationChainConfig config = new ValidationChainConfig();
        config.setParserList(Lists.newArrayList(property.getParser().split(",")));
        config.setPathParserName(property.getPathParserName());
        config.setValidationMethodList(Lists.newArrayList(property.getValidationMethod().split(",")));
        config.setChainFilePath(Lists.newArrayList(property.getChainFilePath().split(",")));
        config.setSqlChainDataMap(property.getSqlChainDataMap());
        config.setXmlChainDataMap(property.getXmlChainDataMap());
        config.setJsonChainDataMap(property.getJsonChainDataMap());
        return config;
    }

}
