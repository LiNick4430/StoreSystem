package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 自定義 基本錯誤 (和 GlobalExceptionHandler 連動)
public abstract class BaseException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final HttpStatus status;
	private final ErrorCode errorCode;
	
	protected BaseException(String message, HttpStatus status, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
		this.status = status;
	}
	
	public HttpStatus getStatus() { return status; }
	public ErrorCode getErrorCode() { return errorCode; }

}
