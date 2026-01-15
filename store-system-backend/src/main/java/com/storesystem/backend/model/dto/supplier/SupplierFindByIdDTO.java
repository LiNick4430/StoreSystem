package com.storesystem.backend.model.dto.supplier;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierFindByIdDTO {	// 供應商 基本資料

	@NotNull(message = "缺少供應商ID")
	private Long id;		// 供應商 ID
	
}
