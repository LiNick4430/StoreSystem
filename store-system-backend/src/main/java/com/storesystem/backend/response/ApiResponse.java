package com.storesystem.backend.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storesystem.backend.exception.ErrorCode;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();
	
	// 基本資料
	private int code;				// HTTP 回應碼
	private String message;			// 錯誤說明
	
	// 成功資料
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;					// 回傳資料
	
	// 失敗資料
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ErrorCode errorCode;	// 自訂義系統碼
	
	// 成功 用 建構子
	private ApiResponse(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	// 失敗 用 建構子
	private ApiResponse(int code, String message, ErrorCode errorCode) {
		this.code = code;
		this.message = message;
		this.errorCode = errorCode;
	}
	
	// 成功 用 方法
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<T>(HttpStatus.OK.value(), message, data);
	}
	
	// 失敗 用 方法
	public static <T> ApiResponse<T> error(int code, String message, ErrorCode errorCode) {
		return new ApiResponse<T>(code, message, errorCode);
	}
	
	// 把 API 錯誤回應 轉成 字串 的 方法
	public static String toErrorJsonString(int code, String message, ErrorCode errorCode) {
		try {
			return OBJECTMAPPER.writeValueAsString(new ApiResponse<>(code, message, errorCode));
		} catch (Exception e) {
			return "{\"code\":" + code + 
		               ",\"errorCode\":\"" + errorCode + 
		               "\",\"message\":\"" + message + 
		               "}";
		}
	}
}
