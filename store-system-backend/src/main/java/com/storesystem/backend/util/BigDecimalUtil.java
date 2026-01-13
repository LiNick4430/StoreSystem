package com.storesystem.backend.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.storesystem.backend.exception.ValueMissException;

@Component
public class BigDecimalUtil {

	/** 檢查輸入的 價格(售價) 是否 符合要求 */
	public static void validatePrice(BigDecimal price) {
		if (price == null) {
			throw new ValueMissException("請輸入商品售價");
		}
		
		if (price.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValueMissException("商品售價 不可以小於 0");
		}
		
		if (price.scale() > 2) {
			throw new ValueMissException("商品售價最多 2 位小數");
		}
	}
	
}
