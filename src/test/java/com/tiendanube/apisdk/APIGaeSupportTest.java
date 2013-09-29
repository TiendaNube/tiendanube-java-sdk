package com.tiendanube.apisdk;

import java.io.IOException;

import org.json.JSONException;

public class APIGaeSupportTest {

	public static void main(String[] args) throws ApiException, JSONException, IOException {
		
		String appId = "16"; // Replace with your application's client id
		String appSecret = "4Why0fZjPAVKKKLstNe1iYYTXV8lIPBAbPExn3BLCTo8RU8v"; // Replace with your application's client secret
		String code = "b0e187bea8a6f2442fe62be5364479919fd11669";
		
		
		ApiCredentials credentials = new ApiCredentials(appId, appSecret);
		ApiClient apiClient = new ApiClient(credentials);
		apiClient.authenticate(code);
		
		
		System.out.println("The credentials from the store are:\n");
		System.out.println(credentials);
		
		System.out.println("The store id of the store whose code was specified is " + credentials.getStoreId());
		Api api = new Api(credentials, "Java SDK test 2", "matias@tiendanube.com");
		
		ObjectResponse store = api.store();
		
		System.out.println("The store that we got authorization for is " + store.getResult().getJSONObject("name").get("es_AR"));
		
	}
}
