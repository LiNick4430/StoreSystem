package com.storesystem.backend.model.dto.supplier;

import com.storesystem.backend.model.dto.FindAllDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** SupplierService 專用中間 DTO*/
@Getter
@Setter
@NoArgsConstructor
public class SupplierSearchAllDTO {

	private Long productId;	// 供應商 ID
	
	// 基本資料
	private Integer page;	// 頁面
	private Integer size;	// 一頁 多少
	
	public static SupplierSearchAllDTO from(FindAllDTO dto) {
		SupplierSearchAllDTO search = new SupplierSearchAllDTO();
		search.setPage(dto.getPage());
		search.setSize(dto.getSize());
		return search;
	}
	
	public static SupplierSearchAllDTO from(SupplierFindByProductDTO dto) {
		SupplierSearchAllDTO search = new SupplierSearchAllDTO();
		search.setProductId(dto.getProductId());
		search.setPage(dto.getPage());
		search.setSize(dto.getSize());
		return search;
	}
}
