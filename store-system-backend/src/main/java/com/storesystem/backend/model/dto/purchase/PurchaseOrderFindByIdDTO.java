package com.storesystem.backend.model.dto.purchase;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderFindByIdDTO {

	@NotNull(message = "缺少訂貨單ID")
	private Long id;			// 進貨單 ID
}
