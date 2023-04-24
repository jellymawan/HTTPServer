Last login: Sun Apr 23 15:17:38 on ttys001                                       

```(base) jelee@MacBook-Pro-156 ~ % curl localhost/example.txt -v
*   Trying ::1:80...
* Connected to localhost (::1) port 80 (#0)
> GET /example.txt HTTP/1.1
> Host: localhost
> User-Agent: curl/7.71.1
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
< Content-type: text/plain
< Content-length: 35
< 
* Excess found in a read: excess = 4, size = 35, maxdownload = 35, bytecount = 0
* Closing connection 0
This is a test file for HttpServer.%                                            

(base) jelee@MacBook-Pro-156 ~ % curl -X POST -d 'I appended to this' localhost/example.txt -v
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying ::1:80...
* Connected to localhost (::1) port 80 (#0)
> POST /example.txt HTTP/1.1
> Host: localhost
> User-Agent: curl/7.71.1
> Accept: */*
> Content-Length: 18
> Content-Type: application/x-www-form-urlencoded
> 
* upload completely sent off: 18 out of 18 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
< Content-type: text/plain
< Content-length: 53
< 
Check the file!

* transfer closed with 34 bytes remaining to read
* Closing connection 0
curl: (18) transfer closed with 34 bytes remaining to read

(base) jelee@MacBook-Pro-156 ~ % curl -X PUT -d 'new file who this' localhost/newFile.txt -v
*   Trying ::1:80...
* Connected to localhost (::1) port 80 (#0)
> PUT /newFile.txt HTTP/1.1
> Host: localhost
> User-Agent: curl/7.71.1
> Accept: */*
> Content-Length: 17
> Content-Type: application/x-www-form-urlencoded
> 
* upload completely sent off: 17 out of 17 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 201 Created
< Content-type: text/plain
< Content-length: 17
< 


* transfer closed with 13 bytes remaining to read
* Closing connection 0
curl: (18) transfer closed with 13 bytes remaining to read

(base) jelee@MacBook-Pro-156 ~ % curl -X DELETE localhost/newFile.txt -v
*   Trying ::1:80...
* Connected to localhost (::1) port 80 (#0)
> DELETE /newFile.txt HTTP/1.1
> Host: localhost
> User-Agent: curl/7.71.1
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
< Content-type: text/plain
* no chunk, no close, no size. Assume close to signal end
< 
File deleted.


* Closing connection 0
(base) jelee@MacBook-Pro-156 ~ % curl --head localhost/example.txt
HTTP/1.1 200 OK
(base) jelee@MacBook-Pro-156 ~ % 
```


