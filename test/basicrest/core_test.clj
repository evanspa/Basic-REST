(ns basicrest.core-test
  (:use clojure.test
        basicrest.core))

(deftest
  ^{:doc "TODO"}
  test-matched-media-type-1
  (def req {:uri "/foo/bars"
            :request-method :post
            :headers {"accept" "application/vnd.foo.bar+xml"
                      "accept-charset" "utf-8"
                      "accept-language" "en"}
            :body "<bar attr=\"val\" />"})
  (defn matcher1 [req mt]
    (let [accept-hdr (get (:headers req) "accept")]
      (if (= accept-hdr (:media-type mt))
        true
        false)))
  (def mts1
    #{{:media-type "application/vnd.foo.bar+xml"
       :re-pattern #"^application/vnd.foo.com+xml$"
       :matcher-fn matcher1}})
  (def mts1_1
    #{{:media-type "application/vnd.foo.bar+json"
       :re-pattern #"^application/vnd.foo.com+json$"
       :matcher-fn matcher1}})
  (let [matched1 (get-matched-media-type req mts1)
        matched2 (get-matched-media-type req mts1_1)]
    (is (not (nil? matched1)) "Simple matching case")
    (is (nil? matched2) "Simple non-matching case")))

(deftest
  ^{:doc "Try with nil media types set."}
  test-matched-media-type-2
  (def req2 {:uri "/foo/bars"
             :request-method :get
             :headers {"accept" "application/vnd.foo.bar+xml"
                       "accept-charset" "utf-8"
                       "accept-language" "en"}})
  (let [matched (get-matched-media-type req nil)]
    (is (nil? matched) "Obvious non-matching case")))
