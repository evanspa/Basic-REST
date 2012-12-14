# Design Notes

## 'rest-api' macro

The `rest-api` macro should return an instance of a clojure map.  The map will encapsulate all of the data that was supplied as arguments to the rest-api invocation.

## 'make-ring-handler' macro

The `make-ring-handler` macro will receive a clojure map as its input (from the `rest-api` invocation), and will return a [ring]() handler function.  At this point, the features and capabilies of ring can be applied (e.g., decorating the handler function with middleware). 

## User-provided functions

### Request entity deserializer

The user-provided request entity deserializer function will receive an input stream as its sole input (the source of the input stream will be the request entity body).  The deserializer is only invoked if the request contains an entity (HTTP PUT or POST only). 

### By-method Request processor

For each HTTP method (e.g., GET, PUT, POST, DELETE, etc) supported by the resource, a user-provided function will be specified.  This function will receive 2 arguments: (1) the request map, and (2) the return value of the deserializer function.  TODO - how to deal with URI query parameters.

### Response entity serializer

The user-provided response entity deserializer function will receive as input the return value of the HTTP-method function.

## Macro Expansion Examples / Progression

(rest-api)
 => {}

