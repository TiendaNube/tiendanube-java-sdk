package com.tiendanube.apisdk;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiExample {

	public static void main(String[] args) throws JSONException {
		try {
			
			String accessToken = ""; // Replace with the access token
			String storeId = ""; // Replace with the store id
			Api api = new Api(new ApiCredentials(storeId, accessToken), "Java SDK test", "matias@tiendanube.com");
			
			
			//get store object
			ObjectResponse store = api.store();
			System.out.println("El dueño de la store es: " + store.getResult().getJSONObject("name").get("es") );
		
			Object mainLanguage = api.store().getResult().getString("main_language");
			System.out.println("El idioma principal de la tiendas es: " + mainLanguage);
			
			
			//create a script json object
			JSONObject newScript = new JSONObject();
			newScript.put("src", "http://backdoor");
			newScript.put("where", "store");
			newScript.put("event", "onload");
			
			//register the script via API, to test the "post" method
			ObjectResponse createdScript = api.create("scripts", newScript);
			
			//get the id of the created object
			int id = createdScript.getResult().getInt("id");
			
			//now try to see if we can access the previously craeted script, to test "get" method
			ObjectResponse gottenScript = api.get("scripts", id);
			System.out.println(gottenScript.getResult().get("src"));
			
			//update the value of the created script to test "put" method
			newScript.put("src", "http://frontdoor");
			ObjectResponse updatedScript = api.update("scripts", id, newScript);
			System.out.println(updatedScript.getResult().get("src"));
			
			
			ObjectResponse gottenScript2 = api.get("scripts", id);
			System.out.println(gottenScript2.getResult().get("src"));
			api.delete("scripts", id);
			
			
			ObjectResponse gottenScript3 = api.get("scripts", id);
			System.out.println(gottenScript3);
			
			
		} catch(ApiDetailedException e) {
			System.out.println(e.getStatusCode());
			System.out.println(e.getErrorObject());
		} catch(ApiException e) {
			e.printStackTrace();
			return;
		}
	}
}
