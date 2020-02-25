package com.tubecentric.waldo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@EnableJpaAuditing
@EnableConfigurationProperties
public class WaldoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaldoApplication.class, args);
	}

}
