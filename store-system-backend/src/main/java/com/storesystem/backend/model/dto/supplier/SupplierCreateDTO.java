package com.storesystem.backend.model.dto.supplier;

import com.storesystem.backend.annotation.TaiwanTaxId;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierCreateDTO {	// 供應商 基本資料

	@NotBlank(message = "缺少供應商名稱")
	private String name;	// 供應商 名稱
	
	@NotBlank(message = "缺少供應商統編")
	@TaiwanTaxId
	private String taxId;	// 供應商 統編
	
	@NotBlank(message = "缺少供應商地址")
	private String address;	// 供應商 地址
	
	@NotBlank(message = "缺少供應商電話")
	private String phone;	// 供應商 電話
	
}
