(ns basicrest.media-type)

(defprotocol MediaRange
  "Abstraction modeling the notion of the 'media range' associated with
internet media types."
  (get-type [this]
    "Returns the type of the media rnage.")
  (get-subtype [this]
    "Returns the subtype of the media range."))

(defprotocol MediaType
  "Abstraction modeling the notion of an internet media type."
  (get-media-range [this]
    "Returns the media range associated with the media type.")
  (get-quality-value [this]
    "Returns the quality value associated with the media type.")
  (get-extended-params [this]
    "Returns the extension parameters associated with the media type.")
  (get-mime-types [this]
    "Returns the set of MIME types supported by the media type.")
  (get-resources [this]))
