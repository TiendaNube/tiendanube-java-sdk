package com.tiendanube.apisdk;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;

/**
 * A response from the API that represents a list of objects.
 * 
 * @author mcolotto
 */
public class ListResponse extends ApiResponse {
	
	private List<JSONObject> results; 
	private int page;
	private int totalCount;
	private boolean lastPage;
	
	private String endpoint;
	private Map<String, String> requestParams;

	ListResponse(JSONArray results, int page, int totalCount, boolean lastPage, 
			String endpoint, Map<String, String> requestParams, Map<String, String> responseHeaders) throws JSONException {
		super(responseHeaders);
		List<JSONObject> resultList = Lists.newArrayList();
		for(int i = 0; i < results.length(); i++) {
			resultList.add(results.getJSONObject(i));
		}
		this.results = resultList;
		this.page = page;
		this.totalCount = totalCount;
		this.lastPage = lastPage;
		this.endpoint = endpoint;
		this.requestParams = requestParams;
	}

	public List<JSONObject> getResults() {
		return results;
	}

	public int getPage() {
		return page;
	}

	public int getTotalCount() {
		return totalCount;
	}
	
	public boolean isLastPage() {
		return lastPage; 
	}
	
	Map<String, String> getRequestParams() {
		return requestParams;
	}
	
	String getEndpoint() {
		return endpoint;
	}
	
}
