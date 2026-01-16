package com.storesystem.backend.model.dto.supplier;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** SupplierService 專用中間 DTO*/
@Getter
@Setter
@NoArgsConstructor
public class SupplierSearchDTO {

	private Long supplierId;			// 供應商 ID
	private String supplierTaxId;		// 供應商 統編
	
	public static SupplierSearchDTO from(SupplierFindByIdDTO dto) {
		SupplierSearchDTO search = new SupplierSearchDTO();
		search.setSupplierId(dto.getId());
		return search;
	}
	
	public static SupplierSearchDTO from(SupplierFindByTaxIdDTO dto) {
		SupplierSearchDTO search = new SupplierSearchDTO();
		search.setSupplierTaxId(dto.getTaxId());
		return search;
	}
}
