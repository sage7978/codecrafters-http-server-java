import builder.HttpRequestBuilder;
import builder.HttpResponseBuilder;
import handler.ClientHandler;
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

    private static volatile boolean running = true;

  public static void main(String[] args) {
    System.out.println("Logs from your program will appear here!");

     ServerSocket serverSocket = null;

     try {
       serverSocket = new ServerSocket(4221);
       serverSocket.setReuseAddress(true);

       while(running) {
           Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
           System.out.println("accepted new connection");

           ClientHandler clientHandler = new ClientHandler(clientSocket);
           Thread thread = new Thread(clientHandler);

           thread.start();
       }
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
