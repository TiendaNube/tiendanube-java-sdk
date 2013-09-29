package com.tiendanube.apisdk;

import org.json.JSONException;

public class ApiCredentialsExample {

	public static void main(String[] args) throws ApiException, JSONException {
		
		
		String appId = "16"; // Replace with your application's client id
		String appSecret = "4Why0fZjPAVKKKLstNe1iYYTXV8lIPBAbPExn3BLCTo8RU8v"; // Replace with your application's client secret
		String code = "f8293eeb5fb6dbb054f4e56f5cefc99bd00f05c0";
		
		ApiCredentials credentials = ApiCredentials.prepareCredentials(appId, appSecret);
		Api api = new Api(credentials);
		api.authenticate(code);
		
		
		System.out.println("The store id of the store whose code was specified is " + credentials.getStoreId());
		
		ObjectResponse store = api.store();
		System.out.println("The store that we got authorization for is " + store.getResult().getJSONObject("name").get("es_AR"));
	}
}
