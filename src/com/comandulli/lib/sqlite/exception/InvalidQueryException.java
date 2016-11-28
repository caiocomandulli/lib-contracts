package com.comandulli.lib.sqlite.exception;

/**
 * Thrown when the query provided is invalid.
 */
public class InvalidQueryException extends RuntimeException {

	private static final long serialVersionUID = 5503434243276813175L;

	public InvalidQueryException() {
		super();
	}

	public InvalidQueryException(String message) {
		super(message);
	}

	public InvalidQueryException(Throwable throwable) {
		super(throwable);
	}

	public InvalidQueryException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
