package com.storesystem.backend.model.dto.product;

import com.storesystem.backend.model.dto.FindAllDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** ProductService 專用中間 DTO*/
@Getter
@Setter
@NoArgsConstructor
public class ProductSearchAllDTO {

	private String productName;	// 商品名稱
	
	private Long supplierId;	// 供應商ID
	
	// 基本資料
	private Integer page;		// 頁面 
	private Integer size;		// 一頁
	
	public static ProductSearchAllDTO from(FindAllDTO dto) {
		ProductSearchAllDTO search = new ProductSearchAllDTO();
		search.setPage(dto.getPage());
		search.setSize(dto.getSize());
		return search;
	}
	
	public static ProductSearchAllDTO from(ProductFindByNameDTO dto) {
		ProductSearchAllDTO search = new ProductSearchAllDTO();
		search.setProductName(dto.getName());
		search.setPage(dto.getPage());
		search.setSize(dto.getSize());
		return search;
	}
	
	public static ProductSearchAllDTO from(ProductFindBySupplierDTO dto) {
		ProductSearchAllDTO search = new ProductSearchAllDTO();
		search.setSupplierId(dto.getSupplierId());
		search.setPage(dto.getPage());
		search.setSize(dto.getSize());
		return search;
	}
}
