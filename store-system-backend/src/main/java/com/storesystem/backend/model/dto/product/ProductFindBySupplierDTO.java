package com.storesystem.backend.model.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductFindBySupplierDTO {	// 供應商 提供的商品
	
	private Long supplierId;		// 供應商 ID
	
	private Integer page;	// 頁面 起始 商品ID
	private Integer size;	// 一頁 多少 商品
	
}
