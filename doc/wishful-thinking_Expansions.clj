(ns basicrest.core)

(def my-rest-api
  (.BasicRestApi
   "v1"
   #{"utf-8"}
   #{"identity"}
   #{(.BasicAcceptabilityNonStrictDefault "accept-charset" "utf-8")
     (.BasicAcceptabilityNonStrictDefault "accept-encoding" "identity")))


; Let's start backward.  Assume we have a map in the structure that we
; intend our 'rest-api' function/macro to produce.  Now, see if we can
; write our 'make-ring-handler' function from it.  If we can, great; it
; just means then that we need to create our 'rest-api' function/macro

(def
  ^{:doc "A REST API is a set of maps.  A REST API map has a version,
and contains a set of maps of media types.  A media type map has a media-type
value, a matcher function, a regex pattern describing its media-type value, and
a set of resource maps."}
  my-rest-api
  #{{:version "v1.0.0"
     :status-if-no-matched-media-type 406 ; Not Acceptable
     :character-sets #{"utf-8"}
     :encodings #{"identity"}

; If some 'accept-' header is deemed unacceptable by the server, the
; server, based on the HTTP spec, can either return a 406, or can
; produce an unacceptable response. The 'acceptability-strictness' is
; a way for the REST API to configure for which 'accept' headers should
; be 'strict' and return a 406 when some request is deemed unacceptable.
     :acceptability-nonstrict-defaults
     {"accept-charset" "utf-8"
      "accept-language" "en"
      "accept-encoding" "identity"}
     :nonstd-extensions
     {:plus-sign-suffixed-format-specifiers :enabled}
     :media-types
     #{{:media-range {:type "application"
                      :subtype "vnd.foo.com"}
        :matcher-fn (fn [req mt] (= 0 0))
        :re-pattern #"^application/vnd.foo.com$"
        :mime-types #{"xml" "json"}
        :extended-param-names #{"paramName1" "paramName2"}
        :resources
        {:pet
         {:matcher-fn (fn [req] true)
          :serializations
          {:xml {:stream-type :character
                 :serializer (fn [pet] (str "fido"))
                 :deserializer (fn [req] ({:mypet {:name "fido"}}))}}
          :finder-fn (fn [pet] true) ; if false, return 404
          :http-methods
          {:PUT (fn [pet] (str "todo"))
           :GET (fn [pet] (str "todo"))
           :DELETE (fn [pet] (str "todo"))}}}}}}})

(def req1 {:server-port 100
           :uri "/foo/bars"
           :request-method :post
           :headers {"accept" "application/vnd.foo.bar+xml"
                     "accept-charset" "utf-8"
                     "accept-language" "en"}
           :body "<bar attr=\"val\" />"})

