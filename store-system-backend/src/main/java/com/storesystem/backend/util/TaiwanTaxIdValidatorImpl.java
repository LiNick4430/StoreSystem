package com.storesystem.backend.util;

import com.storesystem.backend.annotation.TaiwanTaxId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TaiwanTaxIdValidatorImpl implements ConstraintValidator<TaiwanTaxId, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		// 如果是 null 交給 @NotBlank 控管 回傳 true 防止重複報錯
		if (value == null || value.isBlank()) {
			return true;
		}
		
		return TaiwanTaxIdValidator.isValid(value);
	}

}
