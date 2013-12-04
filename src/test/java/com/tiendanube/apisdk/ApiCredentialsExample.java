package com.tiendanube.apisdk;

import org.json.JSONException;

public class ApiCredentialsExample {

	public static void main(String[] args) throws ApiException, JSONException {
		
		
		String appId = "16"; // Replace with your application's client id
		String appSecret = "4Why0fZjPAVKKKLstNe1iYYTXV8lIPBAbPExn3BLCTo8RU8v"; // Replace with your application's client secret
		String code = "41799433022060c3ee26333b1a8b2733bff0ece3";
		
		ApiCredentials credentials = ApiCredentials.prepareCredentials(appId, appSecret);
		Api api = new Api(credentials);
		api.authenticate(code);
		
		
		System.out.println("The store id of the store whose code was specified is " + credentials.getStoreId());
		System.out.println(credentials);
		
		ObjectResponse store = api.store();
		System.out.println("The store that we got authorization for is " + store.getResult().getJSONObject("name"));
		
	}
}
