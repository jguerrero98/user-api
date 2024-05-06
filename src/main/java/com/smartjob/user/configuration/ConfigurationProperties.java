package com.smartjob.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationProperties {
    @Bean
    public UserProperties userProperties() {
        return new UserProperties();
    }
}
