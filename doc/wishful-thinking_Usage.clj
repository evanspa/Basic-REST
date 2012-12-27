(ns basicrest.usage.wishful-thinking
  (:use [basicrest.rest-api]))

;; Phase 1 functionality
(def my-rest-api-v100
  (rest-api
   "v1.0.0"
   (supported-character-sets "UTF-8" "ISO-8859-1")
   (supported-encodings "Identity" "GZIP")
   (non-std-extensions :plus-sign-suffixed-format-specifiers)
   (nonstrict-acceptability-defaults
    {"accept-charset" "UTF-8"
     "accept-encoding" "Identity"
     "accept-language" "en"})
   (uri-builder-config
    {:uri-structure :parent-child
     :prepend-restapi-version true})
   (uri-transformer (fn [rest-api uri] ...))
   (supported-media-types
    (media-type
     (media-range "application" "vnd.foo.com")
     (supported-formats
      (character-based :xml :json))
     (extension-param-names "paramNm1" "paramNm2")
     (matcher-fn (fn [rest-api mt req] ...))
     (supported-resources
      (resource
       (resource-handle :fuel-purchases)
       (static-uri "fuel-purchases")
       (matcher-fn (fn [rest-api mt res req] ...))
       (serialization
        :xml
        (de-serializer (fn [rest-api mt res req] ...)))
       (supported-methods
        (http-post (fn [rest-api mt res req] ...))))
      (resource
       (resource-handle :fuel-purchase)
       (parent-resource :fuel-purchases)
       (dynamic-uri (fn [rest-api mt res req] ...))
       (matcher-fn (fn [rest-api mt res req] ...))
       (serialization
        :xml
        (serializer (fn [rest-api mt res req] ...))
        (de-serializer (fn [rest-api mt res req] ...)))
       (supported-methods
        (http-put (fn [rest-api mt res req] ...))
        (http-get (fn [rest-api mt res req] ...))
        (http-delete (fn [rest-api mt res req] ...)))))))))

;; Phase 2 functionality ('extends-*')
(def my-rest-api-v101
  (rest-api
   "v1.0.1"
   (extends-rest-api
    "v1.0.0"
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
    "v1.0.1"
    (extends-media-type
     (of-range
      "application" "vnd.foo.com"
      (permanently-move-resource :fuel-purchase
                                 (new-uri (fn [res] ...)))
      (temporarily-move-resource :fuel-purchases
                                 (new-uri (fn [res] ...))))))))
