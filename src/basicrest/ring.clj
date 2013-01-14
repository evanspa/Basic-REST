(ns basicrest.ring
  (:use [basicrest.protocols.http-request-acceptability
         :only [most-desired-accepted-mediatype
                acceptable-mediatypes
                most-desired-accepted-charset
                acceptable-charsets
                most-desired-accepted-encoding
                acceptable-encodings
                most-desired-accepted-language
                acceptable-languages]])
  (:use [basicrest.util
         :only [contains-entity?
                parse-serialization-format]])
  (:use [basicrest.protocols.http-request-supportability
         :only [matched-entity-mediatype]])
  (:use [basicrest.protocols.http-request-processor
         :only [matched-resource]])
  (:import [basicrest.types.resource_entities AcceptabilityEntity]))

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
          (if (not (nil? matched-resource))
            (let [req-has-entity (contains-entity? req)
                  http-method (:request-method req)
                  http-method-fns (:http-method-fns matched-resource)
                  http-method-fn (get http-method-fns http-method)]
              (if (not (nil? http-method-fn))
                (if req-has-entity
                  (let [matched-entity-mt
                        (when req-has-entity
                          (matched-entity-mediatype restapi req))]
                    (if (not (nil? matched-entity-mt))
                      (let [req-ent-serialization-format
                            (parse-serialization-format
                             (get req-headers
                                  "Content-Type"))]
                        (if (not (nil? req-ent-serialization-format))
                          (let [serializer-fns
                                (get (:serializers matched-resource)
                                     req-ent-serialization-format)]
                            (if (not (nil? serializer-fns))
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
          (AcceptabilityEntity. supported-accept-mts
                                supported-accept-charsets
                                supported-accept-encodings
                                supported-accept-languages))}))))
