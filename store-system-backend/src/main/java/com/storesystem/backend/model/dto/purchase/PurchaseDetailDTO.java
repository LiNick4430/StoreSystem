package com.storesystem.backend.model.dto.purchase;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseDetailDTO {

	private Long detailId;			// 進貨明細 ID
	private Long orderId;			// 進貨單 ID
	
	private Long productId;			// 商品 ID
	
	private BigDecimal cost;		// 進貨 價格
	
	private Integer quantity;		// 進貨 數量
	
	private BigDecimal subtotal;	// 單項 小計
}
