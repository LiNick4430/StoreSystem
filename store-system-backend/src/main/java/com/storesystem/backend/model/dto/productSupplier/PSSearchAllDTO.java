package com.storesystem.backend.model.dto.productSupplier;

import com.storesystem.backend.model.dto.FindAllDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** ProductSupplierService 專用中間 DTO*/
@Getter
@Setter
@NoArgsConstructor
public class PSSearchAllDTO {

	private Long supplierId;		// 供應商ID
	private String supplierTaxId;	// 供應商統編
	
	private Long productId;			// 商品ID
	private String productName;		// 商品名稱
	private String productBarcode;	// 商品條碼
	
	// 基本需求
	private Integer page;	// 頁面 
	private Integer size;	// 一頁 多少
	
	public static PSSearchAllDTO from(FindAllDTO dto) {
		PSSearchAllDTO search = new PSSearchAllDTO();
		search.setPage(dto.getPage());
		search.setSize(dto.getSize());
		return search;
	}
	
	public static PSSearchAllDTO from(PSFindBySupplierId dto) {
		PSSearchAllDTO search = new PSSearchAllDTO();
		search.setSupplierId(dto.getSupplierId());
		search.setPage(dto.getPage());
		search.setSize(dto.getSize());
		return search;
	}
	
	public static PSSearchAllDTO from(PSFindBySupplierTaxId dto) {
		PSSearchAllDTO search = new PSSearchAllDTO();
		search.setSupplierTaxId(dto.getSupplierTaxId());
		search.setPage(dto.getPage());
		search.setSize(dto.getSize());
		return search;
	}
	
	public static PSSearchAllDTO from(PSFindByPruductId dto) {
		PSSearchAllDTO search = new PSSearchAllDTO();
		search.setProductId(dto.getProductId());
		search.setPage(dto.getPage());
		search.setSize(dto.getSize());
		return search;
	}
	
	public static PSSearchAllDTO from(PSFindByPruductName dto) {
		PSSearchAllDTO search = new PSSearchAllDTO();
		search.setProductName(dto.getProductName());
		search.setPage(dto.getPage());
		search.setSize(dto.getSize());
		return search;
	}
	
	public static PSSearchAllDTO from(PSFindByPruductBarcode dto) {
		PSSearchAllDTO search = new PSSearchAllDTO();
		search.setProductBarcode(dto.getProductBarcode());
		search.setPage(dto.getPage());
		search.setSize(dto.getSize());
		return search;
	}
}
