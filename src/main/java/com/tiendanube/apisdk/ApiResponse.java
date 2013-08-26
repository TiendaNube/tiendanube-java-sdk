package com.tiendanube.apisdk;

import java.util.Map;

/**
 * A generic response from the API.
 * Contains the headers.
 * 
 * @author mcolotto
 */
public class ApiResponse {

	private Map<String, String> responseHeaders;
	
	ApiResponse(Map<String, String> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public String getResponseHeader(String header) {
		if(responseHeaders.containsKey(header)) {
			return responseHeaders.get(header);
		}
		return null;
	}
	
}
