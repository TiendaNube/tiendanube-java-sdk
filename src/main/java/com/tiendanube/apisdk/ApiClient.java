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

	public ApiClient(ApiCredentials apiCredentials, String appName,
			String contactEmail) {
		this.apiCredentials = apiCredentials;
		this.userAgent = appName + " (" + contactEmail + ")";
	}

	public ApiClient(ApiCredentials apiCredentials) {
		this(apiCredentials, "", "");
	}

	public InternalApiResponse get(String url, Map<String, String> params)
			throws ApiException {

		String query = parametersToQuery(params);

		try {
			return internalAuthenticatedHttpRequest(
					"https://api.tiendanube.com/v1/"
							+ apiCredentials.getStoreId() + "/" + url + "?"
							+ query, null, "GET");

		} catch (IOException e) {
			throw new ApiException(
					"Problem while trying to GET the URL " + url, e);
		}

	}

	public InternalApiResponse post(String url, JSONObject object)
			throws ApiException, ApiException {
		try {
			return internalAuthenticatedHttpRequest(url, object.toString(),
					"POST");
		} catch (IOException e) {
			throw new ApiException("Problem while trying to POST the URL "
					+ url, e);
		}
	}

	public InternalApiResponse put(String url, JSONObject object)
			throws ApiException, ApiException {
		try {
			return internalAuthenticatedHttpRequest(url, object.toString(),
					"PUT");
		} catch (IOException e) {
			throw new ApiException(
					"Problem while trying to PUT the URL " + url, e);
		}
	}

	public InternalApiResponse delete(String url) throws ApiException,
			ApiException {
		try {
			return internalAuthenticatedHttpRequest(url, "", "DELETE");
		} catch (IOException e) {
			throw new ApiException("Problem while trying to DELETE the URL "
					+ url, e);
		}
	}

	public InternalApiResponse authenticate(String code) throws ApiException {

		Map<String, String> authParameters = new HashMap<String, String>();
		authParameters.put("client_id", apiCredentials.getAppId());
		authParameters.put("client_secret", apiCredentials.getAppSecret());
		authParameters.put("grant_type", "authorization_code");
		authParameters.put("code", code);
		String url = "http://www.tiendanube.com/apps/authorize/token";
		String query = parametersToQuery(authParameters);

		try {

			InternalApiResponse apiResponse = internalAuthenticatedHttpRequest(
					url, query, "POST");

			if (apiResponse.getStatusCode() != HttpStatus.SC_OK) {
				throw new ApiDetailedException(apiResponse);
			}
			
			JSONObject content = new JSONObject(apiResponse.getResponse());
			apiCredentials.setAccessToken(content.getString("access_token"));
			apiCredentials.setStoreId(content.getString("user_id"));

			return apiResponse;

		} catch (IOException e) {
			throw new ApiException("Problem while trying to authenticate with the following credentials: " + apiCredentials.toString());
		} catch (JSONException e) {
			throw new ApiException("Problem while trying to authenticate with the following credentials: " + apiCredentials.toString());
		}

	}

	private InternalApiResponse internalAuthenticatedHttpRequest(String url,
			String body, String HttpRequestType) throws IOException,
			ApiException {
		return internalHttpRequest(url, body, HttpRequestType, true);
	}

	private InternalApiResponse internalHttpRequest(String url, String body,
			String HttpRequestType, Boolean authenticated) throws IOException,
			ApiException {

		HttpURLConnection connection = null;
		OutputStreamWriter writer = null;
		URL urlObject = null;

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

		// System.out.println(connection.getRequestProperty("Authentication"));
		// System.out.println(connection.getRequestProperty("User-Agent"));
		// System.out.println(connection.getRequestProperty("content-type"));
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

}
