(ns basicrest.protocols.http-request-supportability)

(defprotocol HttpRequestSupportability

  "The capability to determine the supportability characteristics for a given
HTTP request."

  (matched-entity-mediatype [this request]
    "Returns the media type of the request entity, as advertised by the
'Content-Type' header; or nil if the media type is not supported by the
supportability abstraction."))