package com.storesystem.backend.exception;

public class TaxIdErrorException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public TaxIdErrorException(String message) {
		super(message);
	}
}
