package com.storesystem.backend.model.dto.purchase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderDTO {

	private Long id;		// 進貨單ID
	private String number;	// 進貨單 單號
	
	private Long supplierId;	// 供應商 ID
	private String supplierName;	// 供應商名稱
	
	private LocalDate date;		// 進貨日期
	
	private BigDecimal total;	// 進貨 總金額
	
	private String status;	// 目前進貨狀態
	
	private Set<PurchaseDetailDTO> details;	// 對應的 明細
	
}
