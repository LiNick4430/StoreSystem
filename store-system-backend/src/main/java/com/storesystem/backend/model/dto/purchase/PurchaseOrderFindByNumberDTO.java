package com.storesystem.backend.model.dto.purchase;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderFindByNumberDTO {

	@NotBlank(message = "缺少訂貨單單號")
	private String number;		// 進貨 單號
}
