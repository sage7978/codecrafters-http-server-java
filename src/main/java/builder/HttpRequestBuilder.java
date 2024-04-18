package builder;

import objects.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestBuilder {
    private String method;
    private String path;
    private String version;
    private Map<String, String> headers;

    public HttpRequestBuilder() {
        headers = new HashMap<>();
    }

    public HttpRequestBuilder method(String method){
        this.method = method;
        return this;
    }

    public HttpRequestBuilder path(String path){
        this.path = path;
        return this;
    }

    public HttpRequestBuilder version(String version){
        this.version = version;
        return this;
    }

    public HttpRequestBuilder addHeader(String key, String value){
        this.headers.put(key, value);
        return this;
    }

    public HttpRequest build(){
        HttpRequest httpRequest = new HttpRequest(method, path);
        httpRequest.setVersion(version);
        httpRequest.setHeaders(headers);
        return httpRequest;
    }

    public static HttpRequest parseFromInputStream(InputStream inputStream){
        HttpRequestBuilder builder = new HttpRequestBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String requestLine = reader.readLine();
            if(requestLine == null){
                throw new Exception("Request is Empty");
            }

            String[] requestParts = requestLine.split(" ");
            if(requestParts.length != 3){
                throw new Exception("Method, Path or Version is missing");
            }
            builder.method(requestParts[0]);
            builder.path(requestParts[1]);
            builder.version(requestParts[2]);

            String headerLine;
            while((headerLine = reader.readLine()) != null && !headerLine.isEmpty()){
                int colonIndex = headerLine.indexOf(':');
                if(colonIndex > 0){
                    String key = headerLine.substring(0, colonIndex).trim();
                    String value = headerLine.substring(colonIndex + 1).trim();
                    builder.addHeader(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return builder.build();
    }
}
