package com.storesystem.backend.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private FrontendProperties frontendProperties;
	
	/**
	 * 集中處理 Controller 上的 @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
	 * */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		
		// 允許前端的來源 ( 前端來源 )
		configuration.setAllowedOrigins(List.of(frontendProperties.getUrl()));
		
		// 允許的方法 (全部)
		configuration.setAllowedMethods(List.of("*"));
		
		// 允許的標頭 (全部)
		configuration.setAllowedHeaders(List.of("*"));
		
		// 允許帶有憑證 為了給 JWT使用
		configuration.setAllowCredentials(true);
		
		// 註冊到所有路徑
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
	}
	
	/**
	 * 安全設定 過濾使用的方法
	 * */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// 1. 停用 CSRF 保護
			.csrf(csrf -> csrf.disable())
			
			// 2. 套用上面自定的 
			.cors(Customizer.withDefaults())
			
			// 3. 目前預定 全網頁通行
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll()	
			);
		
		return http.build();
	}
	
}
