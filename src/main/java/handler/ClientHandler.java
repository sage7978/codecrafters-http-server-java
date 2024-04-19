package handler;

import builder.HttpRequestBuilder;
import builder.HttpResponseBuilder;
import objects.HttpRequest;
import objects.HttpResponse;

import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            HttpRequest httpRequest = HttpRequestBuilder.parseFromInputStream(clientSocket.getInputStream());
            /*       HttpRequest System Print        */
            System.out.println(httpRequest.toString());

            HttpResponse response = null;
            String body = null;

            String path = httpRequest.getPath();

            if (path.equals("/")) {
                response = new HttpResponseBuilder()
                        .version("HTTP/1.1")
                        .status("OK")
                        .statusCode(200)
                        .method("GET")
                        .addHeader("Content-Length", String.valueOf(0))
                        .addHeader("Content-Type", "text/plain")
                        .build();
            } else if (path.startsWith("/echo")) {
                body = httpRequest.getPath().substring(6);
                response = new HttpResponseBuilder()
                        .version("HTTP/1.1")
                        .status("OK")
                        .statusCode(200)
                        .method("GET")
                        .body(body)
                        .addHeader("Content-Length", String.valueOf(body.length()))
                        .addHeader("Content-Type", "text/plain")
                        .build();
            } else if (path.equals("/user-agent")) {
                body = httpRequest.getHeaders().getOrDefault("User-Agent", "");
                response = new HttpResponseBuilder()
                        .version("HTTP/1.1")
                        .status("OK")
                        .statusCode(200)
                        .method("GET")
                        .addHeader("Content-Length", String.valueOf(body.length()))
                        .addHeader("Content-Type", "text/plain")
                        .body(body)
                        .build();
            } else {
                response = new HttpResponseBuilder()
                        .version("HTTP/1.1")
                        .status("NOT FOUND")
                        .statusCode(404)
                        .method("GET")
                        .addHeader("Content-Length", String.valueOf(0))
                        .addHeader("Content-Type", "text/plain")
                        .build();
            }

            OutputStream os = clientSocket.getOutputStream();
            os.write(response.parseResponse().getBytes());
            os.close();

            clientSocket.close();

        } catch (Exception ex){
            System.out.println("Caught Exception ex: " + ex.getMessage());
        }
    }
}