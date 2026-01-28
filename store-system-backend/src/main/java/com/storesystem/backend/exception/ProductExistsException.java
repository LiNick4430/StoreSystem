package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 商品(Product) 已經存在 的 異常 (401)
public class ProductExistsException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public ProductExistsException(String message) {
		super(message,
				HttpStatus.UNAUTHORIZED,
				ErrorCode.PRODUCT_EXISTS);
	}
}
