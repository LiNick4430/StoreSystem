package com.storesystem.backend.model.dto.productSupplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PSUnlinkAfterDTO {

	private String supplierName;	// 供應商名稱
	
	private String productName;		// 商品名稱
}
