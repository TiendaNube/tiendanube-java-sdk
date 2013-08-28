package com.tiendanube.apisdk;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

/**
 * This class is an internal client to interact with the API.
 * Automatically handles converting to/from JSON.
 * 
 * @author mcolotto
 */
class ApiClient {
	
	private String userAgent;
	private ApiCredentials apiCredentials;
	
	ApiClient(ApiCredentials apiCredentials, String appName, String contactEmail) {
		this.apiCredentials = apiCredentials;
		this.userAgent = appName + " (" + contactEmail + ")";
	}

	InternalApiResponse get(String url, Map<String, String> params) throws ApiException {
		HttpClient client = new DefaultHttpClient();
		
		String query = "";
		for(String key : params.keySet()) {
			String value = params.get(key);
			query = query + key + "=" + value + "&";
		}
		if(query.length() > 0) {
			query = query.substring(0, query.length() - 1);
		}
		
		HttpUriRequest request = new HttpGet("http://api.tiendanube.com/v1/" + apiCredentials.getStoreId() + "/" + url + "?" + query);
		request.addHeader("Authentication", "bearer " + apiCredentials.getAccessToken());
		request.setHeader("User-Agent", userAgent);
		
		HttpParams httpParams = new BasicHttpParams();
		for(String key : params.keySet()) {
			String value = params.get(key);
			httpParams.setParameter(key, value);
		}
		request.setParams(httpParams);
		
		try {
			HttpResponse response = client.execute(request);
			return new InternalApiResponse(response);
		} catch(IOException e) {
			throw new ApiException("Problem while trying to GET the URL " + url, e);
		}
	}
	
	InternalApiResponse delete(String url) throws ApiException {
		HttpClient client = new DefaultHttpClient();
		
		HttpUriRequest request = new HttpDelete("https://api.tiendanube.com/v1/" + apiCredentials.getStoreId() + "/" + url);
		request.addHeader("Authentication", "bearer " + apiCredentials.getAccessToken());
		request.setHeader("User-Agent", userAgent);
		
		try {
			HttpResponse response = client.execute(request);
			return new InternalApiResponse(response);
		} catch(IOException e) {
			throw new ApiException("Problem while trying to DELETE the URL " + url, e);
		}
	}
	
	InternalApiResponse post(String url, JSONObject object) throws ApiException {
		HttpPost request = new HttpPost("https://api.tiendanube.com/v1/" + apiCredentials.getStoreId() + "/" + url);
		return this.internalPostPut(request, object);
	}
	
	InternalApiResponse put(String url, JSONObject object) throws ApiException {
		HttpPut request = new HttpPut("https://api.tiendanube.com/v1/" + apiCredentials.getStoreId() + "/" + url);
		return this.internalPostPut(request, object);
	}
	
	InternalApiResponse internalPostPut(HttpEntityEnclosingRequestBase request, JSONObject object) 
			throws ApiException {
		HttpClient client = new DefaultHttpClient();
		
		request.addHeader("Authentication", "bearer " + apiCredentials.getAccessToken());
		request.setHeader("User-Agent", userAgent);
		StringEntity entity = new StringEntity(object.toString(), ContentType.APPLICATION_JSON);
		request.setEntity(entity);
		
		try {
			HttpResponse response = client.execute(request);
			return new InternalApiResponse(response);
		} catch(IOException e) {
			throw new ApiException("Problem while trying to " + request.getMethod() + 
									" the URL " + request.getURI().toString(), e);
		}
	}
	
}
