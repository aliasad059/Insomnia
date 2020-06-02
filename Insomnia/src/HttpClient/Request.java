package HttpClient;

import GUI.InsomniaFrame;
import GUI.MiddlePanel;
import GUI.ResponsePanel;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * request
 */
public class Request implements Serializable {
    private MiddlePanel middlePanel;
    private ResponsePanel responsePanel;
    private JLabel methodLabel;
    private JButton requestButton;
    private HttpRequest request;
    private boolean completed;
    private HttpResponse<String> lastResponse;
    private String uri, method, headers, output, data, json, upload;
    private boolean showResponseHeaders, followRedirect, saveRequest;

    public Request() {
        middlePanel = new MiddlePanel();
        responsePanel = new ResponsePanel();
        middlePanel.setOwner(this);
    }


    /**
     * get follow redirect
     *
     * @return follow redirect value
     */
    public boolean getFollowRedirect() {
        return followRedirect;
    }

    /**
     * get show response headers
     *
     * @return show response headers value
     */
    public boolean GetShowResponseHeaders() {
        return showResponseHeaders;
    }


    /**
     * makes an HTTP REQUEST as entered interpreted args
     *
     * @return https request
     */
    public HttpRequest makeRequest() {
        //get and delete methods are made in a same form
        if (method.equals("GET") || method.equals("DELETE")) {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .setHeader("User-Agent", "Insomnia")
                    .uri(URI.create(uri));
            if (method.equals("GET"))
                builder.GET();
            else builder.DELETE();
            if (headers != null) {
                String[] pairs = headers.split(";");
                for (int i = 0; i < pairs.length; i++) {
                    String[] splitPairs = pairs[i].split(":");
                    builder.header(splitPairs[0], splitPairs[1]);
                }
            }
            request = builder.build();
        }
        //post put and patch method are made in a same form
        else if (method.equals("POST") || method.equals("PUT") || method.equals("PATCH")) {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .setHeader("User-Agent", "Insomnia")
                    .uri(URI.create(uri));
            if (json != null) {
                builder.setHeader("content-type", "JSON");
                if (method.equals("POST"))
                    builder.POST(HttpRequest.BodyPublishers.ofString(json));
                else if (method.equals("PUT"))
                    builder.PUT(HttpRequest.BodyPublishers.ofString(json));
                else
                    builder.method("PATCH", HttpRequest.BodyPublishers.ofString(json));


            } else if (data != null) {
                String[] forms = data.split("&");
                HashMap<Object, Object> data = new HashMap<>();
                for (int i = 0; i < forms.length; i++) {
                    String[] splitForms = forms[i].split("=");
                    data.put(splitForms[0], splitForms[1]);
                }
                try {
                    builder.setHeader("content-type", "FORM DATA");
                    if (method.equals("POST"))
                        builder.POST(ofMimeMultipartData(data, "" + (new Date()).getTime()));
                    else if (method.equals("PATCH"))
                        builder.method("PATCH", ofMimeMultipartData(data, "" + (new Date()).getTime()));
                    else
                        builder.PUT(ofMimeMultipartData(data, "" + (new Date()).getTime()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (upload != null) {
                try {
                    builder.setHeader("content-type", "BINARY");
                    if (method.equals("POST"))
                        builder.POST(HttpRequest.BodyPublishers.ofFile(Paths.get(upload)));
                    else if (method.equals("PATCH"))
                        builder.method("PATCH", HttpRequest.BodyPublishers.ofFile(Paths.get(upload)));
                    else
                        builder.PUT(HttpRequest.BodyPublishers.ofFile(Paths.get(upload)));
                } catch (FileNotFoundException e) {
                    System.out.println("File Not Found");
                }
            } else {
                builder.setHeader("content-type", "NO BODY");
                if (method.equals("POST"))
                    builder.POST(HttpRequest.BodyPublishers.noBody());
                else if (method.equals("PATCH"))
                    builder.method("PATCH", HttpRequest.BodyPublishers.noBody());
                else
                    builder.PUT(HttpRequest.BodyPublishers.noBody());
            }
            if (headers != null) {
                String[] pairs = headers.split(";");
                for (int i = 0; i < pairs.length; i++) {
                    String[] splitPairs = pairs[i].split(":");
                    builder.header(splitPairs[0], splitPairs[1]);
                }
            }
            request = builder.build();
        } else System.out.println("Undefined method type:" + method);

        return request;
    }

    /**
     * return if the request has enough info to be send in other word if it is completed
     *
     * @return true if the request is completed and vice versa
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * print the request as its info
     */
    public void printRequest() {
        System.out.println("URL: " + uri + " | Method: " + method + " | Headers: " + headers);
        if (json != null) {
            System.out.println("Json: " + json);
        } else if (data != null) {
            System.out.println("Multi FormData: " + data);
        } else if (upload != null) {
            System.out.println("Binary Path: " + upload);
        }
    }

    /**
     * makes a HttpRequest.BodyPublisher
     * <p>
     * THIS METHOD IS COPIED FROM https://golb.hplar.ch/2019/01/java-11-http-client.html LINK
     *
     * @param data     spilt data
     * @param boundary boundary
     * @return HttpRequest.BodyPublisher
     * @throws IOException
     */
    public HttpRequest.BodyPublisher ofMimeMultipartData(Map<Object, Object> data,
                                                         String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof Path) {
                var path = (Path) entry.getValue();
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName()
                        + "\"\r\nContent-Type: " + mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
            } else {
                byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n")
                        .getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }

    /**
     * set last response
     *
     * @param lastResponse last response
     */
    public void setLastResponse(HttpResponse<String> lastResponse) {
        this.lastResponse = lastResponse;
        if (output != null) {
            saveOutput(output);
        }
    }

    /**
     * save request
     */
    public void saveRequest() {
        ArrayList<Request> requests = new ArrayList<>();
        File file = new File("./../save/requests.insomnia");
        try (FileInputStream finRequests = new FileInputStream(file);
             ObjectInputStream requestsReader = new ObjectInputStream(finRequests);
        ) {
            while (true) {
                Request request = (Request) requestsReader.readObject();
                requests.add(request);
            }
        } catch (FileNotFoundException | EOFException | ClassNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();


        try (
                FileOutputStream fout = new FileOutputStream("./../save/requests.insomnia", true);
                ObjectOutputStream objWriter = new ObjectOutputStream(fout)) {
            for (int i = 0; i < requests.size(); i++) {
                objWriter.writeObject(requests.get(i));
            }
            objWriter.writeObject(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * save output
     *
     * @param outputName
     */
    public void saveOutput(String outputName) {
        try {
            File file = new File("./../save/outputs/" + outputName + ".txt");
            FileOutputStream fis = new FileOutputStream(file);
            PrintWriter printWriter = new PrintWriter(fis);
            printWriter.write(lastResponse.body());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * get https request
     */
    public HttpRequest getHttpRequest() {
        return request;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public HttpResponse<String> getLastResponse() {
        return lastResponse;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        //TODO: change the JLabel in gui mode
        this.method = method;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public boolean isShowResponseHeaders() {
        return showResponseHeaders;
    }

    public void setShowResponseHeaders(boolean showResponseHeaders) {
        this.showResponseHeaders = showResponseHeaders;
    }

    public boolean isFollowRedirect() {
        return followRedirect;
    }

    public void setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
    }

    public boolean isSaveRequest() {
        return saveRequest;
    }

    public void setSaveRequest(boolean saveRequest) {
        this.saveRequest = saveRequest;
    }

    public void setMethodLabel(JLabel methodLabel) {
        this.methodLabel = methodLabel;
    }

    public MiddlePanel getMiddlePanel() {
        return middlePanel;
    }

    public ResponsePanel getResponsePanel() {
        return responsePanel;
    }
}
