package HttpClient;

import GUI.InsomniaMenuBar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class InsomniaConfiguration implements Serializable {
    InsomniaMenuBar insomniaMenuBar;
    boolean followRedirect,exitOnClose;
    String theme;
    public InsomniaConfiguration(InsomniaMenuBar insomniaMenuBar) {
        exitOnClose = insomniaMenuBar.getExitOnClose().isSelected();
        followRedirect = insomniaMenuBar.getFollowRedirectCheckBox().isSelected();
        theme = insomniaMenuBar.getThemeType();
    }

    public InsomniaMenuBar getInsomniaMenuBar() {
        return insomniaMenuBar;
    }

    public boolean isFollowRedirect() {
        return followRedirect;
    }

    public boolean isExitOnClose() {
        return exitOnClose;
    }

    public String getTheme() {
        return theme;
    }
    public void save(String path){
        try (FileOutputStream fout=new FileOutputStream(path);
             ObjectOutputStream objWriter=new ObjectOutputStream(fout)){
            objWriter.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
