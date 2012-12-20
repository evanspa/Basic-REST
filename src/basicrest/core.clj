(ns basicrest.core)

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
     :character-sets #{:utf-8}
     :encodings #{:identity}
     :media-types
     #{{:media-type "application/vnd.foo.com"
        :matcher-fn (fn [req mt] (= 0 0))
        :re-pattern #"^application/vnd.foo.com$"
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

(defn
  get-restapi-version
  "Returns the version of the given rest-api."
  [rest-api]
  (:version rest-api))

(defn
  get-media-types
  "Returns the media types of the given REST API."
  [rest-api]
  (:media-types rest-api))

(defn
  get-matcher-fn
  "Returns the matcher function of the given media type."
  [media-type]
  (:matcher-fn media-type))

(defn
  is-match?
  "Returns the evaluation result of the truth functional matcher of the given
media type subject to the given request."
  [request media-type]
  (let [matcher-fn (get-matcher-fn media-type)]
    (if (nil? matcher-fn)
      false
      (matcher-fn request media-type))))

(defn
  get-matched-media-type
  "Returns the first media type whose truth functional matcher evalulates to 
'true' subject to the given request and the matcher's enclosing media type."
  [request media-types]
  (if (nil? media-types)
    nil
    (let [media-type (first media-types)]
      (if (nil? media-type)
        nil
        (if (is-match? request media-type)
          media-type
          (recur request (rest media-types)))))))

(defn
  get-status-if-no-matched-media-type
  "Returns the status code to be used for the given REST API in case the
request does not match to any media type."
  [rest-api]
  (:status-if-no-matched-media-type rest-api))

(defn
  request-has-entity?
  "Returns true if the given request contains a non-empty body (payload);
presumably the body contains a serialized resource entity."
  [request]
  (let [http-method (:request-method request)]
    (or (= http-method :put)
        (= http-method :post))))

(defn
  is-character-set-acceptable?
  [request rest-api]
  "")

(defn
  is-encoding-acceptable?
  [request rest-api]
  "")

(defn
  is-language-acceptable?
  [request media-type]
  "")

(defn
  is-serialization-format-acceptable?
  [request media-type]
  "")

(defn
  is-acceptable?
  [request rest-api media-type]
  (and (is-serialization-format-acceptable? request media-type)
       (is-language-acceptable? request media-type)
       (is-character-set-acceptable? request rest-api)
       (is-encoding-acceptable? request rest-api)))

(defn
  make-ring-handler
  [my-rest-api]
  (fn [request]
    (let [rest-version (get-restapi-version request)]
      (if (nil? rest-version)
        {:status 404}
        (let [matched-mt
              (get-matched-media-type request (get-media-types my-rest-api))]
          (if (nil? matched-mt)
            (get-status-if-no-matched-media-type my-rest-api)
            )))



(defn matched-media-type
  "Returns the media type that matches given the collection of
media types, as well as the request map."
  {:added "1.0"}
  [request media-types]
  (let [matcher-fn (get-matcher)
        match (filter #(matcher-fn request %) media-types)]
    (cond
     (> (count match) 1) (first match)
     (= (count match) 0) nil
     (= (count match) 1) match)))

; to test: ((make-ring-handler my-rest-api) req1)





; A code snippet for producing a map from a list
(defn list-to-map [lst]
  (if (nil? lst)
    nil
    (loop [m {}
           lst lst]
      (let [item (first lst)]
        (if (nil? item)
        m
        (recur (assoc m item item) (rest lst)))))))

; Another implementation, using letfn to define an internal function;
; this is basically in-contrast to using 'loop' in 'list-to-map'
(defn list-to-map2 [lst]
  (letfn [(list-to-map-internal [lst map]
            (if (nil? lst)
              nil
              (if (nil? (first lst))
                map
                (recur (rest lst)
                       (assoc map (first lst) (first lst))))))]
    (list-to-map-internal lst {})))
