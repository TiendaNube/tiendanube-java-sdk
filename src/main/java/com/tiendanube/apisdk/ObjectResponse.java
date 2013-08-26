package com.tiendanube.apisdk;

import java.util.Map;

import org.json.JSONObject;

/**
 * A response from the API that represents a single object.
 * 
 * @author mcolotto
 */
public class ObjectResponse extends ApiResponse {
	
	private JSONObject result;

	ObjectResponse(JSONObject result, Map<String, String> responseHeaders) {
		super(responseHeaders);
		this.result = result;
	}

	public JSONObject getResult() {
		return result;
	}
	
}
