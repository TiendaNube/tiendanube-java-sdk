package com.tiendanube.apisdk;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.json.JSONObject;

/**
 * This class is an internal client to interact with the API. Automatically
 * handles converting to/from JSON.
 * 
 * @author mcolotto
 */
class ApiClient {

	private String userAgent;
	private ApiCredentials apiCredentials;

	ApiClient(ApiCredentials apiCredentials, String appName, String contactEmail) {
		this.apiCredentials = apiCredentials;
		this.userAgent = appName + " (" + contactEmail + ")";
	}

	public InternalApiResponse get(String url, Map<String, String> params)
			throws ApiException {

		// prepare parameters
		String query = "";
		for (String key : params.keySet()) {
			String value = params.get(key);
			query = query + key + "=" + value + "&";
		}
		

		try {
			return internalHttpRequest("https://api.tiendanube.com/v1/"
					+ apiCredentials.getStoreId() + "/" + url + "?" + query,
					"", "GET");
			
		} catch (IOException e) {
			throw new ApiException(
					"Problem while trying to GET the URL " + url, e);
		}

	}

	public InternalApiResponse post(String url, JSONObject object)
			throws ApiException, ApiException {
		try {
			return internalHttpRequest(url, object.toString(), "POST");
		} catch (IOException e) {
			throw new ApiException("Problem while trying to POST the URL "
					+ url, e);
		}
	}

	public InternalApiResponse put(String url, JSONObject object)
			throws ApiException, ApiException {
		try {
			return internalHttpRequest(url, object.toString(), "PUT");
		} catch (IOException e) {
			throw new ApiException(
					"Problem while trying to PUT the URL " + url, e);
		}
	}

	public InternalApiResponse delete(String url) throws ApiException,
			ApiException {
		try {
			return internalHttpRequest(url, "", "DELETE");
		} catch (IOException e) {
			throw new ApiException("Problem while trying to DELETE the URL "
					+ url, e);
		}
	}

	private InternalApiResponse internalHttpRequest(String url,
			String body, String HttpRequestType) throws IOException, ApiException {
		
		HttpURLConnection connection = null;
		OutputStreamWriter writer = null;
		URL urlObject = null;

		// set connection properties
		urlObject = new URL(url);
		connection = (HttpURLConnection) urlObject.openConnection();
		connection.setRequestMethod(HttpRequestType);
		connection.setDoOutput(true);
		connection.setRequestProperty("Authentication", "bearer "
				+ apiCredentials.getAccessToken());
		connection.setRequestProperty("User-Agent", userAgent);
		connection.setRequestProperty("content-type","application/json; charset=utf-8");
		
//		System.out.println(connection.getRequestProperty("Authentication"));
//		System.out.println(connection.getRequestProperty("User-Agent"));
//		System.out.println(connection.getRequestProperty("content-type"));
		
		
		// start the post
		writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(body);
		writer.flush();
		return new InternalApiResponse(
				(HttpURLConnection) urlObject.openConnection());

	}

}
