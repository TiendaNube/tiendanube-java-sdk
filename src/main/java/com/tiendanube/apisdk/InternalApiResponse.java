package com.tiendanube.apisdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.google.common.collect.Maps;

/**
 * Represents a response from the API.
 * 
 * @author mcolotto
 */
class InternalApiResponse {
	
	private int statusCode;
	private String response;
	private Map<String, String> headers;

	InternalApiResponse(HttpResponse response) throws ApiException {
		InputStream stream = null;

		try {
			HttpEntity entity = response.getEntity();
			stream = entity.getContent();
			StringBuffer responseBuffer = new StringBuffer();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
			String line;
			while((line = bufferedReader.readLine()) != null) {
				responseBuffer.append(line);
				responseBuffer.append('\r');
			}
			
			this.response = responseBuffer.toString();
			this.statusCode = response.getStatusLine().getStatusCode();
		
			this.headers = Maps.newHashMap();
			for(Header h : response.getAllHeaders()) {
				this.headers.put(h.getName(), h.getValue());
			}
			
		} catch(IOException e) {
			// If this happens, it's probably a connection error
			throw new ApiException(e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				// We are doomed.
				throw new ApiException(e);
			}
		}
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getResponse() {
		return response;
	}
	
	public String getHeader(String header) {
		if(headers.containsKey(header)) {
			return headers.get(header);
		}
		return null;
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}
	
}
