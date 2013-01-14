(ns basicrest.types.restapi
  (:use [basicrest.protocols.http-request-acceptability
         :only [HttpRequestAcceptability]])
  (:use [basicrest.protocols.http-request-supportability
         :only [HttpRequestSupportability]])
  (:use [basicrest.protocols.printable :only [to-string]])
  (:use [basicrest.protocols.http-request-processor
         :only [HttpRequestProcessor]])
  (:import (java.nio.charset Charset)
           (java.util SortedMap)))

(defrecord RestApi
    [version
     encodings
     languages
     nonstd-extensions
     nonstrict-accept-defaults
     uri-builder-config
     uri-transformer-fn
     media-types])

(extend-type RestApi
  HttpRequestAcceptability
  (acceptable-charsets [this]
    (let [charsets (Charset/availableCharsets)]))

  (acceptable-encodings [this]
    (:encodings this))

  (acceptable-languages [this] )

  (acceptable-mediatypes [this]
    (map to-string (:media-types this)))

  (most-desired-accepted-charset [this request]
    ; TODO (need to do formal language type stuff)
    )

  (most-desired-accepted-encoding [this request]
    ; TODO (need to do formal language type stuff)
    )

  (most-desired-accepted-language [this request]
    ; TODO (need to do formal language type stuff)
    )

  (most-desired-accepted-mediatype [this request]
    ; TODO (need to do formal language type stuff)
    )

  ;; (is-serialization-format-acceptable? [this request]
  ;;   ; TODO (need to do formal language type stuff)
  ;;   )
)

(extend-type RestApi
  HttpRequestSupportability

  (matched-entity-mediatype [this request]
    ; TODO (need to do formal language type stuff)
    )

  ;; (is-entity-serialization-format-supported? [this request]
  ;;   ; TODO (need to do formal language type stuff)
  ;;   )
  )

(extend-type RestApi
  HttpRequestProcessor
  (matched-resource [this request]
    ; TODO
    ))