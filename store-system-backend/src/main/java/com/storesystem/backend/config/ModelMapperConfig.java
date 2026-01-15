package com.storesystem.backend.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.storesystem.backend.model.dto.product.ProductDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDTO;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.model.entity.Supplier;

@Configuration
public class ModelMapperConfig {

	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		// 啟動 嚴格模式(名稱完全一致), 關閉 模糊猜測 功能
		modelMapper.getConfiguration()
					.setMatchingStrategy(MatchingStrategies.STRICT);
		
		// Entity -> DTO
		// TODO 放入 加入轉換邏輯
		modelMapper.typeMap(Product.class, ProductDTO.class).addMappings(mapper -> {
			mapper.map(Product::getProductId, ProductDTO::setId);
			mapper.map(Product::getProductName, ProductDTO::setName);
			mapper.map(Product::getStockQuantity, ProductDTO::setStock);
		});
		
		modelMapper.typeMap(Supplier.class, SupplierDTO.class).addMappings(mapper -> {
			mapper.map(Supplier::getSupplierId, SupplierDTO::setId);
			mapper.map(Supplier::getSupplierName, SupplierDTO::setName);
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
