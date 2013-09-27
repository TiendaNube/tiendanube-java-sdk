package com.tiendanube.apisdk;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents credentials to use for the API. The credentials required are the
 * store id and the access token. You may create a new ApiCredentials object
 * using these data if you know it, or obtain them from an authentication code
 * by using the method {@link ApiCredentials.obtain}.
 * 
 * @author mcolotto
 * 
 */
public class ApiCredentials implements Serializable {

	private static final long serialVersionUID = -4582888512969818949L;
	private String storeId;
	private String accessToken;

	/**
	 * Create a new ApiCredentials object, from a store id and an access token.
	 * 
	 * @param storeId
	 *            The store id
	 * @param accessToken
	 *            The access token
	 */
	public ApiCredentials(String storeId, String accessToken) {
		this.storeId = storeId;
		this.accessToken = accessToken;
	}

	/**
	 * Obtains ApiCredentials using an OAuth 2 authorization code, which is sent
	 * as the {@code code} parameter to your application's {@code redirect_uri}.
	 * 
	 * @param authorizationCode
	 *            The authorization code from the API.
	 * @param appId
	 *            The OAuth 2 client id of your application.
	 * @param appSecret
	 *            The OAuth 2 client secret of your application.
	 * @return The ApiCredentials obtained with the given code.
	 */

	public static ApiCredentials obtain(String authorizationCode,
			String appId, String appSecret) throws ApiException {

		String authenticationURL = "http://www.tiendanube.com/apps/authorize/token";
		URL url = null;
		HttpURLConnection connection = null;
		OutputStreamWriter writer = null;

		try {
			
			//create connection object and set parameters
			url = new URL(authenticationURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			String parameters = "client_id=" + appId + "&client_secret="
					+ appSecret + "&grant_type=authorization_code&code="
					+ authorizationCode;
			connection.setDoOutput(true);
			
			//start the post
			writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(parameters);
			writer.flush();

			//translate java response into our type of response
			InternalApiResponse apiResponse = new InternalApiResponse(connection);

			if (apiResponse.getStatusCode() != HttpStatus.SC_OK) {
				throw new ApiDetailedException(apiResponse);
			}

			//return access credentials
			
			System.out.println(apiResponse.getResponse());
			
			JSONObject content = new JSONObject(apiResponse.getResponse());
			String accessToken = content.getString("access_token");
			String storeId = content.getString("user_id");

			return new ApiCredentials(storeId, accessToken);
			
			
		} catch (MalformedURLException e) {
			throw new ApiException("Obtained malformed URL exception when trying to "
					+ connection.getRequestMethod() + " the URL "
					+ url.toString(), e);
		} catch (IOException e) {
			throw new ApiException("Problem while trying to "
					+ connection.getRequestMethod() + " the URL "
					+ url.toString(), e);
		} catch (JSONException e) {
			throw new ApiException(
					"Obtained invalid JSON from API when trying to "
							+ connection.getRequestMethod() + " the URL "
							+ url.toString(), e);
		}finally{
			try {
				if(writer!=null) {
					writer.close();
				}
			} catch (IOException e) {
				//el peor dia de mi vida si pasa esto
				throw new ApiException(e);
			}
		}
		

	}

	public String getStoreId() {
		return storeId;
	}

	public String getAccessToken() {
		return accessToken;
	}

}
