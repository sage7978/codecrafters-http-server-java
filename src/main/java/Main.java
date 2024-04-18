import builder.HttpRequestBuilder;
import objects.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage

     ServerSocket serverSocket = null;
     Socket clientSocket = null;

     try {
       serverSocket = new ServerSocket(4221);
       serverSocket.setReuseAddress(true);

       while(true) {
           clientSocket = serverSocket.accept(); // Wait for connection from client.
           System.out.println("accepted new connection");

           HttpRequest httpRequest = HttpRequestBuilder.parseFromInputStream(clientSocket.getInputStream());
           System.out.println(httpRequest.toString());

           OutputStream os = clientSocket.getOutputStream();

           if(httpRequest.getPath().charAt(0) != '/'){
               sendBadResponse(os);
           }
           else sendGoodResponse(os);

           os.close();
           clientSocket.close();
       }
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }

    private static void sendGoodResponse(OutputStream os) throws IOException {
        String content = "HTTP/1.1 200 OK\r\n\r\n";
        os.write(content.getBytes());
    }

    private static void sendBadResponse(OutputStream os) throws IOException {
        String content = "HTTP/1.1 404 NOT FOUND\r\n\r\n";
        os.write(content.getBytes());
    }
}
