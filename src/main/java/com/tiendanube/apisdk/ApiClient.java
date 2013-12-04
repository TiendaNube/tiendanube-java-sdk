package com.tiendanube.apisdk;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.JSONException;
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
	private static final String API_URL = "https://api.tiendanube.com/v1/";
	private static final String API_AUTH_URL = "http://www.tiendanube.com/apps/authorize/token";

	ApiClient(ApiCredentials apiCredentials, String appName, String contactEmail) {
		this.apiCredentials = apiCredentials;
		this.userAgent = appName + " (" + contactEmail + ")";
	}

	ApiClient(ApiCredentials apiCredentials) {
		this(apiCredentials, "", "");
	}

	// Below get, post, put, delete and authenticate

	public InternalApiResponse get(String url, Map<String, String> params)
			throws ApiException {
		String query = parametersToQuery(params);
		return internalAuthenticatedHttpRequest(prepareApiUrl(url, query),
				null, "GET");
	}

	public InternalApiResponse post(String url, JSONObject object)
			throws ApiException, ApiException {
		return internalAuthenticatedHttpRequest(prepareApiUrl(url), object.toString(), "POST");
	}

	public InternalApiResponse put(String url, JSONObject object)
			throws ApiException, ApiException {
		return internalAuthenticatedHttpRequest(prepareApiUrl(url), object.toString(), "PUT");

	}

	public InternalApiResponse delete(String url) throws ApiException,
			ApiException {
		return internalAuthenticatedHttpRequest(prepareApiUrl(url), "", "DELETE");
	}

	public InternalApiResponse authenticate(String code) throws ApiException {

		
		Map<String, String> authParameters = prepareAuthenticationParameters(code);
		String query = parametersToQuery(authParameters);

		InternalApiResponse apiResponse = internalHttpRequest(API_AUTH_URL, query,
				"POST", false);

		if (apiResponse.getStatusCode() != HttpStatus.SC_OK) {
			throw new ApiDetailedException(apiResponse);
		}

		JSONObject content;
		try {
			content = new JSONObject(apiResponse.getResponse());
			apiCredentials.setAccessToken(content.getString("access_token"));
			apiCredentials.setStoreId(content.getString("user_id"));
			return apiResponse;

		} catch (JSONException e) {
			throw new ApiException(
					"Problem while trying to authenticate with credentials: "
							+ apiCredentials.toString());
		}

	}

	private InternalApiResponse internalAuthenticatedHttpRequest(String url,
			String body, String HttpRequestType) throws ApiException {
		return internalHttpRequest(url, body, HttpRequestType, true);
	}

	private InternalApiResponse internalHttpRequest(String url, String body,
			String HttpRequestType, Boolean authenticated) throws ApiException {

		HttpURLConnection connection = null;
		OutputStreamWriter writer = null;
		URL urlObject = null;

		try {

			// set connection properties
			urlObject = new URL(url);
			connection = (HttpURLConnection) urlObject.openConnection();
			connection.setRequestMethod(HttpRequestType);
			connection.setDoOutput(true);
			connection.setDoInput(true);

			// if we are doing an authenticated request, then set credentials
			if (authenticated) {
				connection.setRequestProperty("Authentication", "bearer "
						+ apiCredentials.getAccessToken());
				connection.setRequestProperty("User-Agent", userAgent);
				connection.setRequestProperty("Content-Type",
						"application/json; charset=utf-8");
			}

			// if there's a body to write, do it
			if (body != null && !body.isEmpty()) {
				writer = new OutputStreamWriter(connection.getOutputStream());
				writer.write(body);
				writer.flush();
			}

		} catch (IOException e) {
			throw new ApiException("Problem while trying to " + HttpRequestType
					+ " the URL " + url, e);
		}

		return new InternalApiResponse(connection);

	}

	private String parametersToQuery(Map<String, String> params) {
		String query = "";
		for (String key : params.keySet()) {
			String value = params.get(key);
			query = query + key + "=" + value + "&";
		}
		return query;
	}

	private Map<String, String> prepareAuthenticationParameters(String code) {
		HashMap<String, String> authParameters = new HashMap<String, String>();
		authParameters.put("client_id", apiCredentials.getAppId());
		authParameters.put("client_secret", apiCredentials.getAppSecret());
		authParameters.put("grant_type", "authorization_code");
		authParameters.put("code", code);
		return authParameters;
	}

	private String prepareApiUrl(String url, String query) {
		return API_URL + apiCredentials.getStoreId() + "/" + url + "?" + query;
	}

	private String prepareApiUrl(String url) {
		return API_URL + apiCredentials.getStoreId() + "/" + url;
	}

}
