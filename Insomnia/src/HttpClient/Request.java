package HttpClient;

import GUI.MiddlePanel;
import GUI.ResponsePanel;
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
    private static final long serialVersionUID = 6529685098267757690L;

    private transient MiddlePanel middlePanel;
    private transient ResponsePanel responsePanel;
    private transient HttpRequest request;
    private transient HttpResponse<byte[]> lastResponse;

    private Response response;
//    private JLabel methodLabel;
    private boolean completed;
    private String requestName, uri, method, headers, output, data, json, upload;
    private HashMap<String, String> headersMap,formsMap,queries;
    private boolean showResponseHeaders, followRedirect, saveRequest;

    public Request() {
        headersMap= new HashMap<>();
        formsMap = new HashMap<>();
        queries = new HashMap<>();
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
            if (!headers.equals("")) {
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
                    .setHeader("user-agent", "Insomnia")
                    .uri(URI.create(uri));
            if (json != null && !json.equals("")) {
                builder.setHeader("content-type", "application/json");
                if (method.equals("POST"))
                    builder.POST(HttpRequest.BodyPublishers.ofString(json));
                else if (method.equals("PUT"))
                    builder.PUT(HttpRequest.BodyPublishers.ofString(json));
                else
                    builder.method("PATCH", HttpRequest.BodyPublishers.ofString(json));


            } else if (data != null && !data.equals("")) {
                String[] forms = data.split("&");
                HashMap<Object, Object> formData = new HashMap<>();
                for (int i = 0; i < forms.length; i++) {
                    String[] splitForms = forms[i].split("=");
                    formData.put(splitForms[0], splitForms[1]);
                }
                try {
                    String boundary = "" + new Date().getTime();
                    builder.setHeader("content-type", "multipart/form-data; boundary=" + boundary);
                    if (method.equals("POST")) {
                        builder.POST(ofMimeMultipartData(formData, boundary));
                    } else if (method.equals("PATCH")) {
                        builder.method("PATCH", ofMimeMultipartData(formData, boundary));
                    } else {
                        builder.PUT(ofMimeMultipartData(formData, boundary));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (upload != null && !upload.equals("")) {
                try {
                    builder.setHeader("content-type", "application/octet-stream");
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
                builder.setHeader("Content-tType", "No-Body");
                if (method.equals("POST"))
                    builder.POST(HttpRequest.BodyPublishers.noBody());
                else if (method.equals("PATCH"))
                    builder.method("PATCH", HttpRequest.BodyPublishers.noBody());
                else
                    builder.PUT(HttpRequest.BodyPublishers.noBody());
            }
            if (headers != null && !headers.equals("")) {
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
    public void setLastResponse(HttpResponse<byte[]> lastResponse) {
        this.lastResponse = lastResponse;
        response = new Response(lastResponse);
        if (output != null) {
            saveOutput(output);
        }
    }

    /**
     * save request
     */
    public void saveRequest(String path) {
        ArrayList<Request> requests = new ArrayList<>();
        File file = new File(path);
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
                FileOutputStream fout = new FileOutputStream(path, true);
                ObjectOutputStream objWriter = new ObjectOutputStream(fout)) {
            for (int i = 0; i < requests.size(); i++) {
                objWriter.writeObject(requests.get(i));
            }
            if (!requests.contains(this))
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
            printWriter.write(new String(lastResponse.body()));
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

    public MiddlePanel getMiddlePanel() {
        return middlePanel;
    }

    public ResponsePanel getResponsePanel() {
        return responsePanel;
    }

    public void setMiddlePanel(MiddlePanel middlePanel) {
        this.middlePanel = middlePanel;
    }

    public void setResponsePanel(ResponsePanel responsePanel) {
        this.responsePanel = responsePanel;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }
    public Map<String, String> getFormsMap() {
        return formsMap;
    }

    public Map<String, String> getHeadersMap() {
        return headersMap;
    }
    public void addQuery(String key,String value){
        queries.put(key, value);
    }
    public Map<String, String> getQueries() {
        return queries;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * initialize middle panel withe its fields
     */
    public void initMiddlePanel() {
        setMiddlePanel(new MiddlePanel(this));
    }
    /**
     * initialize response panel withe its fields
     */
    public void initResponsePanel() {
        setResponsePanel(new ResponsePanel(this.response));
    }
    /**
     * adds a new header to the headersMap
     */
    public void addHeader(String key , String value){
        headersMap.put(key, value);
    }
    /**
     * adds a new form to the formsMap
     */
    public void addForm(String key , String value){
        formsMap.put(key, value);
    }

}
