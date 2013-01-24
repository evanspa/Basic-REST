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

(ns ^{:doc "Unit tests."
      :author "Paul Evans"}
  basicrest.types.core-test
  (:use [clojure.test])
  (:use [clojure.set :only [difference]])
  (:require [basicrest.types.core])
  (:use [basicrest.protocols.http :only
         [HttpRequestAcceptability
          acceptable-charsets
          acceptable-encodings
          acceptable-languages
          acceptable-mediatypes
          nonstrict-accept-default
          HttpRequestSupportability]])
  (:import [basicrest.types.core RestApi MediaRange MediaType Resource]))

(def ^{:doc "Simple resource useful for doing various sanity-check type
testing."} Resource_1
  (Resource.
   (fn [mt-ext-params res req] true) ; matcher fn
   {:xml {:de-serializer
          (fn [mt-ext-params res req]
            "foo")
          :serializer
          (fn [mt-ext-params res req]
            "<foo />")}}
   {:get (fn [mt-ext-params res req] "foo")}))

(def ^{:doc "Simple media type, that contains a single, simple resource useful
for doing various sanity-check type testing."} MediaType_1
  (MediaType.
   (MediaRange. "application" "vnd.foo.com")
   #{} ; extension params (none)
   (fn [mt req] true) ; matcher fn
   Resource_1)) ; supported resources

(def ^{:doc "Simple REST API, that contains a single, simple media type useful
for doing various sanity-check type testing."} RestApi_1
  (RestApi.
   "v1.0.0"
   #{"Identity"} ; supported encodings
   #{"en", "en-US"} ; supported languages
   #{:plus-sign-suffixed-format-specifiers},
   {"accept-charset" "UTF-8" ; supported non-strict accept defaults
    "accept-encoding" "Identity"}
   {:prepend-restapi-version true} ; URI builder config
   (fn [uri] uri) ; URI transformation fn
   #{MediaType_1})) ; supported media types

(deftest
  ^{:doc "Ensure that at least some character sets are acceptable _This is sort
of a lame test; I need to refactor the implementation such that it should
somehow be pluggable how the character sets are discovered_."}
  test-acceptable-charsets_1
  (is (> (count (acceptable-charsets RestApi_1)) 0)))

(deftest
  ^{:doc "Ensures the acceptable-encodings function behaves as expected."}
  test-acceptable-encodings_1
  (is (= (count (difference #{"Identity"} (acceptable-encodings RestApi_1)))
         0)))

(deftest
  ^{:doc "Ensures the acceptable-languages function behaves as expected."}
  test-acceptable-languages_1
  (is (= (count (difference #{"en-US" "en"} (acceptable-languages RestApi_1)))
         0)))

(deftest
  ^{:doc "Ensures the acceptable-types function behaves as expected."}
  test-acceptable-mediatypes_1
  (is (= (count (acceptable-mediatypes RestApi_1)) 1)))

(deftest
  ^{:doc "Ensures the expected behavior of the nonstrict-accept-default
function."}
  test-nonstrict-accept-default_1
  (is (nil? (nonstrict-accept-default RestApi_1 "accept")))
  (is (= (nonstrict-accept-default RestApi_1 "accept-charset") "UTF-8"))
  (is (= (nonstrict-accept-default RestApi_1 "accept-encoding") "Identity"))))))