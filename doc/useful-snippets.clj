
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
  request-has-entity?
  "Returns true if the given request contains a non-empty body (payload);
presumably the body contains a serialized resource entity."
  [request]
  (let [http-method (:request-method request)]
    (or (= http-method :put)
        (= http-method :post))))


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


(defn get-header-val [request hdr-name]
  (let [headers (:headers request)]
    (when-not (nil? headers)
      (get headers hdr-name))))
