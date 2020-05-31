package HttpClient;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Client {
    private String[] args;
    private ArrayList<Request> requests = new ArrayList<>();
    private static ArrayList<ReqList> reqLists = new ArrayList<>();

    public Client(String[] args) {
        this.args = args;
    }

    /**
     * start the client
     */
    public void start() {
        load();
        if (args[0].equals("url") || args[0].equals("uri")) {
            Request request = new Request(args);
            if (request.isCompleted()) {
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
        HttpRequest httpRequest = request.makeRequest();
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
        for (int i = 0; i < requests.size(); i++) {
            System.out.print((i + 1) + ". ");
            requests.get(0).printRequest();
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
}
