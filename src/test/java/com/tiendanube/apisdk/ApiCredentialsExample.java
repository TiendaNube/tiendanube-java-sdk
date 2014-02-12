package com.tiendanube.apisdk;

import org.json.JSONException;

public class ApiCredentialsExample {

	public static void main(String[] args) throws ApiException, JSONException {
		
		
		String appId = ""; // Replace with your application's client id
		String appSecret = ""; // Replace with your application's client secret
		String code = "";
		
		ApiCredentials credentials = ApiCredentials.prepareCredentials(appId, appSecret);
		Api api = new Api(credentials);
		api.authenticate(code);
		
		
		System.out.println("The store id of the store whose code was specified is " + credentials.getStoreId());
		System.out.println(credentials);
		
		ObjectResponse store = api.store();
		System.out.println("The store that we got authorization for is " + store.getResult().getJSONObject("name"));
		
	}
}
