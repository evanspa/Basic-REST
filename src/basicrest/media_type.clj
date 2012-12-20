(ns basicrest.media-type)

(defprotocol Matchable
  (is-match? [this request]))

(defrecord MediaType [re-pattern resources]
  Matchable
  (is-match? [this request]
    (- 3 1)))