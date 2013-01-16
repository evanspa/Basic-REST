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

(ns ^{:doc "The set of protocols representing capabilities, specifically-
related to HTTP, that are needed for building HTTP-based RESTful services "
      :author "Paul Evans"}
    basicrest.protocols.http)

(defprotocol HttpRequestAcceptability
  "The capability to determine the acceptability characteristics for a given
HTTP request.

For each of the 'most-desired-accept-X' functions, there are 2 sets of data
that need to be considered.  First, there is the set of values that the request
advertises as being acceptable (expressed via one of the Accept-* headers
within the request); and second, there is the set of values that is supported
by the given acceptability abstraction (the 'this' parameter).

In the simplest case, the size of the intersection is 0, and so nil is
returned.  In the next simplest case, the size of the intersection is 1,
and so that value is returned.

In the case where the size of the intersection is greater than 1, then the
quality value provided within the request, if present, is used to determine
which value of the intersection to return.  The value with the largest
quality value wins.  As per the HTTP/1.1 specification, if a quality value
is not provided in the request for a given accepted value, then 1 is assumed.
If the quality value for each of the values in the intersection is the same,
then a random one is returned."

  (acceptable-charsets [this] "Returns the set of character sets acceptable
to the given acceptability abstraction.")

  (acceptable-encodings [this] "Returns the set of encodings acceptable to the
given acceptability abstraction.")

  (acceptable-languages [this] "Returns the set of languages acceptable to the
given acceptability abstraction.")

  (acceptable-mediatypes [this] "Returns the set of media types acceptable to
the given acceptability abstraction.")

  (default-val-if-nonstrict-acccept-header [this header-name] "Returns the
default value associated with the given accept header name for the given
acceptability abstraction.  If no default value exists, or if the given accept
header is considered strict, then nil is returned.

For example, in the HTTP/1.1 spec, some accept headers are not strictly required
to be supplied by the client; if the client omits an Accept-Charset header, the
server is allowed to assume that any character set is acceptable.  If the given
acceptability abstraciton supplied follows the guidance of the HTTP spec, then
headers such as Accept-Charset, when supplied to this function, should return
the appropriate default character set name to use.")

  (most-desired-accepted-charset [this request] "Returns the character set
that is both advertised as acceptable by the request and is supported by the
given acceptability abstraction.")

  (most-desired-accepted-encoding [this request] "Returns the encoding that
is both advertised as acceptable by the request and is supported by the given
acceptability abstraction.")

  (most-desired-accepted-language [this request] "Returns the language that is
both advertised as acceptable by the client and is supported by the given
acceptability abstraction.")

  (most-desired-accepted-mediatype [this request] "Returns the media type that
is both advertised as acceptable by the client and is supported by the given
acceptability abstraction."))

(defprotocol HttpRequestSupportability
  "The capability to determine the supportability characteristics for a given
HTTP request."

  (matched-entity-mediatype [this request] "Returns the intersection of the
media types supported by the given supportability abstraction and the media
type of the entity of the request (as documented by the 'Content-Type' header;
or nil if the media type is not supported.")

  (matched-resource [this request] "Returns the intersection of the resources
known to the supportability abstraction and the subject of the HTTP request; or
nil if the resource is not supported."))
