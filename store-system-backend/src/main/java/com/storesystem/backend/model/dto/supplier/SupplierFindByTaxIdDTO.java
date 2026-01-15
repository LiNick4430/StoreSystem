package com.storesystem.backend.model.dto.supplier;

import com.storesystem.backend.annotation.TaiwanTaxId;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierFindByTaxIdDTO {	// 供應商 基本資料

	@NotNull(message = "缺少供應商統編")
	@TaiwanTaxId
	private String taxId;		// 供應商 統編
	
}
