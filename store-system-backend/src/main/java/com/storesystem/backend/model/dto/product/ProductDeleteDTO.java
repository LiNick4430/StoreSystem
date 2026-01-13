package com.storesystem.backend.model.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDeleteDTO {	// 刪除 商品資料
	
	@NotNull(message = "缺少商品ID")
	private Long id;			// 商品ID
}
