package com.storesystem.backend.model.dto.purchase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReceivedOrderDTO {

	@NotNull(message = "訂單 ID 不能為空")
	private Long orderId;
	
	@NotBlank(message = "入庫人員 不可為空")
	private String name;	// 簽收入庫 人員
}
