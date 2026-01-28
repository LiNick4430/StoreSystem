package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 缺少特定數值 的 異常 (400)
public class ValueMissException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public ValueMissException(String message) {
		super(message,
				HttpStatus.BAD_REQUEST,
				ErrorCode.VALUES_MISS);
	}
}
