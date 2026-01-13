package com.storesystem.backend.model.dto.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateDTO {	// 建立 商品資料
	
	@NotBlank(message = "缺少商品條碼")
	private String barcode;		// 商品條碼
	
	@NotBlank(message = "缺少商品名稱")
	private String name;		// 商品名稱
	
	@NotBlank(message = "缺少商品規格")
	private String spec;		// 商品規格
	
	@NotNull(message = "缺少商品售價")
	@DecimalMin(value = "0.00", inclusive = false, message = "售價 需要 大於 0")
	@Digits(integer = 8, fraction = 2, message = "售價 小數前最多 8 位, 小數後最多 2 位")
	private BigDecimal price;	// 商品售價
	
}
