package com.storesystem.backend.model.dto.productSupplier;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PSFindBySupplierId {

	@NotNull(message = "缺少供應商ID")
	private Long supplierId;		// 供應商ID
	
	@NotNull(message = "缺少頁碼")
	private Integer page;	// 頁面 
	
	@NotNull(message = "缺少大小")
	private Integer size;	// 一頁 多少
	
}
