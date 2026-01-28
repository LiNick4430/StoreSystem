package com.storesystem.backend.model.dto.productSupplier;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PSUpdateCostDTO {

	@NotNull(message = "缺少商品ID")
	private Long productId;			// 商品ID
	
	@NotNull(message = "缺少供應商ID")
	private Long supplierId;		// 供應商ID
	
	@NotNull(message = "缺少供應商報價")
	@DecimalMin(value = "0.00", inclusive = false, message = "報價 需要 大於 0")
	@Digits(integer = 8, fraction = 2, message = "報價 小數前最多 8 位, 小數後最多 2 位")
	private BigDecimal defaultCost;	// 供應商 報價
	
}
