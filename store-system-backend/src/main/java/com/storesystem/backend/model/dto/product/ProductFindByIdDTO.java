package com.storesystem.backend.model.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductFindByIdDTO {	// 搜尋 特定商品
	
	@NotNull(message = "缺少商品ID")
	private Long id;			// 商品ID
}
