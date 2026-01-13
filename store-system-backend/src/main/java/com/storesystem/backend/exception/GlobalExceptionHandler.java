package com.storesystem.backend.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.storesystem.backend.response.ApiResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 處理 商品(Product) 找不到 的 異常 (401)
	@ExceptionHandler(ProductNotFoundException.class) 	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)     		// 401
	public ApiResponse<?> handleProductNotFoundException(ProductNotFoundException ex) {
		int statusCode = HttpStatus.UNAUTHORIZED.value();
		ErrorCode errorCode = ErrorCode.PRODUCT_NOT_FOUND;
		return ApiResponse.error(statusCode, ex.getMessage(), errorCode);
	}
	
	// 處理 商品(Product) 已經存在 的 異常 (401)
	@ExceptionHandler(ProductExistsException.class) 	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)     		// 401
	public ApiResponse<?> handleProductExistsException(ProductExistsException ex) {
		int statusCode = HttpStatus.UNAUTHORIZED.value();
		ErrorCode errorCode = ErrorCode.PRODUCT_EXISTS;
		return ApiResponse.error(statusCode, ex.getMessage(), errorCode);
	}
	
	// 處理 使用 ENUM 卻 找不到 code 的 異常 (401)
	@ExceptionHandler(EnumNotFoundException.class) 	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)     		// 401
	public ApiResponse<?> handleEnumNotFoundException(EnumNotFoundException ex) {
		int statusCode = HttpStatus.UNAUTHORIZED.value();
		ErrorCode errorCode = ErrorCode.ENUM_NOT_FOUND;
		return ApiResponse.error(statusCode, ex.getMessage(), errorCode);
	}

	// 處理 缺少特定數值 的 異常 (400)
	@ExceptionHandler(ValueMissException.class) 	
	@ResponseStatus(HttpStatus.BAD_REQUEST)     		// 400
	public ApiResponse<?> handleValueMissException(ValueMissException ex) {
		int statusCode = HttpStatus.BAD_REQUEST.value();
		ErrorCode errorCode = ErrorCode.VALUES_MISS;
		return ApiResponse.error(statusCode, ex.getMessage(), errorCode);
	}
	
	// 處理 BEAN VALIDATION 統一處理的 缺少特定數值 的 異常 (400)
	// 針對 @RequestBody @Valid DTO
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)     		// 400
	public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		// 1. 取得所有欄位的錯誤訊息
	    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
	            .map(error -> error.getField() + ": " + error.getDefaultMessage())
	            .collect(Collectors.joining(", "));
		
		int statusCode = HttpStatus.BAD_REQUEST.value();
		ErrorCode errorCode = ErrorCode.VALUES_MISS;
		return ApiResponse.error(statusCode, errorMessage, errorCode);
	}
	
	// 處理 BEAN VALIDATION 統一處理的 缺少特定數值 的 異常 (400)
	// 針對 @Validated @RequestParam / @PathVariable
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)     		// 400
	public ApiResponse<?> handleConstraintViolationException(ConstraintViolationException ex) {
		// 1. 取得所有欄位的錯誤訊息
		String errorMessage = ex.getConstraintViolations().stream()
	            .map(violation -> {
	                // 透過迭代取出路徑的最後一個節點（即參數名）
	                String propertyPath = violation.getPropertyPath().toString();
	                String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
	                return fieldName + ": " + violation.getMessage();
	            })
	            .collect(Collectors.joining(", "));
		
		int statusCode = HttpStatus.BAD_REQUEST.value();
		ErrorCode errorCode = ErrorCode.VALUES_MISS;
		return ApiResponse.error(statusCode, errorMessage, errorCode);
	}
}
