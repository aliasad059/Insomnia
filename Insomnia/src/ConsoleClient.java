import HttpClient.Client;

public class ConsoleClient {
    public static void main(String[] args) {
        Client client = new Client(args);
        client.start();
    }
}
