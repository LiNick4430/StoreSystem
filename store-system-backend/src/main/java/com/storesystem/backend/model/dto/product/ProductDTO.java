package com.storesystem.backend.model.dto.product;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {		// 基本商品資料
	private Long id;			// 商品 ID
	private String barcode;		// 商品 條碼
	private String name;		// 商品 名稱
	private String spec;		// 商品 規格
	private BigDecimal price;	// 商品 售價
	private Integer stock;		// 商品 庫存量
	private boolean isForSale;	// 商品 是否正在銷售
}
