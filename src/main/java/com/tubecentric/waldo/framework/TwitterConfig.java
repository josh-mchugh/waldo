package com.tubecentric.waldo.framework;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "twitter")
public class TwitterConfig {

    private String key;
    private String secret;
    private String accessToken;
    private String accessTokenSecret;
}
