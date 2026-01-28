package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 商品(Product) 存在 關聯的供應商 的 異常 (400)
public class ProductHasSupplierException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public ProductHasSupplierException(String message) {
		super(message,
				HttpStatus.BAD_REQUEST,
				ErrorCode.PRODUCT_EXISTS);
	}
}
