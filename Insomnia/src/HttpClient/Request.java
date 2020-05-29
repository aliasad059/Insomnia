package HttpClient;

import java.io.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Request implements Serializable {
    private HttpRequest request;
    private String[] firstArgs;
    private LinkedList<String> args;
    private final boolean completed;
    private HttpResponse<String> lastResponse;
    private final String reqName;
    private String folderName = "Does not belong to any folder";
    // java Main -url uli -M (GET,Post,... ) -H "headers" -i -h -f -O(    empty->(make a auto name) OR
    // name of file to save the response body   ) -S -d "Message body in Form Data shape" -j " Message body in Json shape"
    // -u "path"
    // java Main list  (empty (all saved request) OR listName(all listName's saved requests)) : just show the requests and will not wait for an input
    // java Main fire "requests (1 2 ...) OR (listName 1 2 )"
    private String uri, method, headers, output, data, json, upload, creatList, fire, saveToList;
    private boolean showResponseHeaders, followRedirect, saveRequest;

    public Request(String[] args) {
        this.firstArgs = args;
        this.args = new LinkedList<>(Arrays.asList(args));
        completed = interpreter();
        if (completed)
            makeRequest();
        else {
            System.out.println("Incorrect pattern.");
            System.out.println("Use -h or --help to get help");
        }
        reqName = "" + Client.requestsNumber();
    }

    public boolean getFollowRedirect() {
        return followRedirect;
    }

    public boolean GetShowResponseHeaders() {
        return showResponseHeaders;
    }


    /**
     * interpret args array
     *
     * @return return true if the entered pattern is ok and vice versa
     */
    private boolean interpreter() {

        if (args.get(0).equals("-url") || args.get(0).equals("-uri")) {
            args.remove(0);

            //uri
            if (args.get(0).charAt(0) == '-') {
                //when the user forget to enter the uri
                return false;
            } else {
                uri = args.get(0);
                args.remove(0);
            }

            //method
            if (args.contains("-M")) {
                int index = args.indexOf("-M");
                args.remove(index);
                if (args.get(index).charAt(0) == '-') {
                    return false;
                } else {
                    method = args.get(index);
                    args.remove(index);
                }
            } else {
                method = "GET";
            }

            //output name
            if (args.contains("-O")) {
                int index = args.indexOf("-O");
                args.remove(index);
                if (args.get(index).charAt(0) == '-') {
                    output = "output_" + (new java.sql.Date(System.currentTimeMillis())).toString();
                } else {
                    output = args.get(index);
                    args.remove(index);
                }

            }

            //form date
            if (args.contains("-d")) {
                int index = args.indexOf("-d");
                args.remove(index);
                if (args.get(index).charAt(0) == '-') {
                    return false;
                } else {
                    data = args.get(index);
                    args.remove(index);
                }
            }

            //header
            if (args.contains("-H")) {
                int index = args.indexOf("-H");
                args.remove(index);
                if (args.get(index).charAt(0) == '-') {
                    return false;
                } else {
                    headers = args.get(index);
                    args.remove(index);
                }
            }

            //json
            if (args.contains("-j")) {
                int index = args.indexOf("-j");
                args.remove(index);
                if (args.get(index).charAt(0) == '-') {
                    return false;
                } else {
                    json = args.get(index);
                    args.remove(index);
                }
            }

            //upload
            if (args.contains("-u")) {
                int index = args.indexOf("-u");
                args.remove(index);
                upload = args.get(index);
                args.remove(index);
            }

            //show response headers
            if (args.contains("-i")) {
                int index = args.indexOf("-i");
                args.remove(index);
                showResponseHeaders = true;
            }

            //follow redirect
            if (args.contains("-f")) {
                int index = args.indexOf("-f");
                args.remove(index);
                followRedirect = true;
            }

            //save
            if (args.contains("-S")) {
                int index = args.indexOf("-S");
                args.remove(index);
                saveRequest = true;
                if (!args.get(index).contains("-")) {
                    ReqList reqList = Client.getList(args.get(index));
                    if (reqList == null){
                        System.out.println(args.get(index)+" folder does not exist.");
                        return false;
                    }
                    else {
                        reqList.addReq(this);
                        reqList.saveList();
                        folderName= reqList.getListName();
                    }
                }
                else saveRequest();
            }

        }

        if (args.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private HttpRequest makeRequest() {
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
        } else if (method.equals("POST") || method.equals("PUT") || method.equals("PATCH")) {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .setHeader("User-Agent", "Insomnia")
                    .uri(URI.create(uri));
            if (json != null) {
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

    public boolean isCompleted() {
        return completed;
    }

    public void printRequest() {
        System.out.println("URL: " + uri + " | Method: " + method + " | Headers: " + headers);
    }

    public HttpRequest.BodyPublisher ofMimeMultipartData(Map<Object, Object> data,
                                                         String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof String) {
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

    public HttpRequest getHttpsRequest() {
        return request;
    }

    public void setLastResponse(HttpResponse<String> lastResponse) {
        this.lastResponse = lastResponse;
    }

    private void saveRequest() {
        try (FileOutputStream fout = new FileOutputStream("./save/requests.txt",true);
             ObjectOutputStream objWriter = new ObjectOutputStream(fout)) {
            objWriter.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
