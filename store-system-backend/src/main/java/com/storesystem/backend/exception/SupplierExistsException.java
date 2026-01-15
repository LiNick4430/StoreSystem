package com.storesystem.backend.exception;

public class SupplierExistsException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public SupplierExistsException(String message) {
		super(message);
	}
}
