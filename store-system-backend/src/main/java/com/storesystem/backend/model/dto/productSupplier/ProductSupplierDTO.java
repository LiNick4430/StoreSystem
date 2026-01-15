package com.storesystem.backend.model.dto.productSupplier;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductSupplierDTO {

	private Long productId;			// 商品ID
	private String barcode;			// 商品條碼
	private String productName;		// 商品名稱 
	private String spec;			// 商品規格
	
	private Long supplierId;		// 供應商ID
	private String supplierTaxID;	// 供應商統編
	private String supplierName;	// 供應商名稱
	
	private BigDecimal defaultCost;	// 供應商 報價
	
}
