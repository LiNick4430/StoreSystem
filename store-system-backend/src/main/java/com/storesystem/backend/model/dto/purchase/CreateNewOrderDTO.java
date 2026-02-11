package com.storesystem.backend.model.dto.purchase;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateNewOrderDTO {

	private String number;		// 進貨單 單號

	private Long supplierId;	// 供應商 ID

	private LocalDate date;		// 進貨日期
	
	private Set<CreateNewDetialDTO> details;	// 對應的 明細

}
