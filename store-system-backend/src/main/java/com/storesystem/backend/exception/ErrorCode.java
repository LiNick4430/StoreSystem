package com.storesystem.backend.exception;

//錯誤 回應 的時候 所用的 ErrorCode
public enum ErrorCode {

	ENUM_NOT_FOUND,
	PRODUCT_NOT_FOUND,
	SUPPLIER_NOT_FOUND,
	
	PRODUCT_EXISTS,
	SUPPLIER_EXISTS,
	
	VALUES_MISS,
	
	TAX_ID_ERROR,
	VALID_ERROR,
	
}
