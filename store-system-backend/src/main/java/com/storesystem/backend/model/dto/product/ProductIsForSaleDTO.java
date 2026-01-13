package com.storesystem.backend.model.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductIsForSaleDTO {	// 控制 商品 是否銷售
	
	@NotNull(message = "缺少商品ID")
	private Long id;			// 商品ID
	
	@NotNull(message = "缺少銷售狀態設定內容")
	private Boolean isForSale;	// 商品是否販賣
}
