package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 頁碼規格 錯誤 的 異常 (400)
public class PageErrorException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public PageErrorException(String message) {
		super(message,
				HttpStatus.BAD_REQUEST,
				ErrorCode.PAGE_ERROR);
	}
}
