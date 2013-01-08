(ns basicrest.impl.rest-api.basic-rest-api
  (:use [basicrest.impl.rest-api.rest-api-contract])
  (:use [basicrest.impl.util :only
         (is-accept-hdr-val-acceptable-if-strict?
          is-charset-available-on-system?
          get-header-val)]))

(defrecord BasicRestApi
    [version encodings languages nonstd-extensions nonstrict-accept-defaults
     uri-builder-config uri-transformer-fn media-types]
  RestApi

  (get-version [this]
    (:version this))

  (get-supported-encodings [this]
    (:encodings this))

  (get-supported-languages [this]
    (:languages this))

  (get-supported-nonstd-extensions [this]
    (:nonstd-extensions this))

  (get-nonstrict-accept-defaults [this]
    (:nonstrict-accept-defaults this))

  (get-uri-builder-config [this]
    (:uri-builder-config this))

  (get-uri-transformer-fn [this]
    (:uri-transformer-fn this))

  (is-charset-acceptable? [this request]
    (is-accept-hdr-val-acceptable-if-strict?
     (get-nonstrict-accept-defaults this)
     "accept-charset"
     (fn [charset] (is-charset-available-on-system? charset))
     request))

  (is-encoding-acceptable? [this request]
    (contains? (get-supported-encodings this)
               (get-header-val request "accept-encoding")))

  (is-language-acceptable? [this request]
    (contains? (get-supported-languages this)
               (get-header-val request "accept-language")))

  (is-serialization-format-acceptable? [this request]
    ; for simplicity, we'll assume the 'Accept' header will always
    ; be of the form: 'application/vnd.foo.com+xml' (or something like
    ; this)
    )

  (is-media-type-acceptable? [this request])

  (contains-entity? [this request])

  (is-entity-media-type-supported? [this request])

  (is-entity-serialization-format-supported? [this request])

  (process-request [this request]

    ; step : determine if request is acceptable; if not return 406
    ; step : match request to a resource; if unable, return 404
    ; step : determine if req has payload; and if so, is it acceptable; and if
    ;         so, parse it to populate 'model' object of matched resource
    ; step : invoke http-method function of matched resource
    ; step : if response has a body, serialize it according to 'accept' pref
))