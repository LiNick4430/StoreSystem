package com.storesystem.backend.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.storesystem.backend.util.TaiwanTaxIdValidatorImpl;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = TaiwanTaxIdValidatorImpl.class)	// 指定驗證類別
@Target({ElementType.FIELD, ElementType.PARAMETER})			// 掛在 參數 或 欄位身上
@Retention(RetentionPolicy.RUNTIME)
public @interface TaiwanTaxId {

	// 預設錯誤訊息 被 GlobalExceptionHandler 捕獲
	String message() default "統一編號規格或檢查碼不正確";
	
	Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
