package com.storesystem.backend.model.dto.purchase;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateNewDetialDTO {

	@NotNull(message = "商品 ID 不能為空")
	private Long productId;			// 商品 ID
	
	@DecimalMin(value = "0.0", inclusive = false, message = "進貨價格必須大於 0")
	private BigDecimal cost;		// 進貨 價格
	
	@Min(value = 1, message = "進貨數量至少為 1")
	private Integer quantity;		// 進貨 數量
	
}
