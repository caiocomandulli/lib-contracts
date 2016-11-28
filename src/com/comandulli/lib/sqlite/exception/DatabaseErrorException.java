package com.comandulli.lib.sqlite.exception;

/**
 * Thrown when a database error has ocurred and the query could not be executed.
 */
public class DatabaseErrorException extends RuntimeException {

	private static final long serialVersionUID = -3528054755824619106L;

	public DatabaseErrorException() {
		super();
	}

	public DatabaseErrorException(String message) {
		super(message);
	}

	public DatabaseErrorException(Throwable throwable) {
		super(throwable);
	}

	public DatabaseErrorException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
