(ns basicrest.impl.rest-api.rest-api-contract)

(defprotocol RestApi
  "Abstraction that can handle / process RESTful requests."

  (get-version [this]
    "Returns the version of the given REST API.")

  (get-supported-encodings [this]
    "Returns the set of encodings supported by this REST API.")

  (get-supported-languages [this]
    "Returns the set of languages supported by this REST API (in the context
of providing human-consumable content within returned resource
representations..")

  (get-supported-nonstd-extensions [this]
    "Returns the non-standard extensions supported by this REST API.")

  (get-nonstrict-accept-defaults [this]
    "Returns the set of nonstrict 'Accept-' header defaults associated with
this REST API.")

  (get-uri-builder-config [this]
    "Returns the URI builder configuration of the given REST API.")

  (get-uri-transformer-fn [this]
    "Returns the URI transformer function of the given REST API.")

;;   (is-charset-acceptable? [this request]
;;     "Returns true if the character set accepted by the client (i.e. specified
;; by the 'Accept-Charset' of the HTTP request) is supported by this REST
;; API.")
  (get-most-desirable-accepted-charset [this request]
    "Returns the character set that is both advertised as acceptable by the
client and is supported by the given REST API.  If the client specifies
multiple character sets as being acceptable, the quality value(s) provided will
be honored.  That is, if 2 character sets are provided as being acceptable to
the client and one has a quality value of 0.7 and the other 0.8, and if both
character sets are supported by the given REST API, the character set annotated
with the 0.8 quality value will be returned.")

;;   (is-encoding-acceptable? [this request]
;;     "Returns true if the encoding accepted by the client (i.e. specified
;; by the 'Accept-Encoding' of the HTTP request) is supported by this REST
;; API.")
  (get-most-desired-accepted-encoding [this request]
"Returns the encoding that is both advertised as acceptable by the client and
is supported by the given REST API.  If the client specifies multiple encodings
as being acceptable, the quality value(s) provided will be honored.  That is,
if 2 encodings are provided as being acceptable to the client and one has
quality value of 0.7 and the other 0.8, and if both encodings are supported by
the given REST API, the encoding annotated with the 0.8 quality value will be
returned.")

;;   (is-language-acceptable? [this request]
;;     "Returns true if the language accepted by the client (i.e. specified
;; by the 'Accept-Language' of the HTTP request) is supported by this REST
;; API.  The 'Accept-Language' header is only relevant when the response
;; is anticipated as containing content meant to be display / consumed by a
;; human.")
  (get-most-desired-accepted-language [this request]
"Returns the language that is both advertised as acceptable by the client and
is supported by the given REST API.  If the client specifies multiple languages
as being acceptable, the quality value(s) provided will be honored.  That is,
if 2 languages are provided as being acceptable to the client and one has
quality value of 0.7 and the other 0.8, and if both languages are supported by
the given REST API, the language annotated with the 0.8 quality value will be
returned.")

  (is-serialization-format-acceptable? [this request]
    "Returns true if the serialization format accepted by the client
-- i.e. specified as part of the 'Accept' header of the HTTP request -- is
supported by this REST API.  For example, an 'Accept' header
might look like: 'Accept: application/vnd.foo.bar+XML'  In this example,
the accepted serialization format is XML.")

;;   (is-media-type-acceptable? [this request]
;;     "Returns true if the media type -- as specified in the 'Accept' header --
;; is supported by this REST API.")
  (get-most-desired-accepted-mediatype [this request]
"Returns the media type that is both advertised as acceptable by the client and
is supported by the given REST API.  If the client specifies multiple media
types as being acceptable, the quality value(s) provided will be honored.  That
is, if 2 media types are provided as being acceptable to the client and one has
quality value of 0.7 and the other 0.8, and if both media types are supported by
the given REST API, the media type annotated with the 0.8 quality value will be
returned.")

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