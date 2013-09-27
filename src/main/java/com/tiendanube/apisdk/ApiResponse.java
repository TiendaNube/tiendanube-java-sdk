package com.tiendanube.apisdk;

import java.util.List;
import java.util.Map;

/**
 * A generic response from the API.
 * Contains the headers.
 * 
 * @author mcolotto
 */
public class ApiResponse {

	private Map<String, List<String>> responseHeaders;
	
	ApiResponse(Map<String, List<String>> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public List<String> getResponseHeader(String header) {
		if(responseHeaders.containsKey(header)) {
			return responseHeaders.get(header);
		}
		return null;
	}
	
}
