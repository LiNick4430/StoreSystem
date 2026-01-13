package com.storesystem.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// 1. 停用 CSRF 保護
			.csrf(csrf -> csrf.disable())
			
			// 2. 目前預定 全網頁通行
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll()	
			);
		
		return http.build();
	}
	
}
