# BasicREST

BasicREST is a Lisp-based DSL for describing REST APIs. 

## Current status

BasicREST does not have a release yet; it is currently under-development.

## Rationale

The REST architectural style, paired with HTTP, provides an elegant frameowrk for building scalable, distributed systems.  Simply put, BasicREST provides a concise language for constructing REST APIs (aka RESTful services).

## Usage

Using BasicREST, one defines a REST API in terms of a set of media types.  A media type is a container for a set of resources.  When defining individual resources, a matcher function (predicate) needs to be provided that is used to determine if the current HTTP request matches-up to that resource.  Upon finding a match, additional functions, provided by the user, will be used to process the request.  For example, if the request contains an entity (in the case of HTTP PUT or POSTs), a function provided by the user will be used to deserialize the entity (into some data structure).  A user-provided function will be used to serialize the response entity.

Another user-provided function will be used to actually process the request (functions can be provided for the various HTTP verbs: GET, DELETE, PUT, POST, etc).  

When using the BasicREST language to express your REST API, matters of accepted serialization formats and character-sets can be declared.

Once you've described your REST API, BasicREST will do several validations for you.  For example, if an HTTP request arrives that matches-up with one of your media tyes, but specifies a character set in the 'Accept-Charset' that you don't explicitly support, BasicREST can automatically return a 406 (Not Acceptable).  If an HTTP request arrives that matches up to a media type and an individual resource, but that the resource has no function defined for the verb of the request, BasicREST will automatically return a 405 (Method Not Allowed).  

This small code snippet demonstrates describing a simple REST API using BasicREST:

```clojure
(ns myhello.restapi
  (:require myhello.core)
  (:use basicrest.core))
  
(def my-rest-api 
  (rest-api :version "v1.0.0" :protocol :http
    (media-type "application/vnd.hello"
      (char-serialization :xml #"(i)xml"
        (charset :utf-8 #"(i)utf-8"))
      (char-serialization :json #"(i)json"
        (charset :utf-8 #"(i)utf-8"))
      (resource :type :myhello-resource :matcher myhello.core/my-matcher-fn
        (serialization :xml
          (serializer myhello.core/my-xml-serialize-fn)
          (deserializer myhello.core/my-xml-deserialize-fn))
        (serialization :json
          (serializer myhello.core/my-json-serialize-fn)
          (deserializer myhello.core/my-json-deserializer-fn))
        (methods
          (PUT myhello.core/my-update-fn)
          (DELETE myhello.core/my-delete-fn)
          (GET myhello.core/my-fetch-fn))))))
          
(def my-ring-handle
  (make-ring-handler my-rest-api))
```

Using the `rest-api` macro, we define a variable here called `my-rest-api`.  Then, we use the `make-ring-handler` macro to construct a Ring handler function from our `my-rest-api` instance.

When an HTTP request arrives, the value of its `Accept` header will attempted to be matched against one of the media types contained by our REST API; once matched against a media type, next the request will attempted to be matched against one of the resources of the media type.  This will be done by evaluating the user-provided `myhello.core/my-matcher-fn` predicate function (this function will receive the HTTP request map as its input).

Once matched against a resource, the request entity (for PUT/POST only) will be supplied as an argument to one of the user-provided deserializer functions.  The result of the deserializer will then be supplied as the input to the appropriate HTTP method user-provided function.  

The decision as to which deserializer function to use to parse the request entity depends on the value of the `Content-type` header of the request.  For example, if the value of the Content-Type header is `application/vnd.hello+XML;charset=UTF-8`, then `myhello.core/my-xml-deserializer` will be used.  The decision as to which serializer to use depends on the value of the `Accept` header of the request.  For example, if the value of the Accept header is `application/vnd.hello+JSON`, then `myhello.core/my-json-serializer` will be used to produce the serialized response entity.  

## Documentation

* [Wiki](https://github.com/evanspa/BasicREST/wiki)

## License

Copyright © 2012 Paul Evans

Distributed under the Eclipse Public License, the same as Clojure.
