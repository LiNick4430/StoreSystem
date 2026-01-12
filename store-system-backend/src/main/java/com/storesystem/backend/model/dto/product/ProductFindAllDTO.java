package com.storesystem.backend.model.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductFindAllDTO {	// 搜尋 全部商品 + 頁面
	
	private Integer page;	// 頁面 起始 商品ID
	private Integer size;	// 一頁 多少 商品
	
}
