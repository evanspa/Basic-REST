(ns basicrest.protocols.http-request-processor)

(defprotocol HttpRequestProcessor

  ""

  (matched-resource [this request]))