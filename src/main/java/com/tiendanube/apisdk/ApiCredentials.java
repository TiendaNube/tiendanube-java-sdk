package com.tiendanube.apisdk;

import java.io.Serializable;

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
	private String appId;
	private String appSecret;

	/**
	 * Factory method to get an instance of API Credentials that will be used of
	 * authentication
	 */
	public static ApiCredentials prepareCredentials(String appId,
			String appSecret) {
		ApiCredentials credentials = new ApiCredentials();
		credentials.appId = appId;
		credentials.appSecret = appSecret;
		return credentials;
	}

	public ApiCredentials(String storeId, String accessToken) {
		this.storeId = storeId;
		this.accessToken = accessToken;
	}

	private ApiCredentials() {
	}

	@Override
	public String toString() {
		return "AppId: " + appId + "\nAppSecret: " + appSecret + "\nStoreID: "
				+ storeId + "\nAccess Token: " + accessToken;
	}

	public String getStoreId() {
		return storeId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;

	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;

	}

}
