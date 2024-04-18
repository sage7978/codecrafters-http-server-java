package builder;

import objects.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseBuilder {
    private String              method;
    private String              version;
    private int                 statusCode;
    private String              status;
    private Map<String, String> headers;
    private String              body;

    public HttpResponseBuilder(){
        this.headers = new HashMap<>();
    }

    public HttpResponseBuilder method(String method){
        this.method = method;
        return this;
    }

    public HttpResponseBuilder version(String version){
        this.version = version;
        return this;
    }

    public HttpResponseBuilder statusCode(int statusCode){
        this.statusCode = statusCode;
        return this;
    }

    public HttpResponseBuilder status(String status){
        this.status = status;
        return this;
    }

    public HttpResponseBuilder body(String body){
        this.body = body;
        return this;
    }

    public HttpResponseBuilder addHeader(String key, String value){
        this.headers.put(key, value);
        return this;
    }

    public HttpResponse build(){
        HttpResponse httpResponse = new HttpResponse(statusCode);
        httpResponse.setStatus(status);
        httpResponse.setBody(body);
        httpResponse.setMethod(method);
        httpResponse.setVersion(version);
        httpResponse.setHeaders(headers);
        return httpResponse;
    }
}
