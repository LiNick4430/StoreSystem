package com.storesystem.backend.model.dto.purchase;

import java.time.YearMonth;

import com.storesystem.backend.model.enums.PurchaseStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderSearchAllDTO {

	@NotNull(message = "缺少供應商ID")
	private Long supplierId;			// 供應商ID(必選)
	
	private PurchaseStatus status;		// 進貨單狀態(可選)
	
	private YearMonth orderYearMonth;	// 訂單的年月(可選)
	
	private Long productId;				// 商品ID(可選)
	
	@NotNull(message = "缺少頁碼")
	private Integer page;	// 頁面 
	
	@NotNull(message = "缺少大小")
	private Integer size;	// 一頁 多少
	
}
