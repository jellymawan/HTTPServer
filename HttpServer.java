import java.net.*;
import java.io.*;
import java.util.HashMap;

public class HttpServer {

    static String requestURL;

    public static void main(String[] args) throws IOException {
        int port = 80;
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
                requestURL = line.substring(line.indexOf("/") + 1, line.indexOf("HTTP") - 1);

                if(line.contains("GET")) {
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
                    }else {
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
                } else if (line.contains("POST")) {
                    String requestBody = "";
                    if (path.get("/" + requestURL).equals("text/plain")) { //can only use POST on text/plain types
                        while(!line.isEmpty()) { //parses through until it reaches an empty line
                            //System.out.println(line);
                            line = in.readLine();
                        }
                        requestBody = in.readLine(); //after the empty line, the body of the request
                        //System.out.println("Word to be appended: " + requestBody);
                        File f = new File("./" + requestURL);
                        //System.out.println("Before append, file length: " + f.length());
                        FileWriter fw = new FileWriter(f, true);
                        BufferedWriter br = new BufferedWriter(fw);
                        br.write(requestBody);

                        br.close();
                        fw.close();

                        //System.out.println("After append, file length: " + f.length());

                        FileInputStream fis = new FileInputStream(requestURL);
                        BufferedInputStream bufInputStream = new BufferedInputStream(fis);
                        byte[] bytes = new byte[(int) f.length()];
                        bufInputStream.read(bytes);

                        //OutputStream os = clientSocket.getOutputStream();
                        //os.write("HTTP/1.1 200 OK".getBytes());
                        //os.write("\r\n\r\n".getBytes()); //Currently, this needs to be commented out. After sending the curl, the client waits for a response, and so does the server. I need to exit out of the client
                        //in order for the server to process the request.
                        //os.write(bytes);
                        //os.write("\r\n\r\n".getBytes());

                        //os.flush();
                        fis.close();
                        //fis.close();
                        bufInputStream.close();
                        //os.close();
                    }
                }
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
