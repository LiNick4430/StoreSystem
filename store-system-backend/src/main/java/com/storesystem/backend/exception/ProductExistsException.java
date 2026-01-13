package com.storesystem.backend.exception;

public class ProductExistsException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public ProductExistsException(String message) {
		super(message);
	}
}
