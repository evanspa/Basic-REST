(ns basicrest.rest-api)

(defprotocol RestExecutable
  "A contract for being able to execute (aka process) received HTTP-based
RESTful requests."
  (is-charset-acceptable? [this request]
    "Returns true if the character set accepted by the client (i.e. specified
by the 'Accept-Charset' of the HTTP request) is supported by this REST
executor.")
  (is-encoding-acceptable? [this request]
    "Returns true if the encoding accepted by the client (i.e. specified
by the 'Accept-Encoding' of the HTTP request) is supported by this REST
executor.")
  (is-language-acceptable? [this request]
    "Returns true if the language accepted by the client (i.e. specified
by the 'Accept-Language' of the HTTP request) is supported by this REST
executor.  The 'Accept-Language' header is only relevant when the response
is anticipated as containing content meant to be display / consumed by a
human.")
  (is-serialization-format-acceptable? [this request]
    "Returns true if the serialization format accepted by the client
(i.e. specified as part of the 'Accept' header of the HTTP request) is
supported by this REST executor.  For example, an 'Accept' header
might look like: 'Accept: application/vnd.foo.bar+XML'  In this example,
the accepted serialization format is XML.")
  (is-acceptable? [this request])
  (contains-entity? [this request])
  (is-entity-media-type-supported? [this request])
  (is-entity-serialization-format-supported? [this request])
  (process-request [this request]))

(defrecord BasicRestApi [version character-sets encodings media-types]
  RestExecutable
  (process-request [this request]
    (+ 1 2)))