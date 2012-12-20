# Design of Request Validation

A REST API might be described like this:

    (def my-rest-api
      (rest-api "v1.0.0"
        (media-type
          (range (type "application")
                 (sub-type "vnd.foo"))          
          (allowed-parameters "p1" "p2" "p3"))
        (media-type
          (range (type "application"))
          (allowed-parameters "s1" "w1""))))
          
          
The following 'Accept' header lines should be valid:

> Accept: application/vnd.foo
> Accept: application/vnd.foo;p1=someval
> Accept: application/vnd.foo;p1=someval, application/*
> Accept: application/vnd.foo;p1=someval, application/*;q=0.2
> Accept: application/vnd.foo;p1=someval;q=0.4
> Accept: application/*;w1=someval;q=0.7

The following 'Accept' header lines are NOT valid:

> Accept: application/foo.bar
> Accept: application/*;w2=someval;q=0.7


