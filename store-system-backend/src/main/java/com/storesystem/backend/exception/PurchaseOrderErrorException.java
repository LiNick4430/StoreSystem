package com.storesystem.backend.exception;

import org.springframework.http.HttpStatus;

// 處理 訂貨單(PurchaseOrder) 找不到 的 異常 (401)
public class PurchaseOrderErrorException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public PurchaseOrderErrorException(String message) {
		super(message,
				HttpStatus.UNAUTHORIZED,
				ErrorCode.PURCHASE_ORDER_ERROR);
	}
}
