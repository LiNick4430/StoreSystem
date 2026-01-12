package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.storesystem.backend.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 處理 使用 ENUM 卻 找不到 code 的 異常 (401)
	@ExceptionHandler(EnumNotFoundException.class) 	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)     		// 401
	public ApiResponse<?> handleEnumNotFoundException(EnumNotFoundException ex) {
		int statusCode = HttpStatus.UNAUTHORIZED.value();
		ErrorCode errorCode = ErrorCode.ENUM_NOT_FOUND;
		return ApiResponse.error(statusCode, ex.getMessage(), errorCode);
	}
	
}
