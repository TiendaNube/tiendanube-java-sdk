package com.tiendanube.apisdk;


/**
 * An exception that is launched if the resource does not exist.
 * 
 * @author mcolotto
 */
public class ApiNotFoundException extends ApiDetailedException {
	
	private static final long serialVersionUID = -8532703796171822280L;

	public ApiNotFoundException(InternalApiResponse response) throws ApiException {
		super(response);
	}
	
}
