(ns basicrest.impl.basic-rest-api
  (:use [basicrest.rest-api]))

;; (defn is-acceptable?
;;   "Returns true"
;;   [rest-api accept-hdr-name supported-vals-selector request]
;;     (if (is-accept-strict? rest-api accept-hdr-name)
;;       (let [accept-val (get-header-val request accept-hdr-name)
;;             supported-vals (get-supported-vals-selector rest-api)]
;;         (contains? supported-vals (lower-case accept-val)))
;;       true))


(defrecord BasicRestApiNonStrictAcceptDefault
    [accept-hdr-name default-val]
  RestApiNonStrictAcceptDefault
)

(defrecord BasicRestApi
    [version
     character-sets
     encodings
     acceptability-strictness-config
     nonstd-extensions
     media-types]
  RestApi

  (get-acceptability-strictness-config [this]
    (:acceptability-strictness this))

  (get-supported-charsets [this]
    (:character-sets this))

  (get-supported-encodings [this]
    (:encodings this))

  (get-nonstd-extensions [this]
    (:nonstd-extensions this))

  (is-accept-strict? [this accept-hdr-name]
    (let [strictness-config (get-acceptability-strictness-config this)]
      (when-not (nil? strictness-config)
        (= :strict (get strictness-config accept-hdr-name)))))

  (get-nonstrict-accept-default [this accept-hdr]
    (let [strictness-config (get-acceptability-strictness-config this)]
      (and nil? strictness-config
        (:default (get strictness-config accept-hdr-name)))))

  (is-charset-acceptable? [this request]
    (is-acceptable? this "accept-charset" get-supported-charsets request))

  (is-encoding-acceptable? [this request]
    (is-acceptable? this "accept-encoding" get-supported-encodings request))

  (is-language-acceptable? [this request]
    (is-acceptable? this "accept-language" get-supported-languages request))

  (is-serialization-format-acceptable? [this request]
    ; for simplicity, we'll assume the 'Accept' header will always
    ; be of the form: 'application/vnd.foo.com+xml' (or something like
    ; this)

    )

  (process-request [this request]

    ; step : determine if request is acceptable; if not return 406
    ; step : match request to a resource; if unable, return 404
    ; step : determine if req has payload; and if so, is it acceptable; and if
    ;         so, parse it to populate 'model' object of matched resource
    ; step : invoke http-method function of matched resource
    ; step : if response has a body, serialize it according to 'accept' pref
))