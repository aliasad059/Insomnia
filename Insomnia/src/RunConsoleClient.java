
import HttpClient.ConsoleClient;

/**
 * runs Console client
 */
public class RunConsoleClient {
    public static void main(String[] args) {
        ConsoleClient client = new ConsoleClient(args);
        client.start();
    }
}
