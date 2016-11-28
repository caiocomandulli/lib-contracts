package com.comandulli.lib.sqlite.exception;

/**
 * Thrown when the object provided has no contract attributed to its type.
 */
public class NoContractException extends RuntimeException {

	private static final long serialVersionUID = 4744742573579560498L;

	public NoContractException() {
		super();
	}

	public NoContractException(String message) {
		super(message);
	}

	public NoContractException(Throwable throwable) {
		super(throwable);
	}

	public NoContractException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
