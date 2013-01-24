;   Copyright (c) 2013 Paul Evans

;   Permission is hereby granted, free of charge, to any person obtaining a
;   copy of this software and associated documentation files (the "Software"),
;   to deal in the Software without restriction, including without limitation
;   the rights to use, copy, modify, merge, publish, distribute, sublicense,
;   and/or sell copies of the Software, and to permit persons to whom the
;   Software is furnished to do so, subject to the following conditions:

;   The above copyright notice and this permission notice shall be included
;   in all copies or substantial portions of the Software.

;   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
;   OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
;   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
;   THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
;   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
;   FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
;   DEALINGS IN THE SOFTWARE.

(ns ^{:doc "Collection of helper functions."
      :author "Paul Evans"}
    basicrest.util
  (:import [java.nio.charset Charset IllegalCharsetNameException]))

(defn get-header-val
  "Returns the value of the header with name hdr-value in request; if the
header does not exist, nil is returned."
  [request hdr-name]
  (get (:headers request) hdr-name))

(defn contains-entity?
  "Returns true if the given HTTP request contains an entity in the request
body; otherwise returns false."
  [request]
  (let [method (:request-method request)]
    (or (= method :put)
        (= method :post))))

(defn is-charset-available-on-system?
  "Returns true if the given character set, specified by its canonical name, is
supported by the system this code is running on; otherwise returns false."
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
        => [{:media-range {:type 'text :subtype 'html}}
            {:media-range {:type 'application :subtyle 'xml}}]"
  [accept-hdr-value]
  ;TODO
  )