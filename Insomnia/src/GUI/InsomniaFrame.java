package GUI;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * the main frame of the insomnia that components are laid out here
 * as we can see the selected theme is got from a github repo (darklaf)
 */
public class InsomniaFrame extends JFrame {
    TrayIcon trayIcon;
    SystemTray tray;
    MenuItem exitItem, openItem;
    JSplitPane rightPanels, leftAndRightPanels;
    static final int FRAME_WIDTH = 1250, FRAME_HEIGHT = 600;
    JPanel requestsPanel, middlePanel, responsePanel;
    JMenuBar menuBar;

    InsomniaFrame(String title) {
        super(title);
        if (SystemTray.isSupported()) {

            PopupMenu popup = new PopupMenu();
            exitItem = new MenuItem("Exit");
            openItem = new MenuItem("Open");
            exitItem.addActionListener(new handler());
            openItem.addActionListener(new handler());

            popup.add(openItem);
            popup.add(exitItem);

            Image image = Toolkit.getDefaultToolkit().getImage(".\\Icons\\Insomnia.png");

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
     * makes the 3 panels of the insomnia app and relate them with a split pane
     */
    private void makePanels(){
        LafManager.install(new DarculaTheme());
        this.validate();
        requestsPanel = new RequestsPanel();
        middlePanel =new MiddlePanel();
        responsePanel = new ResponsePanel();
        menuBar = new InsomniaMenuBar(this);

        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        //frame.setLayout(new GridLayout(1, 3));
        this.setJMenuBar(menuBar);
        rightPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, middlePanel, responsePanel);
        rightPanels.setDividerLocation(FRAME_WIDTH / 3 + FRAME_WIDTH / 20);
        leftAndRightPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, requestsPanel, rightPanels);
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
         *
         * if the window is closing and the user chose exit on close, the program will be terminated
         * else it will be in system tray
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
                //TODO: save before exit
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
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == exitItem) {
                //TODO: save before exit
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
     * @return right panels
     */
    public JSplitPane getRightPanels() {
        return rightPanels;
    }

    /**
     * get left and right panels means whole of the frame
     * @return left and right panels
     */
    public JSplitPane getLeftAndRightPanels() {
        return leftAndRightPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, requestsPanel, rightPanels);
    }

}