(ns basicrest.impl.util
  (:import (java.nio.charset Charset IllegalCharsetNameException)))

(defn is-accept-strict?
  "Returns ..."
  [nonstrict-accept-defaults accept-hdr-name]
  (nil? (get nonstrict-accept-defaults accept-hdr-name)))

(defn get-header-val
  "Returns ..."
  [request hdr-name]
  (get (:headers request) hdr-name))

(defn is-accept-hdr-val-acceptable-if-strict?
  "Returns ..."
  [nonstrict-accept-defaults
   accept-hdr-name
   is-accept-hdr-val-acceptable?
   request]
  (if (is-accept-strict? nonstrict-accept-defaults accept-hdr-name)
    (let [accept-val (get-header-val request accept-hdr-name)]
      (is-accept-hdr-val-acceptable? accept-val))
    true))

(defn is-charset-available-on-system?
  [charset-name]
  (try
    (Charset/isSupported charset-name)
    (catch IllegalCharsetNameException exc
      false)))

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