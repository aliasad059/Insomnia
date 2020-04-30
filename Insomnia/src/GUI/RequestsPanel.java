package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static GUI.Display.FRAME_WIDTH;

public class RequestsPanel {
    private JPanel requestsPanel;
    private GroupLayout requestsPanelLayout;
    private JButton addNewRequestButton;
    private GroupLayout.ParallelGroup parallel;
    private GroupLayout.SequentialGroup sequential;
    private ArrayList<JButton> requestsButton;


    public RequestsPanel() {
        requestsButton = new ArrayList<>();
        requestsPanel = new JPanel();
        requestsPanelLayout = new GroupLayout(requestsPanel);
        requestsPanel.setLayout(requestsPanelLayout);
        requestsPanelLayout.setAutoCreateGaps(true);
        requestsPanelLayout.setAutoCreateContainerGaps(true);

        JButton insomniaButton = new JButton("Insomnia");

        addNewRequestButton = new JButton("add new request");

        parallel = requestsPanelLayout.createParallelGroup();
        requestsPanelLayout.setHorizontalGroup(
                requestsPanelLayout.createParallelGroup()
                        .addComponent(insomniaButton, 100, 200, 300)
                        .addComponent(addNewRequestButton, 100, 200, 300)
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
    }

    private void addRequest(String name, String method) {
        //String method = JOptionPane.showInputDialog("Enter method type");
        //String name = JOptionPane.showInputDialog("Enter request's name");
        JLabel label = new JLabel(method);
        JButton requestButton = new JButton(name);
        label.setLabelFor(requestButton);
        parallel.addGroup(requestsPanelLayout.createSequentialGroup().
                addComponent(label).addComponent(requestButton));
        sequential.addGroup(requestsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(label).addComponent(requestButton));
        requestsPanel.updateUI();
        requestsButton.add(requestButton);
    }

    public JPanel getRequestsPanel() {
        return requestsPanel;
    }
    private JPanel formMaker() {
        JPanel formPanel = new JPanel();

        JTextField nameField = new JTextField("Name");
        nameField.setMinimumSize(new Dimension(FRAME_WIDTH / 8 - 80, 20));
        nameField.setPreferredSize(new Dimension(FRAME_WIDTH / 8 - 30, 20));
        nameField.setMaximumSize(new Dimension(FRAME_WIDTH / 8 + 20, 20));
        JTextField valueField = new JTextField("Value");
        valueField.setMinimumSize(new Dimension(FRAME_WIDTH / 8 - 80, 20));
        valueField.setPreferredSize(new Dimension(FRAME_WIDTH / 8 - 30, 20));
        valueField.setMaximumSize(new Dimension(FRAME_WIDTH / 8 + 20, 20));
        JCheckBox isActive = new JCheckBox();
        JButton removeForm = new JButton("x");
        removeForm.setPreferredSize(new Dimension(15, 15));

        formPanel.add(nameField);
        formPanel.add(valueField);
        formPanel.add(isActive);
        formPanel.add(removeForm);
        return formPanel;
    }

}
