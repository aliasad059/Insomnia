import GUI.Display;
import HttpClient.GUIClient;

/**
 * just to test the GUI
 */
public class RunGUIClient{
    public static void main(String[] args) {
//        Display display = GUIClient.load();
//        if (display == null){
//            display = new Display();
//        }
        Display display = new Display();
        GUIClient client = new GUIClient();
    }
}