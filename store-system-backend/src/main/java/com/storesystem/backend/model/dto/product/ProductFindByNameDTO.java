package com.storesystem.backend.model.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductFindByNameDTO {	// 搜尋 全部商品 + 頁面
	
	@NotBlank(message = "缺少商品名稱")
	private String name;	// 商品 名稱
	
	@NotNull(message = "缺少頁碼")
	private Integer page;	// 頁面 起始 商品ID
	
	@NotNull(message = "缺少大小")
	private Integer size;	// 一頁 多少 商品
	
}
