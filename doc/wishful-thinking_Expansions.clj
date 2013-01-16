(def my-rest-api-v100
  (.RestApi
   "v1.0.0" ; version
   #{"Identity" "GZIP"} ; supported encodings
   #{"en-US" ...} ; supported languages
   #{:plus-sign-suffixed-format-specifiers} ; supported non-std extensions
   {"accept-charset" "UTF-8" ; non-strict acceptability defaults
    "accept-encoding" "Identity"}
   {:prepend-restapi-version true} ; URI builder config map
   (fn [uri] ...) ; URI transformer fn
   #{(.MediaType
      (.MediaRange "application" "vnd.foo.com")
      #{["paramNm1" (fn [param] ...)] ; supported extension params
        ["paramNm2" (fn [param] ...)]}
      (fn [mt req] ...) ; matcher-fn
      #{(.Resource
         (fn [mt-ext-params res req] ...) ; matcher fn
         {:xml {:de-serializer (fn [req-ent-str] ...)
                :serializer (fn [req-ent-str] ...)}}
         {:post
          [(fn [mt-ext-params res req] ...) ;handler
           (fn [mt-ext-params new-res] ...) ; returns URI of new res
           ]})
        (.Resource
         (fn [mt-ext-params res req] ...) ; matcher fn
         {:xml {:de-serializer (fn [mt-ext-params res req] ...)
                :serializer (fn [mt-ext-params res req] ...)}}
         {:get (fn [mt-ext-params res req] ...)
          :put (fn [mt-ext-params res req] ...)
          :delete (fn [mt-ext-params res req] ...)})})}))

(def req1 {:server-port 100
           :uri "/foo/bars"
           :request-method :post
           :headers {"accept" "application/vnd.foo.bar+xml"
                     "accept-charset" "utf-8"
                     "accept-language" "en"}
           :body "<bar attr=\"val\" />"})


; From this defrecord instance, the 'make-ring-handler-fun' will have the
; complex job of using this instance to produce a function that can be
; used to process requests

; when building a rest api, the workhorses are the functions that are
; defined for the resources for the http methods.  The output of
; of this class of function should be a map.  In fact, I would ay that
; the map be formated precisely like the map that a ring handler function
; is supposed to produce, except that instead of the ":body" key being
; present, instead there should be a vector of resources (if the vector
; is of size 1, then the 'Content-Type' of the whole of the response is
; the content type of the resource; if the size of the vector is size
; > 1, then the content type of the whole of the response should be
; multipart/$$$ where $$$ is determined based on the properties of the
; vector.  For example, if the vector contains resources that span
; multiple media types, then $$$ should be 'mixed'.  Etc.  Things like
; headers and such should be returned in the map produced by the
; resource/http-method function
