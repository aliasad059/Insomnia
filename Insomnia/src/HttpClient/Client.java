package HttpClient;

import java.util.ArrayList;

public class Client {
    private static ArrayList<Request> requests = new ArrayList<>();
    public static void main(String[] args) {
        if (args[0].equals("-url") || args[0].equals("-uri")){
            Request request = new Request(args);

        }
        else if (args[0].equals("list")) {
            if (args.length == 1) {
                printAllRequests();
            } else{
                printListRequests(args[1]);
            }
        } else if (args[0].equals("fire")) {
            for (int i = 1; i < args.length; i++) {
                int requestNumber = Integer.parseInt(""+args[i].charAt(0));
                
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
    private static void printAllRequests(){

    }
    private static void printListRequests(String listToPrint){

    }
    private static void help() {
        System.out.print("\n\n\n\nhelp:\n\n\n\n");
    }
}
