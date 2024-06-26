package handler;

import builder.HttpRequestBuilder;
import builder.HttpResponseBuilder;
import objects.HttpRequest;
import objects.HttpResponse;

import java.io.File;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final Map<String, String> arguments;

    public ClientHandler(Socket clientSocket, Map<String, String> arguments) {
        this.clientSocket = clientSocket;
        this.arguments = arguments;
    }

    @Override
    public void run() {
        try {
            HttpRequest httpRequest = HttpRequestBuilder.parseFromInputStream(clientSocket.getInputStream());
            /*       HttpRequest System Print        */
            System.out.println(httpRequest.toString());
            System.out.println("arguments: " + arguments);

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
                body = httpRequest.getPath().substring("/echo".length()+1);
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
            } else if(isFileInDirectory(path)) {
                String directoryPath = arguments.get("directory");
                String fileName = path.substring("/files/".length());
                Path filePath = Paths.get(directoryPath, fileName).normalize();
                body = new String(Files.readAllBytes(filePath));
                response = new HttpResponseBuilder()
                        .version("HTTP/1.1")
                        .status("OK")
                        .statusCode(200)
                        .method("GET")
                        .addHeader("Content-Length", String.valueOf(body.length()))
                        .addHeader("Content-Type", "application/octet-stream")
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

    private boolean isFileInDirectory(String path) {
        if(!path.startsWith("/files/")){
            return false;
        }
        if(!arguments.containsKey("directory")){
            return false;
        }
        System.out.println("Directory: " + arguments.get("directory"));
        System.out.println("File Name: " + path.substring("/files/".length()));
        String directoryPath = arguments.get("directory");
        String fileName = path.substring("/files/".length());
        Path filePath = Paths.get(directoryPath, fileName).normalize();
        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }
}
