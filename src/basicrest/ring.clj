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

(ns ^{:doc "The functionality responsible for creating Ring handler functions
from a RestApi definition."
      :author "Paul Evans"}
    basicrest.ring
  (:use [basicrest.protocols.http
         :only [most-desired-accepted-mediatype
                acceptable-mediatypes
                most-desired-accepted-charset
                acceptable-charsets
                most-desired-accepted-encoding
                acceptable-encodings
                most-desired-accepted-language
                acceptable-languages
                matched-entity-mediatype
                matched-resource]])
  (:use [basicrest.util
         :only [contains-entity?
                parse-serialization-format]])
  (:import [basicrest.resources AcceptabilityReportResource]))

(defn make-ring-handler-fn
  "Makes and returns a Ring handler function from the given REST API instance."
  [restapi]
  (fn [req]
    (let [req-headers (:headers req)
          matched-accept-mt (most-desired-accepted-mediatype req restapi)
          supported-accept-mts (when (nil? matched-accept-mt)
                                 (acceptable-mediatypes restapi))
          matched-accept-charset (most-desired-accepted-charset req restapi)
          supported-accept-charsets (when (nil? matched-accept-charset)
                                 (acceptable-charsets restapi))
          matched-accept-encoding (most-desired-accepted-encoding req restapi)
          supported-accept-encodings (when (nil? matched-accept-encoding)
                                 (acceptable-encodings restapi))
          matched-accept-language (most-desired-accepted-language req restapi)
          supported-accept-languages (when (nil? matched-accept-language)
                                 (acceptable-languages restapi))]
      (if (and matched-accept-mt
               matched-accept-charset
               matched-accept-encoding
               matched-accept-language)
        (let [matched-resource (matched-resource restapi req)]
          (if-not (nil? matched-resource)
            (let [req-has-entity (contains-entity? req)
                  http-method (:request-method req)
                  http-method-fns (:http-method-fns matched-resource)
                  http-method-fn (get http-method-fns http-method)]
              (if-not (nil? http-method-fn)
                (if req-has-entity
                  (let [matched-entity-mt
                        (when req-has-entity
                          (matched-entity-mediatype restapi req))]
                    (if-not (nil? matched-entity-mt)
                      (let [req-ent-serialization-format
                            (parse-serialization-format
                             (get req-headers
                                  "Content-Type"))]
                        (if-not (nil? req-ent-serialization-format)
                          (let [serializer-fns
                                (get (:serializers matched-resource)
                                     req-ent-serialization-format)]
                            (if-not (nil? serializer-fns)
                              (let [req-ent ((:de-serializer serializer-fns)
                                             (:body req))]
                                (http-method-fn req-ent req))
                              {:status 415}))
                          {:status 400}))
                      {:status 415}))
                  (http-method-fn req))
                {:status 405}))
            {:status 404}))
        {:status 406
         :headers {"Content-Type"
                   (:unacceptability-notice-content-type restapi)}
         :body
         ((:unacceptability-notice-entity-deserializer restapi)
          (AcceptabilityReportResource. supported-accept-mts
                                        supported-accept-charsets
                                        supported-accept-encodings
                                        supported-accept-languages))}))))
