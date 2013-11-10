package com.tiendanube.apisdk;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class WebhookTest {
	public static void main(String[] args) throws JSONException, ApiException {

		String accessToken = "f6499a87be1177a3ce4f4eac17bedee1d87410da";
		String storeId = "46"; // Replace with the store id
		Api api = new Api(new ApiCredentials(storeId, accessToken),
				"Java SDK test", "matias@tiendanube.com");

		// create a webhook
		JSONObject updateProductWebhook = new JSONObject();
		updateProductWebhook.put("url", "http://www.testurl.com.ar");
		updateProductWebhook.put("event", "product/updated");
		ObjectResponse createdWebhook = api.create("webhooks",
				updateProductWebhook);
		System.out.println(createdWebhook);

		// finally, list all webhooks for the app
		ListResponse webhookList = api.list("webhooks");
		Iterator<JSONObject> webhookIterator = webhookList.getResults()
				.iterator();
		while (webhookIterator.hasNext()) {
			JSONObject aux = webhookIterator.next();
			System.out.println(aux);
		}

		// delete all webhooks
		Iterator<JSONObject> webhookSecondList = webhookList.getResults()
				.iterator();
		while (webhookSecondList.hasNext()) {
			JSONObject aux = webhookSecondList.next();
			api.delete("webhooks", Integer.parseInt(aux.get("id").toString()));
			System.out.println();
		}

	
	}
}
