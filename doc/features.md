# Features
The following is a terse set of features that BasicREST will aspire to provide.

+ The ability to express a REST API as a composition of media types
+ The ability to express a media type as a composition of resources
+ The ability to create a Ring handler function from a REST API instance
+ The ability to provide full support for request validation subject
to the rules of the HTTP spec.

The ability to provide validation of HTTP requests according to the
composed media types (the validation rules described by the HTTP spec)
will require some fun coding (the rules defined in the HTTP spec are
not trivial, and allow for some rich expression of what a media type
can accept).  
