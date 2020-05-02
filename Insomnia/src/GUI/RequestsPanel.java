package GUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.Highlighter;
import javax.swing.text.IconView;
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
    private JTextField filterField;


    public RequestsPanel() {
        requestsButton = new ArrayList<>();
        requestsPanel = new JPanel(new BorderLayout());
        JPanel northRequestPanel = new JPanel();
        JPanel centerRequestPanel = new JPanel(new BorderLayout());
        JPanel northCenterRequestPanel= new JPanel();
        JPanel centerCenterRequestPanel = new JPanel();

        requestsPanelLayout = new GroupLayout(centerCenterRequestPanel);
        centerCenterRequestPanel.setLayout(requestsPanelLayout);
        centerCenterRequestPanel.setBorder(new TitledBorder("Requests"));
        requestsPanelLayout.setAutoCreateGaps(true);
        requestsPanelLayout.setAutoCreateContainerGaps(true);

        JButton insomniaButton = new JButton("Insomnia");
        insomniaButton.setPreferredSize(new Dimension(200,30));
        addNewRequestButton = new JButton("+");
        addNewRequestButton.setPreferredSize(new Dimension(50,25));
        filterField = new JTextField("");
        filterField.setPreferredSize(new Dimension(150,25));
        insomniaButton.setBackground(Color.MAGENTA);

        northRequestPanel.add(insomniaButton);
        northCenterRequestPanel.add(filterField);
        northCenterRequestPanel.add(addNewRequestButton);

        requestsPanel.add(northRequestPanel,BorderLayout.NORTH);
        centerRequestPanel.add(northCenterRequestPanel , BorderLayout.NORTH);
        centerRequestPanel.add(centerCenterRequestPanel, BorderLayout.CENTER);
        requestsPanel.add(centerRequestPanel,BorderLayout.CENTER);

        parallel = requestsPanelLayout.createParallelGroup();
        requestsPanelLayout.setHorizontalGroup(
                requestsPanelLayout.createParallelGroup()
                        //.addComponent(insomniaButton, 100, 200, 300)
                        //.addComponent(addNewRequestButton, 100, 200, 300)
                        .addGroup(parallel));
        sequential = requestsPanelLayout.createSequentialGroup();
        requestsPanelLayout.setVerticalGroup(
                requestsPanelLayout.createSequentialGroup()
                        //.addComponent(insomniaButton)
                        //.addComponent(addNewRequestButton)
                        .addGroup(sequential));

        addNewRequestButton.addActionListener(new handler());
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
    class handler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addNewRequestButton){
                JDialog newRequestDialog =new JDialog();
                newRequestDialog.setVisible(true);
                JTextField nameFiled = new JTextField("My Request");
                nameFiled.setBorder(new TitledBorder("Name"));
                String[] methodsName = {"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"};
                JComboBox cb =new JComboBox(methodsName);
                cb.setSelectedIndex(0);

                JButton createRequestButton = new JButton("Creat");
                createRequestButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addRequest(nameFiled.getText(),(String) cb.getSelectedItem());
                        newRequestDialog.setVisible(false);
                        newRequestDialog.dispose();
                    }
                });

                newRequestDialog.setLayout(new BorderLayout());
                newRequestDialog.add(nameFiled,BorderLayout.CENTER);
                newRequestDialog.add(cb,BorderLayout.EAST);
                newRequestDialog.add(createRequestButton,BorderLayout.SOUTH);
                newRequestDialog.setTitle("New Request");
                newRequestDialog.setBounds(300, 200, 500, 125);

                newRequestDialog.setVisible(true);
            }
        }
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
