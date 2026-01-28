package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 供應商(Supplier) 已經存在 的 異常 (401)
public class SupplierExistsException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public SupplierExistsException(String message) {
		super(message,
				HttpStatus.UNAUTHORIZED,
				ErrorCode.SUPPLIER_EXISTS);
	}
}
