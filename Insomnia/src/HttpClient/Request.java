package HttpClient;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Request {
    private HttpRequest request;
    private LinkedList<String> args;
    private final boolean canBeSent ;
    // java Main -url uli -M (GET,Post,... ) -H "headers" -i -h -f -O(    empty->(make a auto name) OR
    // name of file to save the response body   ) -S -d "Message body in Form Data shape" -j " Message body in Json shape"
    // -u "" ?
    // java Main list  (empty (all saved request) OR listName(all listName's saved requests)) : just show the requests and will not wait for an input
    // java Main fire "requests (1 2 ...) OR (listName 1 2 )"
    private String uri, method, headers, output, data, json, upload, creatList, fire, saveToList;
    private boolean showResponseHeaders, followRedirect, saveRequest;

    public Request(String[] args) {
        this.args = new LinkedList<>(Arrays.asList(args));
        canBeSent = interpreter();
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
                //TODO: complete this one
                System.out.println("");
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
            if (args.contains("-s")) {
                int index = args.indexOf("-s");
                args.remove(index);
                saveRequest = true;
            }

        }

        if (args.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
    public void runRequest(){

    }

    public void printRequest(){
        System.out.println("request");
    }


    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
