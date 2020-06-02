package HttpClient;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class ConsoleClient {
    private String[] args;
    private ArrayList<Request> requests = new ArrayList<>();
    private static ArrayList<ReqList> reqLists = new ArrayList<>();

    public ConsoleClient(String[] args) {
        this.args = args;
    }

    /**
     * start the client
     */
    public void start() {
        load();
        if (args[0].equals("url") || args[0].equals("uri")) {
            Request request = new Request();
            request.setCompleted(interpreter(request));
            if (request.isCompleted()) {
                request.makeRequest();
                requests.add(request);
                runRequest(request);
            } else {
                System.out.println("Incorrect pattern.");
                System.out.println("Use -h or --help to get help");
            }
        } else if (args[0].equals("list")) {

            if (args.length == 1) {
                printRequests();
            } else {
                printRequestsIn(args[1]);
            }
        } else if (args[0].equals("fire")) {
            try {
                int requestNumber = Integer.parseInt(args[1]);
                
                try {
                    for (int i = 1; i < args.length; i++) {
                        requestNumber = Integer.parseInt(args[i]);
                        runRequest(requests.get(requestNumber - 1));
                    }
                }
                catch (NumberFormatException e){
                    System.out.println("Incorrect pattern.");
                    System.out.println("Use -h or --help to get help");
                }

            }
            catch (NumberFormatException e){
                try {
                    ReqList reqList = getList(args[1]);
                    if (reqList != null) {
                        for (int i = 2; i < args.length; i++) {
                            int requestNumber = Integer.parseInt(args[i]);
                            runRequest(reqList.getRequest(requestNumber-1));
                        }
                    }
                    else {
                        System.out.println(args[1]+" folder not found.");
                    }
                }
                catch (NumberFormatException e1){
                    System.out.println("Incorrect pattern.");
                    System.out.println("Use -h or --help to get help");
                    return;
                }
            }
        } else if (args[0].equals("-h") || args[0].equals("--help")) {
            help();
        } else if (args[0].equals("creat")) {
            ReqList reqList = new ReqList(args[1]);
            reqLists.add(reqList);
        } else {
            System.out.println("Incorrect pattern.");
            System.out.println("Use -h or --help to get help");
        }
    }

    /**
     * run request
     * @param request request to run
     */
    private void runRequest(Request request) {
        HttpRequest httpRequest = request.getHttpRequest();
        if (httpRequest == null) {
            System.out.println("Incorrect pattern.");
            System.out.println("Use -h or --help to get help");
        } else {
            HttpClient client;
            HttpClient.Builder builder = HttpClient.newBuilder();
            if (request.getFollowRedirect())
                builder.followRedirects(HttpClient.Redirect.ALWAYS);
            builder.version(HttpClient.Version.HTTP_1_1);
            client = builder.build();
            try {


                double startTime = System.nanoTime();
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                double elapsedTime = System.nanoTime() - startTime;
                request.setLastResponse(response);

                String timeSize = "nS";
                if (elapsedTime > 1000000000){
                    timeSize= " S";
                    elapsedTime/=1000000000;
                }else if(elapsedTime>1000000){
                    timeSize= " mS";
                    elapsedTime/=1000000;
                }else if(elapsedTime>1000){
                    timeSize= " μS";
                    elapsedTime/=1000;
                }
                System.out.println("URI: " + response.uri());
                System.out.println("Response Time: " + elapsedTime + timeSize);
                System.out.println("Content Length: " + response.headers().allValues("Content-Length"));
                System.out.println("Version: " + response.version().toString() + "  |  Status code: " + response.statusCode());
                System.out.println(response.body());
                if (request.GetShowResponseHeaders()) {
                    HttpHeaders headers = response.headers();
                    headers.map().forEach((k, v) -> System.out.println(k + ":" + v));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * print all requests
     */
    private void printRequests() {
        System.out.println("Requests:");
        System.out.println("--------------------------------------------");
        for (int i = 0; i < requests.size(); i++) {
            System.out.print((i + 1) + ". ");
            requests.get(0).printRequest();
        }
        System.out.println("Lists:");
        System.out.println("--------------------------------------------");
        for (int i = requests.size(); i <reqLists.size() ; i++) {
            System.out.println((i + 1)+". "+reqLists.get(i).getListName());
        }
    }

    /**
     * print request in the list
     * @param listToPrint list to print its requests
     */
    private void printRequestsIn(String listToPrint) {
        ReqList list = getList(listToPrint);
        if (list != null) {
            list.printList();
        } else System.out.println(listToPrint + " does not exist.");
    }

    /**
     * load saved info
     */
    private void load() {

        try (FileInputStream finRequests = new FileInputStream("./../save/requests.insomnia");
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


        File [] lists = new File("./../save/lists").listFiles();
        for (int i = 0; i < lists.length; i++) {

            try (FileInputStream finLists = new FileInputStream(lists[i]);
                 ObjectInputStream listsReader = new ObjectInputStream(finLists)
            ) {
                while (true) {
                    ReqList reqList = (ReqList) listsReader.readObject();
                    reqLists.add(reqList);
                }
            } catch (FileNotFoundException | EOFException | ClassNotFoundException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * print help when using -h or --help commands
     */
    private void help() {
        System.out.println("url/uri\t[Input url]\tSetting url to send request");
        System.out.println("url/uri options:");
        System.out.println("-M\t[METHOD]\tMethod to send request");
        System.out.println("-H\t[\"headers\"]\t Sending headers");
        System.out.println("-i\tShow response headers");
        System.out.println("-f\tFollow redirect");
        System.out.println("-O\t[Output name *optional]\tSave response body to a text file");
        System.out.println("-S\t[list name OR empty(just save it)]\tSave the request in a file");
        System.out.println("-d\t[Message body]\tSend body in multiForm Data shape");
        System.out.println("-j\t[Message body]\tSend body in json shape");
        System.out.println("-u\t[File path]\tSend body in binary shape");
        System.out.println("list\t[empty (all saved request) OR listName(all listName's saved requests)]\tprint requests info");
        System.out.println("creat\t[New list name]\tCreat new list");
        System.out.println("fire\t[requests (1 2 ...) OR (listName 1 2 )]\tRun saved requests");
    }

    /**
     * get list as its name
     * @param listName list name
     * @return list if found and null if not
     */
    public static ReqList getList(String listName) {

        for (int i = 0; i < reqLists.size(); i++) {
            if (reqLists.get(i).getListName().toLowerCase().equals(listName.toLowerCase())) {
                return reqLists.get(i);
            }
        }
        return null;
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
    private boolean interpreter(Request requestToInterpret) {
        LinkedList<String> args = new LinkedList<>(Arrays.asList(this.args));
        if (args.get(0).equals("url") || args.get(0).equals("uri")) {
            args.remove(0);

            //uri
            if (args.get(0).charAt(0) == '-') {
                //when the user forget to enter the uri
                return false;
            } else {
                requestToInterpret.setUri(args.get(0));
                args.remove(0);
            }

            //method
            if (args.contains("-M")) {
                int index = args.indexOf("-M");
                args.remove(index);
                if (args.get(index).charAt(0) == '-') {
                    return false;
                } else {
                    requestToInterpret.setMethod(args.get(index));
                    args.remove(index);
                }
            } else {
                requestToInterpret.setMethod("GET");
            }

            //output
            if (args.contains("-O")) {
                int index = args.indexOf("-O");
                args.remove(index);
                if (args.get(index).charAt(0) == '-') {
                    requestToInterpret.setOutput("output_" + (new java.sql.Date(System.currentTimeMillis())).toString());
                } else {
                    requestToInterpret.setOutput(args.get(index));
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
                    requestToInterpret.setData( args.get(index));
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
                    requestToInterpret.setHeaders(args.get(index));
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
                    requestToInterpret.setJson(  args.get(index));
                    args.remove(index);
                }
            }

            //upload
            if (args.contains("-u")) {
                int index = args.indexOf("-u");
                args.remove(index);
                requestToInterpret.setUpload(args.get(index));
                args.remove(index);
            }

            //show response headers
            if (args.contains("-i")) {
                int index = args.indexOf("-i");
                args.remove(index);
                requestToInterpret.setShowResponseHeaders(true);
            }

            //follow redirect
            if (args.contains("-f")) {
                int index = args.indexOf("-f");
                args.remove(index);
                requestToInterpret.setFollowRedirect(true);
            }

//            //save
            if (args.contains("-S")) {
                int index = args.indexOf("-S");
                args.remove(index);
                requestToInterpret.setSaveRequest(true);
                if (args.size()>index && args.get(index).charAt(0) != '-') {
                    ReqList reqList = getList(args.get(index));
                    if (reqList == null) {
                        System.out.println(args.get(index) + " folder does not exist.");
                        return false;
                    } else {
                        reqList.addReq(requestToInterpret);
                        args.remove(index);
                    }
                } else {
                    requestToInterpret.saveRequest();
                }
            }
        }
        if (args.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
