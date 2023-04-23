import java.net.*;
import java.io.*;
import java.util.*;

public class HttpServer {

    static String requestURL = "";
    static String requestLine = "";
    static String bodyLine = "";
    static int port = 80;
    static OutputStream os;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server running on port: " + port);

        HashMap<String, String> path = new HashMap<>();
        path.put("/example.txt", "text/plain");
        path.put("/200.jpeg", "image/jpeg");
        path.put("/example2.txt", "text/plain");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connected");

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = clientSocket.getOutputStream();

                parser(in);

                String line = requestLine;
                requestURL = requestLine.substring(requestLine.indexOf("/") + 1, requestLine.indexOf("HTTP") - 1);
                File f = new File("./" + requestURL);

                if (!line.contains("PUT") && f.exists() && !path.containsKey("/" + requestURL)) {
                    f = new File("./401.jpeg");
                    byte[] bytes = new byte[(int) f.length()];
                    FileInputStream fis = new FileInputStream("401.jpeg");
                    BufferedInputStream bufInputStream = new BufferedInputStream(fis);
                    bufInputStream.read(bytes);

                    os.write("HTTP/1.1 401 Unauthorized\r\n".getBytes());
                    os.write(("Content-type: text/html"+ "\r\n").getBytes());
                    os.write("\r\n\r\n".getBytes());
                    os.write(bytes);
                    os.write("\r\n\r\n".getBytes());
                } else if(line.contains("GET")) {
                    if(path.containsKey("/" + requestURL)) { //if it's a known path
                        byte[] bytes = new byte[(int) f.length()];

                        FileInputStream fis = new FileInputStream(requestURL);
                        BufferedInputStream bufInputStream = new BufferedInputStream(fis);
                        bufInputStream.read(bytes);

                        os.write("HTTP/1.1 200 OK\r\n".getBytes());
                        os.write(("Content-type: " + path.get("/" + requestURL) + "\r\n").getBytes());
                        os.write(("Content-length: " + bytes.length).getBytes());
                        os.write("\r\n\r\n".getBytes());
                        os.write(bytes);
                        os.write("\r\n\r\n".getBytes());

                        fis.close();
                        fis.close();
                        bufInputStream.close();
                    }else { //file does not exist
                        f = new File("./404.jpeg");
                        byte[] bytes = new byte[(int) f.length()];
                        FileInputStream fis = new FileInputStream("404.jpeg");
                        BufferedInputStream bufInputStream = new BufferedInputStream(fis);
                        bufInputStream.read(bytes);

                        os.write("HTTP/1.1 404 Not Found".getBytes());
                        os.write("\r\n\r\n".getBytes());
                        os.write(bytes);
                        os.write("\r\n\r\n".getBytes());
                    }
                } else if (line.contains("POST")) {
                    if (path.get("/" + requestURL).equals("text/plain")) { //can only use POST on text/plain types

                        f = new File("./" + requestURL);
                        FileWriter fw = new FileWriter(f, true);
                        BufferedWriter br = new BufferedWriter(fw);
                        br.write(bodyLine);
                        br.close();
                        fw.close();

                        FileInputStream fis = new FileInputStream(requestURL);
                        BufferedInputStream bufInputStream = new BufferedInputStream(fis);
                        byte[] bytes = new byte[(int) f.length()];
                        bufInputStream.read(bytes);

                        os.write("HTTP/1.1 200 OK\r\n".getBytes());
                        os.write(("Content-type: text/plain\r\n").getBytes());
                        os.write(("Content-length: " + bytes.length).getBytes());
                        os.write("\r\n\r\n".getBytes());
                        os.write("Check the file!".getBytes());
                        os.write("\r\n\r\n".getBytes());

                        fis.close();
                        bufInputStream.close();
                    }
                } else if (line.contains("DELETE")) {
                    f = new File("./" + requestURL);
                    f.delete();
                    String contentType = path.get("/" + requestURL);
                    path.remove("/" + requestURL);
                    System.out.println("File deleted successfully");

                    os.write("HTTP/1.1 200 OK\r\n".getBytes());
                    os.write(("Content-type: " + contentType).getBytes());
                    os.write("\r\n\r\n".getBytes());
                    os.write("File deleted.".getBytes());
                    os.write("\r\n\r\n".getBytes());

                } else if (line.contains("PUT")) {
                    f = new File("./" + requestURL);
                    if (!f.exists()) { //if file does not exist
                        f.createNewFile(); //create new file
                        path.put("/" + requestURL, "text/plain"); //I am forcing it to be a txt file
                        FileWriter fw = new FileWriter(f, true);
                        BufferedWriter br = new BufferedWriter(fw);
                        br.write(bodyLine);

                        br.close();
                        fw.close();

                        FileInputStream fis = new FileInputStream("./" + requestURL);
                        BufferedInputStream bufInputStream = new BufferedInputStream(fis);
                        byte[] bytes = new byte[(int) f.length()];
                        bufInputStream.read(bytes);

                        if (bodyLine.equals("")) { //if there is no body -- creates an empty file
                            os.write("HTTP/1.1 204 No Content\r\n".getBytes());
                            os.write(("Content-type: text/plain\r\n").getBytes());
                            os.write(("Content-length: " + bytes.length).getBytes());
                            os.write("\r\n\r\n".getBytes());
                            //os.write("New empty file created.".getBytes());
                            os.write("\r\n\r\n".getBytes());

                            fis.close();
                            bufInputStream.close();
                        } else { // if there is a body, creates a file with the body as the contents
                            os.write("HTTP/1.1 201 Created\r\n".getBytes());
                            os.write(("Content-type: text/plain\r\n").getBytes());
                            os.write(("Content-length: " + bytes.length).getBytes());
                            os.write("\r\n\r\n".getBytes());
                            //os.write("New file created with contents.".getBytes());
                            os.write("\r\n\r\n".getBytes());

                            fis.close();
                            bufInputStream.close();
                        }

                    } else { //if the file exists
                        FileWriter fw = new FileWriter(f , false); //overwrite the file
                        BufferedWriter br = new BufferedWriter(fw);
                        br.write(bodyLine);
                        br.close();
                        fw.close();

                        FileInputStream fis = new FileInputStream(requestURL);
                        BufferedInputStream bufInputStream = new BufferedInputStream(fis);
                        byte[] bytes = new byte[(int) f.length()];
                        bufInputStream.read(bytes);

                        os.write("HTTP/1.1 201 Created\r\n".getBytes());
                        os.write(("Content-type: text/plain\r\n").getBytes());
                        os.write(("Content-length: " + bytes.length).getBytes());
                        os.write("\r\n\r\n".getBytes());
                        //os.write("New file created with contents.".getBytes());
                        os.write("\r\n\r\n".getBytes());

                        fis.close();
                        bufInputStream.close();
                    }
                }
                os.flush();
                os.close();
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void parser(BufferedReader bufferedReader) throws IOException { //Parses request
        String contentLength = "";
        requestLine = bufferedReader.readLine();
        String inputLine = bufferedReader.readLine();
        while (inputLine.length() > 0) { //header
            inputLine = bufferedReader.readLine();
            if (inputLine.contains("Content-Length")) {
                contentLength = inputLine;
            }
        }

        if (!requestLine.contains("GET")) { //parses body
            bodyLine = "";
            int index = contentLength.indexOf(":");
            if(index != -1) {
                String length = contentLength.substring(index + 2);
                int num = Integer.parseInt(length);
                if (num > 0) {
                    for(int i = 0; i < num; i++) {
                        bodyLine += (char)bufferedReader.read();
                    }
                }
            }
        }
    }
}
