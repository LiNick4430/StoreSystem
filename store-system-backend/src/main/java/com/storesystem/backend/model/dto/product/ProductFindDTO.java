package com.storesystem.backend.model.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductFindDTO {	// 搜尋 特定商品
	
	private Long id;			// 商品ID
	private String barcode;		// 商品條碼
}
