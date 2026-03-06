package com.storesystem.backend.model.dto.purchase;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateOrderDTO {

	@NotNull(message = "訂單 ID 不能為空")
	private Long orderId;
	
	@NotEmpty(message = "進貨明細至少需要一筆")
	private Set<UpdateDetialDTO> details;	// 對應的 明細

}
