(ns basicrest.types.resource)

(defrecord Resource
  [matcher-fn
   serializers
   http-method-fns])