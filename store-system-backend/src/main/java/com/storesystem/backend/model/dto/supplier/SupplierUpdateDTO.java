package com.storesystem.backend.model.dto.supplier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierUpdateDTO {	// 供應商 基本資料

	@NotNull(message = "缺少供應商ID")
	private Long id;		// 供應商 ID
	
	@NotBlank(message = "缺少供應商名稱")
	private String name;	// 供應商 名稱
	
	@NotBlank(message = "缺少供應商地址")
	private String address;	// 供應商 地址
	
	@NotBlank(message = "缺少供應商電話")
	private String phone;	// 供應商 電話
	
}
