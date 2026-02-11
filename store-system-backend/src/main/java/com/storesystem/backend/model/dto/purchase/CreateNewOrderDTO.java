package com.storesystem.backend.model.dto.purchase;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateNewOrderDTO {

	@NotNull(message = "供應商不能為空")
	private Long supplierId;	// 供應商 ID

	@NotNull(message = "進貨日期不能為空")
	private LocalDate date;		// 進貨日期
	
	@NotEmpty(message = "進貨明細至少需要一筆")
	private Set<CreateNewDetialDTO> details;	// 對應的 明細

}
