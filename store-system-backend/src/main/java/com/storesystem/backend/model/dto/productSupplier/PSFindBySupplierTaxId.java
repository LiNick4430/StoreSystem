package com.storesystem.backend.model.dto.productSupplier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PSFindBySupplierTaxId {

	@NotBlank(message = "缺少供應商統編")
	private String supplierTaxId;		// 供應商統編
	
	@NotNull(message = "缺少頁碼")
	private Integer page;	// 頁面 
	
	@NotNull(message = "缺少大小")
	private Integer size;	// 一頁 多少
	
}
