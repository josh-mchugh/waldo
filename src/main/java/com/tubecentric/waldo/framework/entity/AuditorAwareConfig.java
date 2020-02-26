package com.tubecentric.waldo.framework.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
@RequiredArgsConstructor
public class AuditorAwareConfig {

    private final AuditorAwareImpl auditorAware;

    @Bean
    public AuditorAware<String> auditorAware() {

        return auditorAware;
    }
}
