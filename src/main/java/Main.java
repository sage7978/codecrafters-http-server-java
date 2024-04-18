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
           httpRequest.setPath(httpRequest.getPath() + " ");
           String[] paths = httpRequest.getPath().split("/");
//           System.out.println(Arrays.toString(paths));
           HttpResponse response = null;
           if(paths.length == 0){
               response = new HttpResponseBuilder()
                       .version("HTTP/1.1")
                       .status("OK")
                       .statusCode(200)
                       .method("GET")
                       .build();
           }
           else if("echo".equals(paths[1])){
               String body = paths[paths.length-1].trim();
               response = new HttpResponseBuilder()
                       .version("HTTP/1.1")
                       .status("OK")
                       .statusCode(200)
                       .method("GET")
                       .body(body)
                       .addHeader("Content-Length", String.valueOf(body.length()))
                       .addHeader("Content-Type", "text/plain")
                       .build();
           }
           else {
               response = new HttpResponseBuilder()
                       .version("HTTP/1.1")
                       .status("NOT FOUND")
                       .statusCode(404)
                       .method("GET")
                       .build();
           }

           String randomString = httpRequest.getPath().substring(httpRequest.getPath().lastIndexOf('/')+1);

           OutputStream os = clientSocket.getOutputStream();

           os.write(response.parseResponse().getBytes());
           os.close();
           clientSocket.close();
       }
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
