package com.tiendanube.apisdk;

import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Maps;

/**
 * The main point of entry to the Tienda Nube API.
 * 
 * @author mcolotto
 */
public class Api {

	private ApiClient client;

	/**
	 * Create a new Api object to interact with the Tienda Nube API. Valid only
	 * for one store and for one app.
	 * 
	 * @param apiCredentials
	 *            The {@link ApiCredentials} object with the information to
	 *            access the API. This contains the store id and the access
	 *            token.
	 * @param appName
	 *            The name of the application.
	 * @param contactAddress
	 *            An email or website to contact the application developer.
	 */
	public Api(ApiCredentials apiCredentials, String appName,
			String contactAddress) {
		this.client = new ApiClient(apiCredentials, appName, contactAddress);
	}

	/**
	 * Accesses an endpoint that lists objects.
	 * 
	 * @param endpoint
	 *            The endpoint to be accessed. Examples: "products", "scripts"
	 * @param additionalParams
	 *            Additional parameters to send with the request.
	 * @return A {@link ListResponse} with the results.
	 */
	public ListResponse list(String endpoint,
			Map<String, String> additionalParams) throws ApiException {
		try {
			if (additionalParams == null) {
				additionalParams = Maps.newHashMap();
			}
			if (!additionalParams.containsKey("page")) {
				additionalParams.put("page", "1");
			}
			if (!additionalParams.containsKey("per_page")) {
				additionalParams.put("per_page", "30");
			}

			InternalApiResponse response = client.get(endpoint,
					additionalParams);
			validateResponse(response);

			int page = Integer.valueOf(additionalParams.get("page"));
			int perPage = Integer.valueOf(additionalParams.get("per_page"));
			int totalCount = Integer.valueOf(response
					.getHeader("X-Total-Count").get(0));

			return new ListResponse(new JSONArray(response.getResponse()),
					page, totalCount, page * perPage >= totalCount, endpoint,
					additionalParams, response.getHeaders());
		} catch (JSONException e) {
			throw new ApiException("Invalid JSON responded by API", e);
		}
	}

	/**
	 * Shortcut method for {@link Api#list(String, Map)} without additional
	 * parameters.
	 * 
	 * @param endpoint
	 *            The endpoint to access
	 * @return A {@link ListResponse} with the results
	 */
	public ListResponse list(String endpoint) throws ApiException {
		return this.list(endpoint, null);
	}

	/**
	 * Returns the next page for the given {@link ListResponse}. Returns null if
	 * it was the last page.
	 * 
	 * @param response
	 *            The {@ListResponse} obtained through a call to
	 *            {@link #list(String, Map)} or {@link #nextPage(ListResponse)}
	 * @return A {@link ListResponse} with the results
	 */
	public ListResponse nextPage(ListResponse response) throws ApiException {
		if (response.isLastPage()) {
			return null;
		}

		Map<String, String> params = response.getRequestParams();
		params.put("page",
				String.valueOf(Integer.valueOf(params.get("page")) + 1));
		return this.list(response.getEndpoint(), params);
	}

	/**
	 * Accesses an endpoint that returns a single object.
	 * 
	 * @param endpoint
	 *            The endpoint to be accessed. Examples: "products", "scripts"
	 * @param id
	 *            The entity id.
	 * @return A {@link ObjectResponse} with the results, or null if the object
	 *         does not exist.
	 */
	public ObjectResponse get(String endpoint, int id,
			Map<String, String> additionalParams) throws ApiException {
		if (id < 0) {
			throw new IllegalArgumentException("Invalid id");
		}
		return this.internalGet(endpoint + "/" + id, additionalParams);
	}

	/**
	 * Shortcut to {@link #get(String, int, Map)}
	 * 
	 * @param endpoint
	 *            The endpoint to be accessed
	 * @param id
	 *            The entity id
	 * @return {@link ObjectResponse} with the results, or null if object does
	 *         not exist
	 */
	public ObjectResponse get(String endpoint, int id) throws ApiException {
		return this.get(endpoint, id, null);
	}

	/**
	 * Accesses the store endpoint, to obtain the data associated with the
	 * store.
	 * 
	 * @param additionalParams
	 *            Additional parameters for the request
	 * @return {@link ObjectResponse} with the results
	 */
	public ObjectResponse store(Map<String, String> additionalParams)
			throws ApiException {
		return this.internalGet("store", additionalParams);
	}

	/**
	 * Shortcut for {@link #store(Map)} without any additional params.
	 * 
	 * @return {@link ObjectResponse} with the store information
	 */
	public ObjectResponse store() throws ApiException {
		return this.store(null);
	}

	private ObjectResponse internalGet(String url,
			Map<String, String> additionalParams) throws ApiException {
		try {
			if (additionalParams == null) {
				additionalParams = Maps.newHashMap();
			}

			InternalApiResponse response = client.get(url, additionalParams);
			
			try {
				validateResponse(response);
			} catch (ApiNotFoundException e) {
				// If not found, return null
				return null;
			}

			return new ObjectResponse(new JSONObject(response.getResponse()),
					response.getHeaders());
		} catch (JSONException e) {
			throw new ApiException("Invalid JSON responded by API", e);
		}
	}

	/**
	 * Creates an object. Equivalent to a POST to a certain endpoint.
	 * 
	 * @param endpoint
	 *            The endpoint to be accessed. Example: "products", "scripts"
	 * @param object
	 *            The entity to create
	 * @return {@link ObjectResponse} with the created object
	 */
	public ObjectResponse create(String endpoint, JSONObject object)
			throws ApiException {
		try {
			InternalApiResponse response = client.post(endpoint, object);
			validateResponse(response);

			return new ObjectResponse(new JSONObject(response.getResponse()),
					response.getHeaders());
		} catch (JSONException e) {
			throw new ApiException("Invalid JSON responded by API", e);
		}
	}

	/**
	 * Updates an object. Equivalent to a PUT to a certain endpoint + "/" + the
	 * object id.
	 * 
	 * @param endpoint
	 *            The endpoint to be accessed. Example: "products", "scripts"
	 * @param id
	 *            The id of the object to update
	 * @param object
	 *            The entity to update
	 * @return {@link ObjectResponse} with the updated object
	 */
	public ObjectResponse update(String endpoint, int id, JSONObject object)
			throws ApiException {
		if (id < 1) {
			throw new IllegalArgumentException("Invalid id: " + id);
		}
		try {
			InternalApiResponse response = client.put(endpoint + "/" + id,
					object);
			validateResponse(response);

			return new ObjectResponse(new JSONObject(response.getResponse()),
					response.getHeaders());
		} catch (JSONException e) {
			throw new ApiException("Invalid JSON responded by API", e);
		}
	}

	/**
	 * Deletes an object. Equivalent to a DELETE to a certain endpoint + "/" +
	 * the object id.
	 * 
	 * @param endpoint
	 *            The endpoint to be accessed. Example: "products", "scripts"
	 * @param id
	 *            The id of the object to delete
	 */
	public void delete(String endpoint, int id) throws ApiException {
		if (id < 1) {
			throw new IllegalArgumentException("Invalid id: " + id);
		}

		InternalApiResponse response = client.delete(endpoint + "/" + id);
		validateResponse(response);
	}

	private void validateResponse(InternalApiResponse response)
			throws ApiException {
		if (response.getStatusCode() >= 500) {
			// Internal server error
			throw new ApiException("Internal server error returned by API.");
		}
		switch (response.getStatusCode()) {
		case HttpStatus.SC_BAD_REQUEST:
		case HttpStatus.SC_UNPROCESSABLE_ENTITY:
		case HttpStatus.SC_FORBIDDEN:
		case HttpStatus.SC_UNAUTHORIZED:
			throw new ApiDetailedException(response);
		case HttpStatus.SC_NOT_FOUND:
			throw new ApiNotFoundException(response);
		case 429: // Too many requests
			throw new ApiException("Too many requests");
		}
	}

}
