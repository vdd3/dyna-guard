package com.dg.config;

import com.dg.domain.config.ValidationChainConfig;
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
@EnableConfigurationProperties()
@PropertySource(name = "Liteflow Default Properties", value = "classpath:/liteflow-default.properties")
public class ValidationChainPropertyAutoConfiguration {

    @Bean
    public ValidationChainConfig validationChainConfig() {
        ValidationChainConfig config = new ValidationChainConfig();
        return config;
    }

}
