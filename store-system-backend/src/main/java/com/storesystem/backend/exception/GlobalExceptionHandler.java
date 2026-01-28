package com.storesystem.backend.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.storesystem.backend.response.ApiResponse;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;

@Slf4j					// lombok 支援的 log
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// 負責處理 全部的 自定義 錯誤
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ApiResponse<?>> handleBaseException(BaseException ex) {
		// 包裝 ResponseEntity 回傳
		return ResponseEntity
				// 控制 ResponseStatus
				.status(ex.getStatus())								
				// 放入 ApiResponse
				.body(ApiResponse.error(
						ex.getStatus().value(), 	
						ex.getMessage(), 
						ex.getErrorCode()));
	}
	
	// 非自定義錯誤 -----------------------------------------------------------------------------------------------------
	// 處理 BEAN VALIDATION 統一處理的 缺少特定數值 的 異常 (400)
	// 針對 @RequestBody @Valid DTO
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		// 1. 取得所有欄位的錯誤訊息
	    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
	            .map(error -> error.getField() + ": " + error.getDefaultMessage())
	            .collect(Collectors.joining("; "));
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), errorMessage, ErrorCode.VALID_ERROR));
	}
	
	// 處理 BEAN VALIDATION 統一處理的 缺少特定數值 的 異常 (400)
	// 針對 @Validated @RequestParam / @PathVariable
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
		// 1. 取得所有欄位的錯誤訊息
		String errorMessage = ex.getConstraintViolations().stream()
	            .map(violation -> {
	            	String fieldName = "";
	                for (Path.Node node : violation.getPropertyPath()) {
	                    fieldName = node.getName();
	                }
	                return fieldName + ": " + violation.getMessage();
	            })
	            .collect(Collectors.joining("; "));
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), errorMessage, ErrorCode.VALID_ERROR));
	}
	
	// 其他 非預期性的錯誤 (500)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
		log.error("系統發生非預期錯誤: ", ex);
		
		return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                        "系統發生非預期錯誤，請聯繫管理員", 
                        ErrorCode.SYSTEM_ERROR));
	}
}
