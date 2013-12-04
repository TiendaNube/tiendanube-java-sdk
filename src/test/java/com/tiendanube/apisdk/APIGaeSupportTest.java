package com.tiendanube.apisdk;

import java.io.IOException;

import org.json.JSONException;

public class APIGaeSupportTest {

	public static void main(String[] args) throws ApiException, JSONException, IOException {
		
		String appId = "16"; // Replace with your application's client id
		String appSecret = "4Why0fZjPAVKKKLstNe1iYYTXV8lIPBAbPExn3BLCTo8RU8v"; // Replace with your application's client secret
		String code = "d9a26e85232e3b91a89b1de52ba3aec1a7650e69";
		
		
		//authenticate API
		ApiCredentials credentials = ApiCredentials.prepareCredentials(appId, appSecret);
		Api api = new Api(credentials);
		api.authenticate(code);
		
		//print auth info
		System.out.println("The credentials from the store are:\n");
		System.out.println(credentials);
		System.out.println("The store id of the store whose code was specified is " + credentials.getStoreId());
		
	}
}
