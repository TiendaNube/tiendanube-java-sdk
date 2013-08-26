package com.tiendanube.apisdk;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An exception that arises from an error message thrown by the API.
 * 
 * @author mcolotto
 */
public class ApiDetailedException extends ApiException {
	
	private static final long serialVersionUID = -8532703796171822280L;
	
	private int statusCode;
	private JSONObject errorObject;

	public ApiDetailedException(InternalApiResponse response) throws ApiException {
		try {
			this.statusCode = response.getStatusCode();
			this.errorObject = new JSONObject(response.getResponse());
		} catch(JSONException e) {
			throw new ApiException("JSON error parsing API error message", e);
		}
	}

	public int getStatusCode() {
		return statusCode;
	}

	public JSONObject getErrorObject() {
		return errorObject;
	}
	
}
