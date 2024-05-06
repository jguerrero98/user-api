package com.smartjob.user.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "user-regex")
public class UserProperties {
    public String emailRegex;
    public String emailMessage;
    public String passwordRegex;
    public String passwordMessage;
}
