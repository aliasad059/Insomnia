package HttpClient;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;

public class Client {
    private String [] args;
    private  ArrayList<Request> requests = new ArrayList<>();

    public Client(String[] args) {
        this.args = args;
    }

    public void start() {
        loadRequests();
        if (args[0].equals("-url") || args[0].equals("-uri")){
            Request request = new Request(args);
            if (request.isCompleted()){
                requests.add(request);
                runRequest(request.getHttpsRequest());
            }
        }
        else if (args[0].equals("list")) {
            if (args.length == 1) {
                printAllRequests();
            } else{
                printListRequests(args[1]);
            }
        } else if (args[0].equals("fire")) {
            for (int i = 1; i < args.length; i++) {
                int requestNumber = Integer.parseInt(args[i]);
                runRequest(requests.get(requestNumber).getHttpsRequest());
            }
        } else if (args[0].equals("-h") ||args[0].equals("--help")) {
            help();
        }
        else if (args[0].equals("creat")){
            List list = new List();
        }
        else {
            System.out.println("Incorrect pattern.");
            System.out.println("Use -h or --help to get help");
        }
    }
    private void runRequest(HttpRequest request){
        if (request == null){
            System.out.println("Incorrect pattern.");
            System.out.println("Use -h or --help to get help");
        }
        else {
            HttpClient.newHttpClient()
            HttpClient client ;

        }
    }
    private  void printAllRequests(){

    }
    private  void printListRequests(String listToPrint){

    }
    private  void loadRequests(){

    }
    private  void help() {
        System.out.print("\n\n\n\nhelp:\n\n\n\n");
    }
}
