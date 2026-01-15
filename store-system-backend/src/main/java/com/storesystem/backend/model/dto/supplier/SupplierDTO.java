package com.storesystem.backend.model.dto.supplier;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierDTO {	// 供應商 基本資料

	private Long id;		// 供應商 ID
	private String name;	// 供應商 名稱
	private String taxId;	// 供應商 統編
	private String address;	// 供應商 地址
	private String phone;	// 供應商 電話
	
}
