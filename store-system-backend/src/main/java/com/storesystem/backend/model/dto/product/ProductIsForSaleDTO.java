package com.storesystem.backend.model.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductIsForSaleDTO {	// 控制 商品 是否銷售
	
	private Long id;			// 商品ID
	
	private boolean isForSale;	// 商品是否販賣
}
