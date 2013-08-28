package com.tiendanube.apisdk;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Maps;
import com.tiendanube.apisdk.Api;
import com.tiendanube.apisdk.ApiException;
import com.tiendanube.apisdk.ListResponse;
import com.tiendanube.apisdk.ObjectResponse;

public class ApiExample2 {

	public static void main(String[] args) throws ApiException, JSONException {
		String accessToken = ""; // Replace with the access token
		String storeId = "0"; // Replace with the store id
		Api api = new Api(new ApiCredentials(storeId, accessToken), "Java SDK test 2", "matias@tiendanube.com");
		ObjectResponse store = api.store();
		System.out.println(store.getResult().getJSONObject("name").get("es_AR"));
		Map<String, String> additionalParams = Maps.newHashMap();
		additionalParams.put("per_page", "200");
		ListResponse response = api.list("products", additionalParams);
		do {
			List<JSONObject> results = response.getResults();
			for(JSONObject result : results) {
				System.out.println(result.getInt("id") + " - " + result.getJSONObject("name").get("es"));
			}
			System.out.println("Remaining: " + response.getResponseHeader("X-Rate-Limit-Remaining"));
			System.out.println("Reset time: " + response.getResponseHeader("X-Rate-Limit-Reset"));
			response = api.nextPage(response);
		} while(response != null);
	}
}
