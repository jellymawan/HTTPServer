# HTTPServer

#### For some reason, I couldn't get this to work with Postman, but curl works perfectly fine.

To run the server, type ```java HttpServer.java``` in terminal/command line and type in ```localhost/example.txt``` to get started. You can navigate between these paths:
 - example.txt
 - example2.txt
 - 200.jpg
 
To try POST, type ```curl -X POST -d 'insert text here' localhost/example.txt -v``` into terminal/command line.
 
To try PUT, use the example below in the terminal/command line:
 - ```curl -X PUT -d 'insert text here' localhost/filename.txt```

To try DELETE, type ```curl -X DELETE localhost/example.txt -v``` into terminal/command line

To try HEAD, type ```curl --head localhost/example.txt``` into terminal/command line

To try to get the 401 error code, use PUT to make a new file, close the server, and try to GET the file
 
## Assignment Information
### Creating an HTTP Server
For this assignment, you will be building your own HTTP server that implements HTTP/1.1 (RFC2616)Links to an external site.!

You don't have to read the whole RFC, but you do need to understand the following:

HTTP Message Structure

HTTP Request-Line and Status-Line

HTTP Methods

HTTP Headers (Content-Type and Content-Length)

MIME Types

Status Codes
 

### Rubric (10 pts)
GET Links to an external site. (2 pts): 
- retrieve static file resource requested
- Make sure Content-Type is correct.
- Make sure Content-Length is correct.
- Your application should support at least two MIME types.
Example

1. If /example.txt exists, your server responds back with 200 OK, the correct response headers, and the content of that example.txt file in the response body.

2. If /example.txt doesn't exist, your server responds back with an error code.

POST Links to an external site.(2 pts): 
- append the body of the request to the resource
- text/plain types only

PUT Links to an external site.(2 pts): 
- puts the body of the request to the resource
- Creates a new file using the body of the request.
- Overrides the content if the file already exists.

DELETE Links to an external site. (2 pts): 
- delete the resource

Support the right response codes. (2 pts):

Any response code that fits; use your best judgement
