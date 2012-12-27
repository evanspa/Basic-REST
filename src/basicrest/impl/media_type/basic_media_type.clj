(ns basicrest.impl.basic-media-type
  (:use [basicrest.media-type]))

(defn parse-media-types
  "Parses the media types from the given string and returns a vector of media
types.  The order of the returned vector is based on the quality-value
see http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.9 associated
with each of the media types.

Example usage:

    (parse-media-types 'application/xml; q=0.7, text/html')
        => [{:media-range}]

"
  [accept-hdr-str])

(defrecord BasicMediaRange [type subtype]
  MediaRange
  (get-type [this]
    (:type this))
  (get-subtype [this]
    (:subtype this)))

(defrecord BasicMediaType
    [media-range resources mime-types extended-param-names]
  MediaType

  (get-media-range [this]))