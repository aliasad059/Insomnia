import GUI.Display;
import HttpClient.GUIClient;

/**
 * runs GUI client
 */
public class RunGUIClient{
    public static void main(String[] args) {
        Display display = GUIClient.load();
        if (display == null){
             display = new Display();
        }
    }
}