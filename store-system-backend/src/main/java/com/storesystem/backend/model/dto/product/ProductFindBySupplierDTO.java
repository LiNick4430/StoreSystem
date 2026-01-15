package com.storesystem.backend.model.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductFindBySupplierDTO {	// 供應商 提供的商品
	
	@NotNull(message = "缺少供應商ID")
	private Long supplierId;		// 供應商 ID
	
	@NotNull(message = "缺少頁碼")
	private Integer page;	// 頁面 
	
	@NotNull(message = "缺少大小")
	private Integer size;	// 一頁 
	
}
