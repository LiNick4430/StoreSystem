package com.storesystem.backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.storesystem.backend.model.dto.product.ProductDTO;
import com.storesystem.backend.model.entity.Product;

@Configuration
public class ModelMapperConfig {

	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		// Entity -> DTO
		// TODO 放入 加入轉換邏輯
		modelMapper.typeMap(Product.class, ProductDTO.class).addMappings(mapper -> {
			mapper.map(Product::getProductId, ProductDTO::setId);
			mapper.map(Product::getProductName, ProductDTO::setName);
			mapper.map(Product::getStockQuantity, ProductDTO::setStock);
		});
		
		// ------------------------------------------------------------------------------------
		// DTO -> Entity
		// TODO 放入 加入轉換邏輯
		
		// ------------------------------------------------------------------------------------
		// DTO -> DTO
		// TODO 放入 加入轉換邏輯
		
		return modelMapper;
	}
}
