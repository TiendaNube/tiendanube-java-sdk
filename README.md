tiendanube-java-sdk
===================

Small Java SDK to use with the Tienda Nube API.


## Download

[Latest version](https://github.com/TiendaNube/tiendanube-java-sdk/raw/master/dist/tiendanube-java-sdk.jar)


## Useful links

* [Tienda Nube API Documentation](https://github.com/TiendaNube/api-docs)
* [SDK Javadocs](http://tiendanube.github.io/tiendanube-java-sdk/index.html)
* [Examples](https://github.com/TiendaNube/tiendanube-java-sdk/tree/master/src/test/java/com/tiendanube/apisdk)


## Documentation


The main point of entry is the [Api](http://tiendanube.github.io/tiendanube-java-sdk/index.html?com/tiendanube/apisdk/Api.html) class. This class offers an interface to interact with the Tienda Nube API. The methods of this class let you send requests to the API, using `list` (and `nextPage`), `get`, `store`, `create`, `update`, or `delete`. An instance of `Api` applies only to one set of [ApiCredentials](http://tiendanube.github.io/tiendanube-java-sdk/index.html?com/tiendanube/apisdk/ApiCredentials.html), which is specified when creating the object. 

To obtain the `ApiCredentials` for a store, you may use the `obtain` class method of `ApiCredentials`, which requires the OAuth 2 authorization code shown [in the official Tienda Nube API documentation](https://github.com/TiendaNube/api-docs/blob/master/resources/authentication.md#authorization-flow) as well as your app ID and secret. The `ApiCredentials` returned should be stored somewhere, in order to be able to reuse them later.

The Tienda Nube API offers several endpoints, which are equivalent to different entities. When accessing the API through the SDK, you must specify which endpoint you want to use when using any of the methods. Many of those (in particular, the creation / modification endpoints of the API) receive certain arguments in JSON format. To be able to send them, you must create a `JSONObject` which represents the arguments to be sent with the request. This object is part of the [json.org SDK for JSON handling](http://json.org/java/), which is included with the SDK for convenience purposes. The information returned is also inside a `JSONObject` which you must access according to the documentation of the Tienda Nube API of the endpoint you are using.

The [examples](https://github.com/TiendaNube/tiendanube-java-sdk/tree/master/src/test/java/com/tiendanube/apisdk) are a good starting point for working with the SDK. The first one features the creation, modification and deletion of a Script resource. The second one lists all the products in a store. There is also a third one that shows how to obtain `ApiCredentials` for the first time.


## TODO

* Entities for each particular object type
