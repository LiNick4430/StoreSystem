package com.storesystem.backend.model.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductFindByBarcodeDTO {	// 搜尋 特定商品
	
	private String barcode;		// 商品條碼
}
