package com.tiendanube.apisdk;

import java.io.IOException;

import org.json.JSONException;

public class APIGaeSupportTest {

	public static void main(String[] args) throws ApiException, JSONException, IOException {
		
		String appId = "16"; // Replace with your application's client id
		String appSecret = "4Why0fZjPAVKKKLstNe1iYYTXV8lIPBAbPExn3BLCTo8RU8v"; // Replace with your application's client secret
		String code = "006dd12ac50f99e751fe7b5442cc42e31724c75c";
		
		
		ApiCredentials credentials = ApiCredentials.obtain(code, appId, appSecret);
		if(credentials == null){
			return;
		}
		
		System.out.println("The store id of the store whose code was specified is " + credentials.getStoreId());
		Api api = new Api(credentials, "Java SDK test 2", "matias@tiendanube.com");
		
		ObjectResponse store = api.store();
		
		System.out.println("The store that we got authorization for is " + store.getResult().getJSONObject("name").get("es_AR"));
		
	}
}
