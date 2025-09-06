package cn.easygd.dynaguard.config;

import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 验证配置自动注册
 *
 * @author VD
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
        config.setParserList(property.getParser());
        config.setPathParserName(property.getPathParserName());
        config.setValidationMethodList(property.getValidationMethod());
        config.setChainFilePath(Lists.newArrayList(property.getChainFilePath().split(",")));
        config.setEnableSecurityStrategy(property.getEnableSecurityStrategy());
        config.setEnableBizTrace(property.getEnableBizTrace());
        config.setSqlChainDataMap(property.getSqlChainDataMap());
        config.setXmlChainDataMap(property.getXmlChainDataMap());
        config.setJsonChainDataMap(property.getJsonChainDataMap());
        return config;
    }

}
