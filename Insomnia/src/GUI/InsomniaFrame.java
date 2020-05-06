package GUI;

import com.sun.xml.internal.bind.v2.TODO;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InsomniaFrame extends JFrame {
    TrayIcon trayIcon;
    SystemTray tray;
    MenuItem exitItem, openItem;

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

            Image image = Toolkit.getDefaultToolkit().getImage("");

            tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(image, "Insomnia", popup);
            trayIcon.setImageAutoSize(true);
        } else {
            System.out.println("system tray not supported");
        }

        addWindowListener(new handler());
    }
    class handler extends WindowAdapter implements ActionListener {

        /**
         * Invoked when a window is in the process of being closed.
         * The close operation can be overridden at this point.
         *
         * @param e
         */
        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            if (MenuBar.isIsSystemTrayEnabled()) {
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
}