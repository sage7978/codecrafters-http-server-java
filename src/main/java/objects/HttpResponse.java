package objects;

import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private String              method;
    private String              version;
    private int                 statusCode;
    private String              status;
    private Map<String, String> headers;
    private String              body;

    public HttpResponse(int statusCode){
        this.statusCode = statusCode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String parseResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append(version);
        sb.append(" ");
        sb.append(statusCode);
        sb.append(" ");
        sb.append(status);
        sb.append("\r\n");
        if(Objects.nonNull(headers)){
            for(Map.Entry<String, String> entry: headers.entrySet()){
                sb.append(entry.getKey());
                sb.append(": ");
                sb.append(entry.getValue());
                sb.append("\r\n");
            }
            sb.append("\r\n");
        }
        if(Objects.nonNull(body)) {
            sb.append(body);
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
