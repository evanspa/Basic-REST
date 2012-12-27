(ns basicrest.rest-api)

(defprotocol RestApiNonStrictAcceptDefault
  (get-accept-header-name [this]
    "Returns the name of the accept header.")
  (get-default-value [this]
    "Returns the default value."))

(defprotocol RestApi
  "Abstraction that can handle / process RESTful requests."
  (get-acceptability-strictness-config [this]
    "Returns the acceptability-strictness configuration map defined for this
REST API.")
  (get-nonstrict-accept-defaults [this]
    "Returns the set of nonstrict 'Accept-' header defaults associated with
this REST API.")
  (get-supported-charsets [this]
    "Returns the set of character sets supported by this REST API.")
  (get-supported-encodings [this]
    "Returns the set of encodings supported by this REST API.")
  (get-nonstd-extensions [this]
    "Returns the non-standard extensions supported by this REST API.")
  (is-charset-acceptable? [this request]
    "Returns true if the character set accepted by the client (i.e. specified
by the 'Accept-Charset' of the HTTP request) is supported by this REST
API.")
  (is-encoding-acceptable? [this request]
    "Returns true if the encoding accepted by the client (i.e. specified
by the 'Accept-Encoding' of the HTTP request) is supported by this REST
API.")
  (is-language-acceptable? [this request]
    "Returns true if the language accepted by the client (i.e. specified
by the 'Accept-Language' of the HTTP request) is supported by this REST
API.  The 'Accept-Language' header is only relevant when the response
is anticipated as containing content meant to be display / consumed by a
human.")
  (is-serialization-format-acceptable? [this request]
    "Returns true if the serialization format accepted by the client
-- i.e. specified as part of the 'Accept' header of the HTTP request -- is
supported by this REST API.  For example, an 'Accept' header
might look like: 'Accept: application/vnd.foo.bar+XML'  In this example,
the accepted serialization format is XML.")
  (is-media-type-acceptable? [this request]
    "Returns true if the media type -- as specified in the 'Accept' header --
is supported by this REST API.")
  (contains-entity? [this request]
    "Returns true if the request contains an entity payload -- should only
be true for PUT and POST requests.")
  (is-entity-media-type-supported? [this request]
    "Returns true if the media type of the request entity is supported by this
REST API.")
  (is-entity-serialization-format-supported? [this request]
    "Returns true if the serialization format of the request entity is
supported by this REST API.")
  (process-request [this request]
    "Processes the request and produces a legal HTTP response."))
