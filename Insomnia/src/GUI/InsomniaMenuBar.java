package GUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import HttpClient.GUIClient;
import com.github.weisj.darklaf.*;
import com.github.weisj.darklaf.theme.*;

/**
 * menu bar of the insomnia frame
 */
public class InsomniaMenuBar extends JMenuBar {
    private JMenu applicationMenu, viewMenu, helpMenu;
    private JMenuItem optionsItem, exitItem, toggleFullScreenItem, toggleSidebarItem, aboutItem, helpItem;
    private InsomniaFrame frame;
    private Display display;
    private static boolean isSystemTrayEnabled = true;
    private static boolean followRedirect = false;
    private String themeTypeString;
    InsomniaSystemTray systemTray;
    JDialog optionDialog;
    JCheckBox followRedirectCheckBox, ExitOnClose;
    ButtonGroup themeType;
    JRadioButton darculaTheme, solarizedLightTheme, intelliJTheme, solarizedDarkTheme, highContrastDarkTheme, highContrastLightTheme;
    JButton saveSetting;
    private boolean isSlideToggledActive = false;
    JPanel requestsPanel;

    /**
     * creat an insomnia menu bar and set its owner frame
     * @param frame owner frame
     */
    public InsomniaMenuBar(InsomniaFrame frame) {
        this.frame = frame;
        applicationMenu = new JMenu("Application");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");
        optionsItem = new JMenuItem("Options");
        exitItem = new JMenuItem("Exit");
        toggleFullScreenItem = new JMenuItem("Toggle Full Screen");
        toggleSidebarItem = new JMenuItem("Toggle Sidebar");
        aboutItem = new JMenuItem("About");
        helpItem = new JMenuItem("Help");

        this.add(applicationMenu);
        this.add(viewMenu);
        this.add(helpMenu);

        applicationMenu.add(optionsItem);
        applicationMenu.add(exitItem);

        viewMenu.add(toggleFullScreenItem);
        viewMenu.add(toggleSidebarItem);

        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);

        exitItem.addActionListener(new handler());
        optionsItem.addActionListener(new handler());
        toggleFullScreenItem.addActionListener(new handler());
        toggleSidebarItem.addActionListener(new handler());
        helpItem.addActionListener(new handler());
        aboutItem.addActionListener(new handler());

        followRedirectCheckBox = new JCheckBox("Follow Redirect");
        followRedirectCheckBox.addActionListener(new handler());
        ExitOnClose = new JCheckBox("Exit On Close");
        ExitOnClose.addChangeListener(new handler());
        themeType = new ButtonGroup();
        darculaTheme = new JRadioButton("Darcula");
        solarizedLightTheme = new JRadioButton("Solarized Light");
        intelliJTheme = new JRadioButton("IntelliJ");
        solarizedDarkTheme = new JRadioButton("Solarized Dark");
        highContrastDarkTheme = new JRadioButton("High Contrast Dark");
        highContrastLightTheme = new JRadioButton("High Contrast Light");

        themeType.add(darculaTheme);
        themeType.add(intelliJTheme);
        themeType.add(highContrastLightTheme);
        themeType.add(solarizedLightTheme);
        themeType.add(highContrastDarkTheme);
        themeType.add(solarizedDarkTheme);
        darculaTheme.setSelected(true);
        saveSetting = new JButton("OK");

        applicationMenu.setMnemonic('A');
        viewMenu.setMnemonic('V');
        helpMenu.setMnemonic('H');
        helpItem.setMnemonic('H');
        aboutItem.setMnemonic('A');
        toggleSidebarItem.setMnemonic('S');
        toggleFullScreenItem.setMnemonic('F');
        optionsItem.setMnemonic('O');
        exitItem.setMnemonic('E');


    }

    /**
     * makes the dialog of option item
     */
    private void makeOptionDialog() {
        optionDialog = new JDialog();
        //optionDialog.validate();
        optionDialog.setLayout(new BorderLayout());
        JPanel themePanel = new JPanel();
        themePanel.setLayout(new FlowLayout());
        themePanel.add(darculaTheme);
        themePanel.add(intelliJTheme);
        themePanel.add(solarizedDarkTheme);
        themePanel.add(solarizedLightTheme);
        themePanel.add(highContrastDarkTheme);
        themePanel.add(highContrastLightTheme);
        themePanel.setBorder(new TitledBorder("Themes"));
        optionDialog.getContentPane().add(themePanel, BorderLayout.CENTER);

        JPanel otherOptionsPanel = new JPanel(new FlowLayout());
        otherOptionsPanel.setBorder(new TitledBorder("Other"));
        otherOptionsPanel.add(ExitOnClose);
        otherOptionsPanel.add(followRedirectCheckBox);
        otherOptionsPanel.add(saveSetting);
        optionDialog.getContentPane().add(otherOptionsPanel, BorderLayout.SOUTH);

        optionDialog.setTitle("Option");
        optionDialog.setBounds(300, 200, 350, 200);
        optionDialog.setVisible(true);
        saveSetting.addActionListener(new ActionListener() {
            @Override
            /**
             * changing the status of program as the user choose
             */
            public void actionPerformed(ActionEvent e) {
                if (darculaTheme.isSelected()) {
                    themeTypeString = "darculaTheme";
                    LafManager.install(new DarculaTheme());
                } else if (highContrastLightTheme.isSelected()) {
                    themeTypeString = "highContrastLightTheme";
                    LafManager.install(new HighContrastLightTheme());
                } else if (solarizedLightTheme.isSelected()) {
                    themeTypeString = "solarizedLightTheme";
                    LafManager.install(new SolarizedLightTheme());
                } else if (solarizedDarkTheme.isSelected()) {
                    themeTypeString = "solarizedDarkTheme";
                    LafManager.install(new SolarizedDarkTheme());
                } else if (highContrastDarkTheme.isSelected()) {
                    themeTypeString = "highContrastDarkTheme";
                    LafManager.install(new HighContrastDarkTheme());
                } else if (intelliJTheme.isSelected()) {
                    themeTypeString = "intelliJTheme";
                    LafManager.install(new IntelliJTheme());
                }
                frame.revalidate();
                frame.repaint();
                //TODO:add follow redirect action listener
                optionDialog.dispose();
            }
        });

    }

    /**
     * return the isSystemTrayEnabled value
     * @return true if enabled
     */
    public static boolean isIsSystemTrayEnabled() {
        return isSystemTrayEnabled;
    }

    /**
     * return the isFollowRedirect value
     * @return true if enabled
     */
    public static boolean isFollowRedirect() {
        return followRedirect;
    }

    /**
     * handles the main events of the menubar
     */
    public class handler implements ActionListener, ChangeListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == exitItem) {
                //save setting before terminating or making it system tray
                GUIClient.save();
                System.exit(0);
            } else if (e.getSource() == optionsItem) {
                makeOptionDialog();
            } else if (e.getSource() == toggleFullScreenItem) {

                if (frame.getExtendedState() == JFrame.NORMAL) {
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } else {
                    frame.setExtendedState(JFrame.NORMAL);
                }
            } else if (e.getSource() == toggleSidebarItem) {
                if (isSlideToggledActive) {
                    frame.getContentPane().remove(0);
                    frame.getContentPane().add(frame.getLeftAndRightPanels());
                    isSlideToggledActive = false;
                } else {
                    frame.getContentPane().remove(0);
                    frame.getContentPane().add(frame.getRightPanels());
                    isSlideToggledActive = true;
                }
                frame.revalidate();
                frame.repaint();
            } else if (e.getSource() == helpItem) {
                JOptionPane.showMessageDialog(frame, "Help"
                        , "Help", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == aboutItem) {
                JOptionPane.showMessageDialog(frame, "Hi it's Ali, this is my midterm project in AP" +
                                "\nEmail: Aliasad059@gmail.com" +
                                "\nAUT mail:Aliasad059@aut.ac.ir" +
                                "\nStudent Number : 9831004"
                        , "About", JOptionPane.INFORMATION_MESSAGE);
            }
            if (e.getSource() == ExitOnClose) {
                isSystemTrayEnabled = !isSystemTrayEnabled;
            } else if (e.getSource() == followRedirectCheckBox) {
                followRedirect = !followRedirect;
            }
        }

        /**
         * Invoked when the target of the listener has changed its state.
         *
         * @param e a ChangeEvent object
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == ExitOnClose) {
                isSystemTrayEnabled = !isSystemTrayEnabled;
            } else if (e.getSource() == followRedirectCheckBox) {
                followRedirect = !followRedirect;
            }
        }
    }

    /**
     * insomnia system tray
     */
    public class InsomniaSystemTray {
        public void makeSystemTray() {
            //checking for support
            if (!SystemTray.isSupported()) {
                System.out.println("System tray is not supported !!! ");
                return;
            }
            //get the systemTray of the system
            SystemTray systemTray = SystemTray.getSystemTray();

            //get default toolkit
            //Toolkit toolkit = Toolkit.getDefaultToolkit();
            //get image
            //Toolkit.getDefaultToolkit().getImage("src/resources/busylogo.jpg");
            Image image = Toolkit.getDefaultToolkit().getImage("src/images/1.gif");

            //popupmenu
            PopupMenu trayPopupMenu = new PopupMenu();

            //1t menuitem for popupmenu
            MenuItem open = new MenuItem("Open");
            open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
            trayPopupMenu.add(open);

            //2nd menuitem of popupmenu
            MenuItem close = new MenuItem("Close");
            close.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GUIClient.save();
                    System.exit(0);
                }
            });
            trayPopupMenu.add(close);

            //setting tray icon
            TrayIcon trayIcon = new TrayIcon(image, "Insomnia", trayPopupMenu);
            //adjust to default size as per system recommendation
            trayIcon.setImageAutoSize(true);

            try {
                systemTray.add(trayIcon);
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }
        }

    }

    /**
     * get theme type as user chose
     * @return string that contains theme name
     */
    public String getThemeType() {
        return themeTypeString;
    }

    /**
     * get exit on close check box
     * @return exit on close
     */
    public JCheckBox getExitOnClose() {
        return ExitOnClose;
    }
    /**
     * getFollowRedirectCheckBox
     * @return FollowRedirect
     */
    public JCheckBox getFollowRedirectCheckBox() {
        return followRedirectCheckBox;
    }

    /**
     * this is used when loading the configs and loads the last selections
     * @param status the last chosen status of exit on close
     */
    public void setExitOnClose(boolean status) {
        if ((ExitOnClose.isSelected() && !status) || (!ExitOnClose.isSelected() && status)) {
            ExitOnClose.doClick();
        }
    }

    /**
     * this is used when loading the configs and loads the last selections
     * @param status the last chosen status  of follow redirect
     */
    public void setFollowRedirect(boolean status) {
        if ((followRedirectCheckBox.isSelected() && !status) || (!followRedirectCheckBox.isSelected() && status)) {
            followRedirectCheckBox.doClick();
        }
    }

    /**
     * set theme
     * @param theme theme to set
     */
    public void setTheme(String theme) {
        if (theme == null){
            darculaTheme.doClick();
            LafManager.install(new DarculaTheme());
        }else {
            if (theme.equals("highContrastLightTheme")) {
                highContrastLightTheme.doClick();
                LafManager.install(new HighContrastLightTheme());
            } else if (theme.equals("solarizedLightTheme")) {
                solarizedLightTheme.doClick();
                LafManager.install(new SolarizedLightTheme());
            } else if (theme.equals("solarizedDarkTheme")) {
                solarizedDarkTheme.doClick();
                LafManager.install(new SolarizedDarkTheme());
            } else if (theme.equals("highContrastDarkTheme")) {
                highContrastDarkTheme.doClick();
                LafManager.install(new HighContrastDarkTheme());
            } else if (theme.equals("intelliJTheme")) {
                intelliJTheme.doClick();
                LafManager.install(new IntelliJTheme());
            } else {
                darculaTheme.doClick();
                LafManager.install(new DarculaTheme());
            }
        }
        frame.revalidate();
        frame.repaint();
    }
}
