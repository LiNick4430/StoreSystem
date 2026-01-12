package com.storesystem.backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		// Entity -> DTO
		// TODO 放入 加入轉換邏輯
		
		// ------------------------------------------------------------------------------------
		// DTO -> Entity
		// TODO 放入 加入轉換邏輯
		
		// ------------------------------------------------------------------------------------
		// DTO -> DTO
		// TODO 放入 加入轉換邏輯
		
		return modelMapper;
	}
}
