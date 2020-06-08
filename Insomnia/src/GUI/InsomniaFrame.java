package GUI;

import HttpClient.GUIClient;
import HttpClient.InsomniaConfiguration;
import HttpClient.ReqList;
import HttpClient.Request;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.*;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.*;

/**
 * the main frame of the insomnia that components are laid out here
 * as we can see the selected theme is got from a github repo (darklaf)
 */
public class InsomniaFrame extends JFrame {
    TrayIcon trayIcon;
    SystemTray tray;
    MenuItem exitItem, openItem;
    JSplitPane currentRightPanels, leftAndRightPanels;
    static final int FRAME_WIDTH = 1250, FRAME_HEIGHT = 600;
    RequestsPanel requestsPanel;
    JPanel currentMiddlePanel, currentResponsePanel;
    InsomniaMenuBar menuBar;
    ArrayList<Request> requests;
    ArrayList<ReqList> reqLists;

    /**
     * creat a new insomnia frame with empty data
     * @param title frame title
     */
    public InsomniaFrame(String title) {
        super(title);
        requests = new ArrayList<>();
        reqLists = new ArrayList<>();
        if (SystemTray.isSupported()) {

            PopupMenu popup = new PopupMenu();
            exitItem = new MenuItem("Exit");
            openItem = new MenuItem("Open");
            exitItem.addActionListener(new handler());
            openItem.addActionListener(new handler());

            popup.add(openItem);
            popup.add(exitItem);

            Image image = Toolkit.getDefaultToolkit().getImage("./src/Icons/Insomnia.png");

            tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(image, "Insomnia", popup);
            trayIcon.setImageAutoSize(true);
        } else {
            System.out.println("system tray not supported");
        }

        addWindowListener(new handler());
        makePanels();
    }

    /**
     * creat a new insomnia frame with input data (data that are loaded from file)
     * @param title title
     * @param reqLists reqlists
     * @param requests requests
     * @param configuration configuration relate to the menu bar settings
     */
    public InsomniaFrame(String title , ArrayList<ReqList> reqLists, ArrayList<Request> requests, InsomniaConfiguration configuration) {
        super(title);
        this.reqLists = reqLists;
        this.requests = requests;
        if (SystemTray.isSupported()) {

            PopupMenu popup = new PopupMenu();
            exitItem = new MenuItem("Exit");
            openItem = new MenuItem("Open");
            exitItem.addActionListener(new handler());
            openItem.addActionListener(new handler());

            popup.add(openItem);
            popup.add(exitItem);

            Image image = Toolkit.getDefaultToolkit().getImage("./src/Icons/Insomnia.png");

            tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(image, "Insomnia", popup);
            trayIcon.setImageAutoSize(true);
        } else {
            System.out.println("system tray not supported");
        }

        addWindowListener(new handler());
        makePanels();
        if (configuration!=null) {
            menuBar.setFollowRedirect(configuration.isFollowRedirect());
            menuBar.setExitOnClose(configuration.isExitOnClose());
            menuBar.setTheme(configuration.getTheme());
        }

        requestsPanel.setRequests(requests);
        requestsPanel.setFolders(reqLists);
        for (int i = 0; i < requests.size(); i++) {
            requests.get(i).initMiddlePanel();
            requests.get(i).initResponsePanel();
        }
        for (int i = 0; i < reqLists.size(); i++) {
            for (int j = 0; j < reqLists.get(i).getRequests().size(); j++) {
                reqLists.get(i).getRequests().get(j).initMiddlePanel();
                reqLists.get(i).getRequests().get(j).initResponsePanel();
            }
        }
    }

    /**
     * makes the 3 panels of the insomnia app and relate them with a split pane
     */
    private void makePanels() {
        LafManager.install(new DarculaTheme());
        this.validate();
        requestsPanel = new RequestsPanel(this);
        currentMiddlePanel = new MiddlePanel(null,false);
        currentResponsePanel = new ResponsePanel();
        menuBar = new InsomniaMenuBar(this);

        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setJMenuBar(menuBar);
        currentRightPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, currentMiddlePanel, currentResponsePanel);
        currentRightPanels.setDividerLocation(FRAME_WIDTH / 3 + FRAME_WIDTH / 20);
        leftAndRightPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, requestsPanel, currentRightPanels);
        leftAndRightPanels.setDividerLocation(FRAME_WIDTH / 6);

        this.getContentPane().add(leftAndRightPanels);
        this.setVisible(false);
    }

    /**
     * handling the main events of frame
     */
    class handler extends WindowAdapter implements ActionListener {

        /**
         * Invoked when a window is in the process of being closed.
         * The close operation can be overridden at this point.
         * <p>
         * if the window is closing and the user chose exit on close, the program will be terminated
         * else it will be in system tray
         *
         * @param e
         */
        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            if (InsomniaMenuBar.isIsSystemTrayEnabled()) {
                try {
                    tray.add(trayIcon);
                    setVisible(false);
                } catch (AWTException ex) {
                }
            } else {
                GUIClient.save();
                System.exit(0);
            }
        }

        /**
         * Invoked when a window has been opened.
         *
         * @param e
         */
        @Override
        public void windowOpened(WindowEvent e) {
            super.windowOpened(e);
            tray.remove(trayIcon);
            setVisible(true);
        }

        /**
         * Invoked when an action occurs.
         *
         * @param e action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == exitItem) {
                GUIClient.save();
                System.exit(0);
            } else if (e.getSource() == openItem) {
                tray.remove(trayIcon);
                setVisible(true);
                setExtendedState(JFrame.NORMAL);
            }
        }
    }

    /**
     * get right panels
     *
     * @return right panels
     */
    public JSplitPane getRightPanels() {
        return currentRightPanels;
    }

    /**
     * get left and right panels means whole of the frame
     *
     * @return left and right panels
     */
    public JSplitPane getLeftAndRightPanels() {
        return leftAndRightPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, requestsPanel, currentRightPanels);
    }

    /**
     * set right panels
     */
    public void setRightPanels() {
        currentRightPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, currentMiddlePanel, currentResponsePanel);
    }

    /**
     * set left and right panels in other word the whole panels
     */
    public void setLeftAndRightPanels() {
        setRightPanels();
        this.getContentPane().remove(0);
        this.getContentPane().add(leftAndRightPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, requestsPanel, currentRightPanels));
        currentRightPanels.setDividerLocation(FRAME_WIDTH / 3 + FRAME_WIDTH / 20);
        leftAndRightPanels.setDividerLocation(FRAME_WIDTH / 6);
        this.revalidate();
        this.repaint();
    }

    /**
     * as different request has different panels we have a current response panel
     * @param currentResponsePanel
     */
    public void setCurrentResponsePanel(JPanel currentResponsePanel) {
        this.currentResponsePanel = currentResponsePanel;
    }

    /**
     * add a new request to the frame list
     * this list can be used in save and load
     * @param request request to save
     */
    public void addRequestToFrameList(Request request) {
        requests.add(request);
    }

    /**
     * add a new reqlist to the frame reqlists
     * @param reqList
     */
    public void addReqlistToFrameList(ReqList reqList) {
        reqLists.add(reqList);
    }

    /**
     * get stored requests
     * @return requests
     */
    public ArrayList<Request> getRequests() {
        return requests;
    }

    /**
     * get stored reqlists
     * @return reqlists
     */
    public ArrayList<ReqList> getReqLists() {
        return reqLists;
    }

    /**
     *      * as different request has different panels we have a current middle panel
     * @param currentMiddlePanel
     */
    public void setCurrentMiddlePanel(JPanel currentMiddlePanel) {
        this.currentMiddlePanel = currentMiddlePanel;
    }

    /**
     * get menu bar of the frame
     * @return menu bar
     */
    public InsomniaMenuBar getInsomniaMenuBar() {
        return menuBar;
    }

    /**
     * get requestpanel of the frame
     * @return
     */
    public RequestsPanel getRequestsPanel() {
        return requestsPanel;
    }

    /**
     * reinstall the theme as choosed in insomnia menu bar
     */
    public void updateTheme(){
        try {
            if (menuBar.highContrastLightTheme.isSelected()) {
                LafManager.install(new HighContrastLightTheme());
            } else if (menuBar.solarizedLightTheme.isSelected()) {
                LafManager.install(new SolarizedLightTheme());
            } else if (menuBar.solarizedDarkTheme.isSelected()) {
                LafManager.install(new SolarizedDarkTheme());
            } else if (menuBar.highContrastDarkTheme.isSelected()) {
                LafManager.install(new HighContrastDarkTheme());
            } else if (menuBar.intelliJTheme.isSelected()) {
                LafManager.install(new IntelliJTheme());
            } else
                LafManager.install(new DarculaTheme());
            this.revalidate();
            this.repaint();
        }catch (Exception ignored){

        }
    }
}