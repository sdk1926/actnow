package com.sdk.actnow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@EnableJpaAuditing
@SpringBootApplication
public class ActnowApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActnowApplication.class, args);
	}
	@Bean
	public PageableHandlerMethodArgumentResolverCustomizer customize() { 
		return p -> p.setOneIndexedParameters(true);
	}

}
