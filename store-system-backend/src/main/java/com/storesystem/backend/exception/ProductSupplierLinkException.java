package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 商品(Product), 供應商(Supplier) 已經關聯 的 異常 (401)
public class ProductSupplierLinkException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public ProductSupplierLinkException(String message) {
		super(message,
				HttpStatus.UNAUTHORIZED,
				ErrorCode.PRODUCT_SUPPLIER_LINK);
	}
}
