package com.storesystem.backend.model.dto.inventoryLog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InventoryLogDTO {

	private Long inventoryLogId;	// 入庫 ID
	
	private Long purchaseDetailId;
	
	private Long productId;
	
	private Integer quantity;
	
	private Integer beforeQuantity;
	
	private Integer afterQuantity;
	
	private String warehouseName;
}
