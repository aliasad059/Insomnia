import javafx.scene.control.ComboBox;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Display {
    JFrame frame;
    final int FRAME_WIDTH = 1500, FRAME_HEIGHT = 800;
    JPanel requestsPanel, newRequestPanel, responsePanel;
    GroupLayout requestsPanelLayout;
    //    ArrayList<JButton> requestsButton;
    JButton requestButton;
    //ArrayList<Request> requests;
    JButton addNewRequestButton;
    GroupLayout.ParallelGroup parallel;
    GroupLayout.SequentialGroup sequential;

    public Display() {
        frame = new JFrame("Insomnia");
        frame.validate();
        //requests = Request.getRequests;
        makeRequestsPanel();
        makeNewRequestPanel();
        makeResponsePanel();

        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(new GridLayout(1, 3));
        frame.getContentPane().add(requestsPanel);
        frame.getContentPane().add(newRequestPanel);
        frame.getContentPane().add(responsePanel);
        frame.setVisible(true);
    }

    private void makeRequestsPanel() {

        requestsPanel = new JPanel();
        requestsPanelLayout = new GroupLayout(requestsPanel);
        requestsPanel.setLayout(requestsPanelLayout);
        requestsPanelLayout.setAutoCreateGaps(true);
        requestsPanelLayout.setAutoCreateContainerGaps(true);

        //requestsPanel.setBackground(Color.WHITE);
        JButton insomniaButton = new JButton("Insomnia");
        //insomniaButton.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));

        addNewRequestButton = new JButton("add new request");


        parallel = requestsPanelLayout.createParallelGroup();
        requestsPanelLayout.setHorizontalGroup(
                requestsPanelLayout.createParallelGroup()
                        .addComponent(insomniaButton, 100, 200, 200)
                        .addComponent(addNewRequestButton, 100, 200, 200)
                        .addGroup(parallel));
        sequential = requestsPanelLayout.createSequentialGroup();
        requestsPanelLayout.setVerticalGroup(
                requestsPanelLayout.createSequentialGroup()
                        .addComponent(insomniaButton)
                        .addComponent(addNewRequestButton)
                        .addGroup(sequential));

        addNewRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRequest("My request", "Get");
            }
        });
        addRequest("a", "b");

    }

    private void addRequest(String name, String method) {
        //String method = JOptionPane.showInputDialog("Enter method type");
        //String name = JOptionPane.showInputDialog("Enter request's name");
        JLabel label = new JLabel(method);
        requestButton = new JButton(name);
        label.setLabelFor(requestButton);
        parallel.addGroup(requestsPanelLayout.createSequentialGroup().
                addComponent(label).addComponent(requestButton));
        sequential.addGroup(requestsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(label).addComponent(requestButton));
        frame.validate();
    }

    private void makeNewRequestPanel() {
        newRequestPanel = new JPanel();
    }
    private void makeResponsePanel() {
        responsePanel = new JPanel();
    }

    private void darkTheme() {
    }

    private void lightTheme() {
    }

}
