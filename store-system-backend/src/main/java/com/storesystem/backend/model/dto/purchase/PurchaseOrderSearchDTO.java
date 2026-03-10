package com.storesystem.backend.model.dto.purchase;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** PurchaseService 專用中間 DTO */
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderSearchDTO {

	private Long id;			// 進貨單 ID
	private String number;		// 進貨 單號
	
	public static PurchaseOrderSearchDTO from(PurchaseOrderFindByIdDTO dto) {
		PurchaseOrderSearchDTO search = new PurchaseOrderSearchDTO();
		search.setId(dto.getId());
		return search;
	}
	
	public static PurchaseOrderSearchDTO from(PurchaseOrderFindByNumberDTO dto) {
		PurchaseOrderSearchDTO search = new PurchaseOrderSearchDTO();
		search.setNumber(dto.getNumber());
		return search;
	}
}
