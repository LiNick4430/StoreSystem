package com.storesystem.backend.model.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductFindByIdDTO {	// 搜尋 特定商品
	
	private Long id;			// 商品ID
}
