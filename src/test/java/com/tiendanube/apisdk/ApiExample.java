package com.tiendanube.apisdk;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiExample {

	public static void main(String[] args) throws JSONException {
		try {
			
			String accessToken = "46a971bbf4d655d8e2d91db1772c9410104315ae"; // Replace with the access token
			String storeId = "46"; // Replace with the store id
			
			Api api = new Api(new ApiCredentials(storeId, accessToken), "Java SDK test", "matias@tiendanube.com");
			ObjectResponse store = api.store();
			System.out.println(store.getResult().getJSONObject("name").get("es_AR"));
			JSONObject newScript = new JSONObject();
			newScript.put("src", "http://backdoor");
			newScript.put("where", "store");
			newScript.put("event", "onload");
			ObjectResponse createdScript = api.create("scripts", newScript);
			int id = createdScript.getResult().getInt("id");
			ObjectResponse gottenScript = api.get("scripts", id);
			System.out.println(gottenScript.getResult().get("src"));
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
