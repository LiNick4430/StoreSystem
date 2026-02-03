package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 供應商(Supplier) 存在 關聯的商品 的 異常 (400)
public class SupplierHasProductException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public SupplierHasProductException(String message) {
		super(message,
				HttpStatus.BAD_REQUEST,
				ErrorCode.SUPPLIER_EXISTS);
	}
}
