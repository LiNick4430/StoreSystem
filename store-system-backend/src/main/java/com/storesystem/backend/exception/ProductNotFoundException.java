package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 商品(Product) 找不到 的 異常 (401)
public class ProductNotFoundException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String message) {
		super(message,
				HttpStatus.UNAUTHORIZED,
				ErrorCode.PRODUCT_NOT_FOUND);
	}
}
