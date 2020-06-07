package HttpClient;

import GUI.InsomniaMenuBar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * save the configs of the menu bar to a file
 */
public class InsomniaConfiguration implements Serializable {
    boolean followRedirect,exitOnClose;
    String theme;
    public InsomniaConfiguration(InsomniaMenuBar insomniaMenuBar) {
        exitOnClose = insomniaMenuBar.getExitOnClose().isSelected();
        followRedirect = insomniaMenuBar.getFollowRedirectCheckBox().isSelected();
        theme = insomniaMenuBar.getThemeType();
    }

    /**
     * isFollowRedirect
     * @return FollowRedirect value
     */
    public boolean isFollowRedirect() {
        return followRedirect;
    }

    /**
     * isExitOnClose
     * @return exitOnClose value
     */
    public boolean isExitOnClose() {
        return exitOnClose;
    }

    /**
     * get theme
     * @return theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * save the configs
     * @param path path to save
     */
    public void save(String path){
        try (FileOutputStream fout=new FileOutputStream(path);
             ObjectOutputStream objWriter=new ObjectOutputStream(fout)){
            objWriter.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
