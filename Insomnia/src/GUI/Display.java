package GUI;


import com.github.weisj.darklaf.DarkLaf;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class Display {
    JFrame frame;
    static final int FRAME_WIDTH = 1500, FRAME_HEIGHT = 800;
    JPanel requestsPanel, middlePanel, responsePanel;
    RequestsPanel requestsPanelObj;
    MiddlePanel middlePanelObj;
    ResponsePanel responsePanelObj;
    MenuBar menuBarObj;
    JMenuBar menuBar;
    JSplitPane rightPanels, leftAndRightPanels;

    public Display() {


        frame = new JFrame("Insomnia");
        frame.validate();
        requestsPanelObj = new RequestsPanel();
        requestsPanel = requestsPanelObj.getRequestsPanel();

        middlePanelObj = new MiddlePanel();
        middlePanel = middlePanelObj.getMiddlePanel();

        responsePanelObj = new ResponsePanel();
        responsePanel = responsePanelObj.getResponsePanel();

        menuBarObj = new MenuBar(this);
        menuBar = menuBarObj.getMenuBar();

        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        //frame.setLayout(new GridLayout(1, 3));
        frame.setJMenuBar(menuBar);
        rightPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, middlePanel, responsePanel);
        rightPanels.setDividerLocation(FRAME_WIDTH / 3 + FRAME_WIDTH / 20);
        leftAndRightPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, requestsPanel, rightPanels);
        leftAndRightPanels.setDividerLocation(FRAME_WIDTH / 6);

        frame.getContentPane().add(leftAndRightPanels);

        frame.setVisible(true);


    }

    public JFrame getFrame() {
        return frame;
    }

    public JSplitPane getRightPanels() {
        return rightPanels;
    }

    public JSplitPane getLeftAndRightPanels() {
        return leftAndRightPanels;
    }
}
