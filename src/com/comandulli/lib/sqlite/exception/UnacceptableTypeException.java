package com.comandulli.lib.sqlite.exception;

/**
 * Thrown when the type provided is not supported by SQLite.
 */
public class UnacceptableTypeException extends RuntimeException {

	private static final long serialVersionUID = 6913381687398282020L;

	public UnacceptableTypeException() {
		super();
	}

	public UnacceptableTypeException(String message) {
		super(message);
	}

	public UnacceptableTypeException(Throwable throwable) {
		super(throwable);
	}

	public UnacceptableTypeException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
