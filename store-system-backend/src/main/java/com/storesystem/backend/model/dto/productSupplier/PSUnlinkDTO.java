package com.storesystem.backend.model.dto.productSupplier;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PSUnlinkDTO {

	@NotNull(message = "缺少商品ID")
	private Long productId;			// 商品ID
	
	@NotNull(message = "缺少供應商ID")
	private Long supplierId;		// 供應商ID
}
