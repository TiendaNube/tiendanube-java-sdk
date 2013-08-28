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

The main point of entry is the [Api](http://tiendanube.github.io/tiendanube-java-sdk/index.html?com/tiendanube/apisdk/Api.html) class. This class offers an interface to interact with the Tienda Nube API. The methods of this class let you send requests to the API, using `list` (and `nextPage`), `get`, `store`, `create`, `update`, or `delete`. An instance of `Api` applies only to one store, which is specified when creating the object.

The Tienda Nube API offers several endpoints, which are equivalent to different entities. When accessing the API through the SDK, you must specify which endpoint you want to use when using any of the methods. Many of those (in particular, the creation / modification endpoints of the API) receive certain arguments in JSON format. To be able to send them, you must create a `JSONObject` which represents the arguments to be sent with the request. This object is part of the [json.org SDK for JSON handling](http://json.org/java/), which is included with the SDK for convenience purposes. The information returned is also inside a `JSONObject` which you must access according to the documentation of the Tienda Nube API of the endpoint you are using.

The [examples](https://github.com/TiendaNube/tiendanube-java-sdk/tree/master/src/test/java/com/tiendanube/apisdk) are a good starting point for working with the SDK. The first one features the creation, modification and deletion of a Script resource. The second one lists all the products in a store.


## TODO

* OAuth support
* Entities for each particular object type
