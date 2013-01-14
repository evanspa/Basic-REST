(ns basicrest.types.mediatype
  (:use [basicrest.protocols.printable
         :only [Printable]]))

(defrecord MediaRange
    [type
     subtype])

(defrecord MediaType
    [media-range
     supported-extension-params
     matcher-fn
     resources])

(extend-type MediaType
  Printable
  (to-string [this]
    (let [media-range (:media-range this)]
      (str (:type media-range) "/" (:subtype media-range)))))
