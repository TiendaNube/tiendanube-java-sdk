package com.tiendanube.apisdk;

import java.io.IOException;
import java.io.Serializable;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents credentials to use for the API.
 * The credentials required are the store id and the access token.
 * You may create a new ApiCredentials object using these data if you know it,
 * or obtain them from an authentication code by using the method
 * {@link ApiCredentials.obtain}.
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
	 * @param storeId The store id
	 * @param accessToken The access token
	 */
	public ApiCredentials(String storeId, String accessToken) {
		this.storeId = storeId;
		this.accessToken = accessToken;
	}

	/**
	 * Obtains ApiCredentials using an OAuth 2 authorization code,
	 * which is sent as the {@code code} parameter to your application's
	 * {@code redirect_uri}.
	 * @param authorizationCode The authorization code from the API.
	 * @param appId The OAuth 2 client id of your application.
	 * @param appSecret The OAuth 2 client secret of your application.
	 * @return The ApiCredentials obtained with the given code.
	 */
	public static ApiCredentials obtain(String authorizationCode, String appId, String appSecret) throws ApiException {
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost("http://www.tiendanube.com/apps/authorize/token");
		
		try {
			StringEntity entity = new StringEntity("client_id=" + appId + 
					"&client_secret=" + appSecret + "&grant_type=authorization_code&code=" + authorizationCode,
					ContentType.APPLICATION_FORM_URLENCODED);
			request.setEntity(entity);
		
			HttpResponse response = client.execute(request);
			InternalApiResponse apiResponse = new InternalApiResponse(response);
			
			if(apiResponse.getStatusCode() != HttpStatus.SC_OK) {
				throw new ApiDetailedException(apiResponse);
			}
			
			JSONObject content = new JSONObject(apiResponse.getResponse());
			
			String accessToken = content.getString("access_token");
			String storeId = content.getString("user_id");
			
			return new ApiCredentials(storeId, accessToken);
			
		} catch(IOException e) {
			throw new ApiException("Problem while trying to " + request.getMethod() + 
									" the URL " + request.getURI().toString(), e);
		} catch(JSONException e) {
			throw new ApiException("Obtained invalid JSON from API when trying to " + request.getMethod() + 
					" the URL " + request.getURI().toString(), e);
		}
	}
	
	public String getStoreId() {
		return storeId;
	}

	public String getAccessToken() {
		return accessToken;
	}
	

}
