import java.net.*;
import java.io.*;
import java.util.*;

public class HttpServer {

    static String requestURL = "";
    static String requestLine = "";
    static String bodyLine = "";

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

                parser(in);

                String line = requestLine;
                requestURL = requestLine.substring(requestLine.indexOf("/") + 1, requestLine.indexOf("HTTP") - 1);

                if(line.contains("GET")) {
                    if(path.containsKey("/" + requestURL)) { //if its a known path
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
                        File f = new File("./404.jpeg");
                        byte[] bytes = new byte[(int) f.length()];
                        FileInputStream fis = new FileInputStream("404.jpeg");
                        BufferedInputStream bufInputStream = new BufferedInputStream(fis);
                        bufInputStream.read(bytes);

                        OutputStream os = clientSocket.getOutputStream();
                        os.write("HTTP/1.1 404 Not Found".getBytes());
                        os.write("\r\n\r\n".getBytes());
                        os.write(bytes);
                        os.write("\r\n\r\n".getBytes());

                        os.flush();
                    }
                } else if (line.contains("POST")) {
                    if (path.get("/" + requestURL).equals("text/plain")) { //can only use POST on text/plain types
                        System.out.println("POST request");

                        System.out.println("Word to be appended: " + bodyLine);
                        File f = new File("./" + requestURL);
                        FileWriter fw = new FileWriter(f, true);
                        BufferedWriter br = new BufferedWriter(fw);
                        br.write(bodyLine);

                        FileInputStream fis = new FileInputStream(requestURL);
                        BufferedInputStream bufInputStream = new BufferedInputStream(fis);
                        byte[] bytes = new byte[(int) f.length()];
                        bufInputStream.read(bytes);

                        OutputStream os = clientSocket.getOutputStream();
                        os.write("HTTP/1.1 200 OK\r\n".getBytes());
                        os.write(("Content-length: " + bytes.length).getBytes());
                        os.write("\r\n\r\n".getBytes());
                        os.write("Check the file!".getBytes());
                        os.write("\r\n\r\n".getBytes());

                        os.flush();
                        fis.close();
                        bufInputStream.close();
                        br.close();
                        fw.close();
                        os.close();
                    }
                } else if (line.contains("DELETE")) {
                    File f = new File("./" + requestURL);
                    f.delete();
                    path.remove("/" + requestURL);
                    System.out.println("File deleted successfully");

                    OutputStream os = clientSocket.getOutputStream();
                    os.write("HTTP/1.1 200 OK".getBytes());

                } else if (line.contains("PUT")) {
                    File f = new File("./" + requestURL);
                    if (!f.exists()) {
                        f.createNewFile();

                        //parse through body
                    } else {
                        //FileWriter fw = new FileWriter(, false);
                        //fw.write();
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

    public static void parser(BufferedReader bufferedReader) throws IOException {
        String contentLength = "";
        requestLine = bufferedReader.readLine();
        String inputLine = bufferedReader.readLine();
        while (inputLine.length() > 0) { //header
            inputLine = bufferedReader.readLine();
            if (inputLine.contains("Content-Length")) {
                contentLength = inputLine;
            }
            System.out.println(inputLine);
        }

        if (!requestLine.contains("GET")) { //parses body
            int index = contentLength.indexOf(":");
            String length = contentLength.substring(index + 2);
            int num = Integer.parseInt(length);

            if (num > 0) {
                for(int i = 0; i < num; i++) {
                    bodyLine += (char)bufferedReader.read();
                    System.out.println(bodyLine);
                }
            }
        }

    }

}
