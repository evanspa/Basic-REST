;   Copyright (c) 2013 Paul Evans

;   Permission is hereby granted, free of charge, to any person obtaining a
;   copy of this software and associated documentation files (the "Software"),
;   to deal in the Software without restriction, including without limitation
;   the rights to use, copy, modify, merge, publish, distribute, sublicense,
;   and/or sell copies of the Software, and to permit persons to whom the
;   Software is furnished to do so, subject to the following conditions:

;   The above copyright notice and this permission notice shall be included
;   in all copies or substantial portions of the Software.

;   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
;   OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
;   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
;   THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
;   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
;   FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
;   DEALINGS IN THE SOFTWARE.

(ns ^{:doc "The core types of BasicREST."
      :author "Paul Evans"}
    basicrest.types.core
  (:use [basicrest.protocols.http :only
         [HttpRequestAcceptability
          HttpRequestSupportability]])
  (:use [basicrest.protocols.core
         :only [Printable to-string]])
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
    (let [charsets (Charset/availableCharsets)]
      charsets))

  (acceptable-encodings [this]
    (:encodings this))

  (acceptable-languages [this]
    (:languages this))

  (acceptable-mediatypes [this]
    (map to-string (:media-types this)))

  (nonstrict-accept-default [this header]
    (get (:nonstrict-accept-defaults this) header))

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

  (matched-resource [this request]
    ; TODO
    )

  ;; (is-entity-serialization-format-supported? [this request]
  ;;   ; TODO (need to do formal language type stuff)
  ;;   )
  )

(defrecord MediaRange
    [type
     subtype])

(defrecord MediaType
    [media-range
     supported-extension-params
     matcher-fn
     resources])

(extend-type MediaType
  Printable
  (to-string [this]
    (let [media-range (:media-range this)]
      (str (:type media-range) "/" (:subtype media-range)))))

(defrecord Resource
  [matcher-fn
   serializers
   http-method-fns])
