package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 供應商(Supplier) 找不到 的 異常 (401)
public class SupplierNotFoundException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public SupplierNotFoundException(String message) {
		super(message,
				HttpStatus.UNAUTHORIZED,
				ErrorCode.SUPPLIER_NOT_FOUND);
	}
}
