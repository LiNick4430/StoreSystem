package com.storesystem.backend.model.dto.productSupplier;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PSFindByPruductId {

	@NotNull(message = "缺少商品ID")
	private Long productId;	// 商品ID
	
	@NotNull(message = "缺少頁碼")
	private Integer page;	// 頁面 
	
	@NotNull(message = "缺少大小")
	private Integer size;	// 一頁 多少
	
}
