A middleware function can both augment the request and augment
the response.  Chances are, your middleware functions will
only augment the request, or only augment the response.  It
might get confusing and/or complicated if you created middleware
that does both.  All middleware functions should invoke the
the handler function that it is passed as part of the
implementation of the function that it returns.

Here's an example of a middleware function that augments the
response by adding a header: resp

((fn [handler]
   (fn [req]
     (let [resp (handler req)
           respHdrs (:headers resp)]
       (assoc resp :headers (assoc respHdrs "resp" "resp-hdr-val"))))))

Here's an example of a middleware function that augments the
request (by adding a header) before invoking the handler:

((fn [handler]
   (fn [req]
     (let [reqHdrs (:headers req)]
       (handler (assoc req :headers
                       (assoc reqHdrs "req" "req-hdr-val")))))))

The following illustrates which middleware the ordering with respect
to augmenting the request map and the response map.  I.e., if
both request-enhancer-1-response-enhancer-3 and
request-enhancer-3-response-enhancer-1 augment the request map by
adding a new request header of the same name,
request-enhancer-3-response-enhancer-1's value will win.  If both
functions modify the response in a similar manner, then
request-enhancer-1-response-enhancer-3's value will win.
                       
(->
 main-routes
 (request-enhancer-3-response-enhancer-1)
 (request-enhancer-2-response-enhancer-2)
 (request-enhancer-1-response-enhancer-3)

(request-enhancer-1-response-enhancer-3
 (request-enhancer-2-response-enhancer-2
  (request-enhancer-3-response-enhancer-1
   main-routes)

It should also be noted that middleware functions whose intent is to
perform some sort of validation may not actually invoke the handler
function.  For example, if after inspecting the request there is
deemed to be something illegal, the middleware may simply return
response map itself (of its own creation --- probably with some
appropriate status code set) having never even invoked the handler function.
