package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 商品(Product), 供應商(Supplier) 關聯 找不到 的 異常 (401)
public class ProductSupplierNotFoundException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public ProductSupplierNotFoundException(String message) {
		super(message,
				HttpStatus.UNAUTHORIZED,
				ErrorCode.PRODUCT_SUPPLIER_NOT_FOUND);
	}
}
