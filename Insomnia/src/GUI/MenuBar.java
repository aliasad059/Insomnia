package GUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import com.github.weisj.darklaf.*;
import com.github.weisj.darklaf.theme.*;

public class MenuBar {
    private JMenuBar menuBar;
    private JMenu applicationMenu, viewMenu, helpMenu;
    private JMenuItem optionsItem, exitItem, toggleFullScreenItem, toggleSidebarItem, aboutItem, helpItem;
    private JFrame frame;
    private Display display;
    InsomniaSystemTray systemTray;
    JDialog optionDialog;
    JCheckBox followRedirectCheckBox, ExitOnClose;
    ButtonGroup themeType;
    JRadioButton darculaTheme, solatizedLightTheme, intelliJTheme, solarizedDarkTheme, highContrastDarkTheme, highContrastLightTheme;
    JButton saveSetting;
    private boolean isSlideToggledActive = false;
    JPanel requestsPanel;

    public MenuBar(Display display) {
        this.display = display;
        frame = display.getFrame();
        menuBar = new JMenuBar();
        applicationMenu = new JMenu("Application");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");
        optionsItem = new JMenuItem("Options");
        exitItem = new JMenuItem("Exit");
        toggleFullScreenItem = new JMenuItem("Toggle Full Screen");
        toggleSidebarItem = new JMenuItem("Toggle Sidebar");
        aboutItem = new JMenuItem("About");
        helpItem = new JMenuItem("Help");

        menuBar.add(applicationMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

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
        ExitOnClose = new JCheckBox("Exit On Close");
        themeType = new ButtonGroup();
        darculaTheme = new JRadioButton("Darcula");
        solatizedLightTheme = new JRadioButton("Solatized ght");
        intelliJTheme = new JRadioButton("IntelliJ");
        solarizedDarkTheme = new JRadioButton("Solarized Dark");
        highContrastDarkTheme = new JRadioButton("High Contrast Dark");
        highContrastLightTheme = new JRadioButton("High Contrast Light");

        themeType.add(darculaTheme);
        themeType.add(intelliJTheme);
        themeType.add(highContrastLightTheme);
        themeType.add(solatizedLightTheme);
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

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private void makeOptionDialog() {
        optionDialog = new JDialog();
        //optionDialog.validate();
        optionDialog.setLayout(new BorderLayout());
        JPanel themePanel = new JPanel();
        themePanel.setLayout(new FlowLayout());
        themePanel.add(darculaTheme);
        themePanel.add(intelliJTheme);
        themePanel.add(solarizedDarkTheme);
        themePanel.add(solatizedLightTheme);
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

    }

    private class handler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == exitItem) {
                //save setting before terminating or making it system tray
                System.exit(0);
            } else if (e.getSource() == optionsItem) {
                makeOptionDialog();
            } else if (e.getSource() == toggleFullScreenItem) {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else if (e.getSource() == toggleSidebarItem) {
                if (isSlideToggledActive) {
                    frame.getContentPane().remove(display.getRightPanels());
                    frame.getContentPane().add(display.getLeftAndRightPanels());
                    isSlideToggledActive=false;
                 }
                else {
                    frame.getContentPane().remove(display.getLeftAndRightPanels());
                    frame.getContentPane().add(display.getRightPanels());
                    isSlideToggledActive = true;
                }
                frame.revalidate();
                frame.repaint();
            } else if (e.getSource() == helpItem) {

            } else if (e.getSource() == aboutItem) {
            }
        }
    }

    private class InsomniaSystemTray {
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

}
