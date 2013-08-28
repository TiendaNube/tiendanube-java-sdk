package com.tiendanube.apisdk;

import org.json.JSONException;

public class ApiCredentialsExample {

	public static void main(String[] args) throws ApiException, JSONException {
		String code = ""; // Replace with the OAuth 2 authentication code
		String appId = ""; // Replace with your application's client id
		String appSecret = ""; // Replace with your application's client secret
		ApiCredentials credentials = ApiCredentials.obtain(code, appId, appSecret);
		System.out.println("The store id of the store whose code was specified is " + credentials.getStoreId());
		Api api = new Api(credentials, "Java SDK test 2", "matias@tiendanube.com");
		ObjectResponse store = api.store();
		System.out.println("The store that we got authorization for is " + store.getResult().getJSONObject("name").get("es_AR"));
	}
}
