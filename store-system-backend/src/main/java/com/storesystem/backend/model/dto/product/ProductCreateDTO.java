package com.storesystem.backend.model.dto.product;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateDTO {	// 建立 商品資料
	
	private String barcode;		// 商品條碼
	private String name;		// 商品名稱
	private String spec;		// 商品規格
	private BigDecimal price;	// 商品售價
	
}
