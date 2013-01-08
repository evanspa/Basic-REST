(ns basicrest.impl.basic-media-type
  (:use [basicrest.media-type
         basicrest.impl.util]))

(defrecord BasicMediaRange [type subtype]
  MediaRange
  (get-type [this]
    (:type this))
  (get-subtype [this]
    (:subtype this)))

(defrecord BasicMediaType
    [media-range
     supported-formats
     extension-param-names
     matcher-fn
     resources]
  MediaType

  (get-media-range [this]))