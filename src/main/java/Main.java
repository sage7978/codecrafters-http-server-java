import builder.HttpRequestBuilder;
import builder.HttpResponseBuilder;
import objects.HttpRequest;
import objects.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

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
           /*       HttpRequest System Print        */
           System.out.println(httpRequest.toString());

           String randomString = httpRequest.getPath().substring(httpRequest.getPath().lastIndexOf('/')+1);

           OutputStream os = clientSocket.getOutputStream();
           HttpResponse response = new HttpResponseBuilder()
                                                    .version("HTTP/1.1")
                                                       .status("OK")
                                                       .statusCode(200)
                                                       .method("GET")
                                                       .body(randomString)
                                                        .build();

           os.write(response.parseResponse().getBytes());
           os.close();
           clientSocket.close();
       }
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
