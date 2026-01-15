package com.storesystem.backend.util;

import com.storesystem.backend.annotation.TaiwanTaxId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/** 用於 BEAN VALIDATION 自定註解 的 方法 */
public class TaiwanTaxIdValidatorImpl implements ConstraintValidator<TaiwanTaxId, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		// 給 @NotBlank 控管 回傳 true 防止重複報錯
		if (value == null || value.isBlank()) {
			return true;
		}
		
		return TaiwanTaxIdValidator.isValid(value);
	}

}
