package com.storesystem.backend.model.dto.productSupplier;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PSLinkDTO {

	@NotNull(message = "缺少商品ID")
	private Long productId;			// 商品ID
	
	@NotNull(message = "缺少供應商ID")
	private Long supplierId;		// 供應商ID
	
	@NotNull(message = "缺少供應商報價")
	private BigDecimal defaultCost;	// 供應商 報價
}
