package com.comandulli.lib.sqlite.exception;

/**
 * Thrown when the type of object provided does not match the contract type.
 */
public class IncompatibleContractException extends RuntimeException {

	private static final long serialVersionUID = 2931059258037702905L;

	public IncompatibleContractException() {
		super();
	}

	public IncompatibleContractException(String message) {
		super(message);
	}

	public IncompatibleContractException(Throwable throwable) {
		super(throwable);
	}

	public IncompatibleContractException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
