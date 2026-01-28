package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 使用 ENUM 卻 找不到 code 的 異常 (401)
public class EnumNotFoundException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public EnumNotFoundException(String message) {
		super(message,
				HttpStatus.UNAUTHORIZED,
				ErrorCode.PRODUCT_NOT_FOUND);
	}
}
