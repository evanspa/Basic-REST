(ns basicrest.usage.wishful-thinking
  (:use [basicrest.rest-api]))

;; Phase 1 functionality
(def my-rest-api-v100
  (rest-api
   "v1.0.0"
   ; whether or not the desired (accepted) character set is supported or
   ; not will be discovered at runtime (i.e., by the return value of
   ; java.nio.charset.Charset.isAvailable() function).
   (supported-encodings "Identity" "GZIP")
   (supported-languages "en-US")
   (non-std-extensions :plus-sign-suffixed-format-specifiers)
   (nonstrict-acceptability-defaults
    {"accept-charset" "UTF-8"
     "accept-encoding" "Identity"
     "accept-language" "en"})
   (uri-builder-config
    {:prepend-restapi-version true})
   (uri-transformer (fn [uri] ...))
   (unacceptability-notice
    (content-type
     "application/vnd.foo.unacceptability-notice+XML;charset=UTF-8")
    (serialization-fn (fn [acceptability-entity] ...)))
   (supported-media-types
    (media-type
     (media-range "application" "vnd.foo.com")
     (supported-extension-params
      (extension-param "paramNm1" (fn [... param-val] ...))
      (extension-param "paramNm2" (fn [... param-val] ...)))
     (matcher-fn (fn [mt req] ...)) ; if nil is supplied,
     ; then perhaps by default a regex-based match will be
     ; performed (the regex can be built automatically based on the
     ; media range
     (supported-resources
      (resource
       (matcher-fn (fn [mt-ext-params res req] ...))
       (serialization
        :xml
        (de-serializer (fn [mt-ext-params res req] ...)))
       (supported-methods
        (http-post
         (fn [mt-ext-params res req] ...) ;should return newly-created resource
         (fn [mt-ext-params new-res] ...)))) ;should return URI of new resource
      (resource
       (matcher-fn (fn [mt-ext-params res req] ...))
       (serialization
        :xml
        (serializer (fn [mt-ext-params res req] ...))
        (de-serializer (fn [mt-ext-params res req] ...)))
       (supported-methods
        (http-put (fn [mt-ext-params res req] ...))
        (http-get (fn [mt-ext-params res req] ...))
        (http-delete (fn [mt-ext-params res req] ...)))))))))



;; Phase 2 functionality ('extends-*')
(def my-rest-api-v101
  (rest-api
   "v1.0.1"
   (extends-rest-api
    my-rest-api-v100
    (extends-media-type
     (of-range
      "application" "vnd.foo.com"
      (extends-resource
       :fuel-purchase
       (supported-methods
        (http-post (fn [rest-api mt res req] ...))
        (http-options (fn [rest-api mt res req] ...))))
      (new-supported-resources
       (resource (resource-type :gas-station) ...)))
     (of-range
      "application"
      (new-subtype
       "vnd.foo.bar.com"
       (formats ...)
       (extension-param-names ...)
       (matcher-fn ...)
       (new-supported-resources ...))))
    (new-supported-media-types ...))))

;; Phase 3 functionality (ability to move resources)
(def my-rest-api-v200
  (rest-api
   "v2.0.0"
   (extends-rest-api
    my-rest-api-v101
    (extends-media-type
     (of-range
      "application" "vnd.foo.com"
      (permanently-move-resource :fuel-purchase
                                 (new-uri (fn [res] ...)))
      (temporarily-move-resource :fuel-purchases
                                 (new-uri (fn [res] ...))))))))
