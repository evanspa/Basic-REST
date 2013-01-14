(ns basicrest.types.resource-entities)

(defrecord AcceptabilityEntity
    [supported-mediatypes
     supported-charsets
     supported-encodings
     supported-languages])