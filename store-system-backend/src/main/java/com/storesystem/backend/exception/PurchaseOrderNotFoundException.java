package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 訂貨單(PurchaseOrder) 找不到 的 異常 (401)
public class PurchaseOrderNotFoundException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public PurchaseOrderNotFoundException(String message) {
		super(message,
				HttpStatus.UNAUTHORIZED,
				ErrorCode.PURCHASE_ORDER_NOT_FOUND);
	}
}
