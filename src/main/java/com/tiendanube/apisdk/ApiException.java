package com.tiendanube.apisdk;

/**
 * An exception thrown by the Tienda Nube API SDK.
 * 
 * @author mcolotto
 */
public class ApiException extends Exception {

	private static final long serialVersionUID = -4155117290800209275L;

	public ApiException() {
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
