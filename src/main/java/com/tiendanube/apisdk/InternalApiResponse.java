package com.tiendanube.apisdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a response from the API.
 * 
 * @author mcolotto
 */
class InternalApiResponse {

	private int statusCode;
	private String response;
	private Map<String, List<String>> headers;


	public InternalApiResponse(HttpURLConnection connection)
			throws ApiException {

		// start reading the answer
		String auxLine;
		StringBuffer auxResponse = new StringBuffer();
		BufferedReader reader = null;

		try {
			
			// read status code
			this.statusCode = connection.getResponseCode();

			// read all headers
			this.headers = connection.getHeaderFields();

			// read text response
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((auxLine = reader.readLine()) != null) {
				auxResponse.append(auxLine);
			}
			this.response = auxResponse.toString();

		} catch (IOException e) {
			// If this happens, it's probably a connection error
			throw new ApiException(e);
		} finally {
			try {
				if(reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// May day, may day!
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

	public List<String> getHeader(String header) {
		if (headers.containsKey(header)) {
			return headers.get(header);
		}
		return null;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	@Override
	public String toString() {

		StringBuffer buff = new StringBuffer();

		buff.append("Status Code: ");
		buff.append(this.statusCode);
		buff.append("\n\n");
		buff.append("Headers:\n");
		
		if(this.headers != null) {
			Iterator<Entry<String, List<String>>> iterator = this.headers.entrySet()
					.iterator();

			while (iterator.hasNext()) {
				Entry<String, List<String>> next = iterator.next();
				buff.append(next.getKey() + " " + next.getValue());
				buff.append("\n");
			}
			buff.append("\n\n");
		}
		
		buff.append("Response:\n");
		buff.append(this.response);
		
		return buff.toString();

	}

}
