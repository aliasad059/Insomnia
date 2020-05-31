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

/**
 * request
 */
public class Request implements Serializable {
    private HttpRequest request;
    private String[] firstArgs;
    private LinkedList<String> args;
    private final boolean completed;
    private HttpResponse<String> lastResponse;
    private String folderName = "Does not belong to any folder";
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
    }

    /**
     * get follow redirect
     * @return follow redirect value
     */
    public boolean getFollowRedirect() {
        return followRedirect;
    }

    /**
     * get show response headers
     * @return show response headers value
     */
    public boolean GetShowResponseHeaders() {
        return showResponseHeaders;
    }


    /**
     * interpret args array as follows
     * java Main url uli -M (GET,Post,... ) -H "headers" -i -h -f -O(    empty->(make a auto name) OR
     * name of file to save the response body   ) -S -d "Message body in multiForm Data shape" -j " Message body in Json shape"
     * -u "path"
     * java Main list  (empty (all saved request) OR listName(all listName's saved requests)) : just show the requests and will not wait for an input
     * java Main fire "requests (1 2 ...) OR (listName 1 2 )"
     *
     * @return return true if the entered pattern is ok and vice versa
     */
    private boolean interpreter() {

        if (args.get(0).equals("url") || args.get(0).equals("uri")) {
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

            //output
            if (args.contains("-O")) {
                int index = args.indexOf("-O");
                args.remove(index);
                if (args.get(index).charAt(0) == '-') {
                    output = "output_" + (new java.sql.Date(System.currentTimeMillis())).toString();
                } else {
                    output = args.get(index);
                }
                args.remove(index);
            }

            //form data
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

//            //save
            if (args.contains("-S")) {
                int index = args.indexOf("-S");
                args.remove(index);
                saveRequest = true;
                if (args.size()>index && args.get(index).charAt(0) != '-') {
                    ReqList reqList = Client.getList(args.get(index));
                    if (reqList == null) {
                        System.out.println(args.get(index) + " folder does not exist.");
                        return false;
                    } else {
                        reqList.addReq(this);
                        folderName = reqList.getListName();
                        args.remove(index);
                    }
                } else {
                    saveRequest();
                }
            }
        }
        if (args.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * makes an HTTP REQUEST as entered interpreted args
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
                builder.setHeader("content-type","JSON");
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
                    builder.setHeader("content-type","FORM DATA");
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
                    builder.setHeader("content-type","BINARY");
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
                builder.setHeader("content-type","NO BODY");
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
        System.out.println("Request folder name: "+folderName);
        if (json!=null){
            System.out.println("Json: "+json);;
        }else if (data != null){
            System.out.println("Multi FormData: "+data);;
        }else if (upload != null){
            System.out.println("Binary Path: "+upload);;
        }
    }

    /**
     * makes a HttpRequest.BodyPublisher
     *
     * THIS METHOD IS COPIED FROM https://golb.hplar.ch/2019/01/java-11-http-client.html LINK
     *
     * @param data spilt data
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
     * @param lastResponse last response
     */
    public void setLastResponse(HttpResponse<String> lastResponse) {
        this.lastResponse = lastResponse;
        if (output!=null){
            saveOutput(output);
        }
    }

    /**
     * save request
     */
    private void saveRequest() {
        ArrayList<Request>requests = new ArrayList<>();
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
     * @param outputName
     */
    private void saveOutput(String outputName){
        try {
            File file = new File("./../save/outputs/"+outputName+".txt");
            FileOutputStream fis = new FileOutputStream(file);
            PrintWriter printWriter = new PrintWriter(fis);
            printWriter.write(lastResponse.body());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
