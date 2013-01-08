(def my-rest-api-v100
  (.BasicRestApi
   "v1.0.0" ; version
   #{"Identity" "GZIP"} ; supported encodings
   #{"en-US" ...} ; supported languages
   #{:plus-sign-suffixed-format-specifiers} ; supported non-std extensions
   {"accept-charset" "UTF-8" ; non-strict acceptability defaults
    "accept-encoding" "Identity"}
   {:prepend-restapi-version true} ; URI builder config map
   (fn [uri] ...) ; URI transformer fn
   #{(.BasicMediaType
      (.BasicMediaRange "application" "vnd.foo.com")
      #{["paramNm1" (fn [param] ...)] ; supported extension params
        ["paramNm2" (fn [param] ...)]}
      (fn [mt req] ...) ; matcher-fn
      #{(.BasicResource
         (fn [mt-ext-params res req] ...) ; matcher fn
         {:xml {:de-serializer (fn [mt-ext-params res req] ...)
                :serializer (fn [mt-ext-params res req] ...)}}
         {"post"
          [(fn [mt-ext-params res req] ...) ;handler
           (fn [mt-ext-params new-res] ...) ; returns URI of new res
           ]})
        (.BasicResource
         (fn [mt-ext-params res req] ...) ; matcher fn
         {:xml {:de-serializer (fn [mt-ext-params res req] ...)
                :serializer (fn [mt-ext-params res req] ...)}}
         {"get" (fn [mt-ext-params res req] ...)
          "put" (fn [mt-ext-params res req] ...)
          "delete" (fn [mt-ext-params res req] ...)})})}))

(def req1 {:server-port 100
           :uri "/foo/bars"
           :request-method :post
           :headers {"accept" "application/vnd.foo.bar+xml"
                     "accept-charset" "utf-8"
                     "accept-language" "en"}
           :body "<bar attr=\"val\" />"})
