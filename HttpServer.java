import java.net.*;
import java.io.*;
import java.util.HashMap;

public class HttpServer {

    public static void main(String[] args) throws IOException {
        int port = 80;
        String requestURL = "";
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server running on port: " + port);

        HashMap<String, String> path = new HashMap<>();
        path.put("/example.txt", "text/plain");
        path.put("/200.jpeg", "image/jpeg");
        path.put("/example2.txt", "text/plain");
        //path.put("/how-to-hack-Ted.txt", "text/plain"); //XC
        //path.put("/rated-r.txt", "text/plain"); //XC

        while (true) {
            try {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connected");

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


                String line = in.readLine();
                if(line.contains("GET")) {

                    requestURL = line.substring(line.indexOf("/") + 1, line.indexOf("HTTP") - 1);
                    System.out.println(requestURL);

                    if(path.containsKey("/" + requestURL)) {
                        File f = new File("./" + requestURL);
                        byte[] bytes = new byte[(int) f.length()];

                        FileInputStream fis = new FileInputStream(requestURL);
                        BufferedInputStream bufInputStream = new BufferedInputStream(fis);
                        bufInputStream.read(bytes);
                        OutputStream os = clientSocket.getOutputStream();

                        os.write("HTTP/1.1 200 OK\r\n".getBytes());
                        os.write(("Content-type: " + path.get("/" + requestURL) + "\r\n").getBytes());
                        os.write(("Content-length: " + bytes.length).getBytes());
                        os.write("\r\n\r\n".getBytes());
                        os.write(bytes);
                        os.write("\r\n\r\n".getBytes());

                        os.flush();
                        fis.close();
                        fis.close();
                        bufInputStream.close();
                        os.close();
                    } else {
//                    String http404Response = "HTTP/1.1 404 Not Found\r\n\r\n" + "<!DOCTYPE html>\n" +
//                            "<html>\n" +
//                            "\n" +
//                            "<head>\n" +
//                            "    <title>Not Found</title>\n" +
//                            "</head>\n" +
//                            "\n" +
//                            "<body>\n" +
//                            "<img src=\"/404.jpeg\">\n" +
//                            "</body>\n" +
//                            "\n" +
//                            "</html>";

                        File f = new File("./404.jpeg");
                        byte[] bytes = new byte[(int) f.length()];
                        FileInputStream fis = new FileInputStream("404.jpeg");
                        BufferedInputStream bufInputStream = new BufferedInputStream(fis);
                        bufInputStream.read(bytes);

                        OutputStream clientOutput = clientSocket.getOutputStream();
                        //clientOutput.write(http404Response.getBytes());
                        clientOutput.write("HTTP/1.1 404 Not Found".getBytes());
                        clientOutput.write("\r\n\r\n".getBytes());
                        clientOutput.write(bytes);
                        clientOutput.write("\r\n\r\n".getBytes());

                        clientOutput.flush();
                    }
                }
//                } else if(line.contains("POST")) {
//                }




//                while(!line.isEmpty()) {
//                    System.out.println(line);
//
//                    line = in.readLine();
//                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
