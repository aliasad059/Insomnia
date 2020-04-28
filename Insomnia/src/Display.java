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
        frame.pack();
    }

    private void makeNewRequestPanel() {
        newRequestPanel = new JPanel();
        newRequestPanel.setLayout(new BorderLayout());
        newRequestPanel.setBackground(Color.GRAY);
        JPanel north = new JPanel();
        north.setLayout(new GridLayout(1, 3));
        String[] methodsName = {"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"};
        north.add(new JComboBox(methodsName));
        north.add(new JTextField());
        north.add(new JButton("Send"));

        newRequestPanel.add(north, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BorderLayout());
        JPanel authPanel = new JPanel();
        JPanel queryPanel = new JPanel();
        JPanel headerPanel = new JPanel();
        JPanel docsPanel = new JPanel();

        tabs.addTab("Body", bodyPanel);
        tabs.addTab("Auth", authPanel);
        tabs.addTab("Query", queryPanel);
        tabs.addTab("Header", headerPanel);
        tabs.addTab("Docs", docsPanel);


        ////////////////////////////// making body panel


        String[] bodyTab = {"No body", "Form", "JASON", "Binary"};
        JComboBox bodyTabStatus = new JComboBox(bodyTab);
        bodyPanel.add(bodyTabStatus, BorderLayout.NORTH);

        JPanel binaryPanel = new JPanel();
        binaryPanel.setLayout(new BorderLayout());
        JButton fileChooserButton = new JButton("Choose file");
        binaryPanel.add(fileChooserButton, BorderLayout.NORTH);
        final JTextField[] filePath = new JTextField[1];

        fileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int i = fc.showOpenDialog(null);
                if (i == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    String filepath = f.getPath();
                    JOptionPane.showMessageDialog(null, filepath, "You have chosen following file ...", 1);
                    filePath[0] = new JTextField(filepath);
                    frame.validate();
                }
            }
        });

        JPanel JSONPanel = new JPanel(new BorderLayout());
        JTextArea JSONEditor = new JTextArea("");
        JScrollPane sp = new JScrollPane(JSONEditor);
        JSONPanel.add(sp, BorderLayout.CENTER);

        JPanel formPanel = new JPanel();
        //JScrollPane formScroller = new JScrollPane();
        //formPanel.setLayout(new BoxLayout(formPanel,BoxLayout.PAGE_AXIS));
        JButton addFormButton = new JButton("Add new form");

        addFormButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formPanel.add(formMaker());
                formPanel.updateUI();
            }
        });
        formPanel.add(addFormButton);

        JPanel noBodyPanel = new JPanel();

        bodyPanel.add(noBodyPanel);

        bodyTabStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                if (cb.getSelectedIndex() == 1) {
                    System.out.println(1);
                    bodyPanel.remove(1);
                    bodyPanel.add(formPanel, BorderLayout.CENTER);
                } else if (cb.getSelectedIndex() == 2) {
                    System.out.println(2);
                    bodyPanel.remove(1);
                    bodyPanel.add(JSONPanel, BorderLayout.CENTER);
                } else if (cb.getSelectedIndex() == 3) {
                    System.out.println(3);
                    bodyPanel.remove(1);
                    bodyPanel.add(binaryPanel, BorderLayout.CENTER);
                } else {
                    System.out.println(0);
                    bodyPanel.remove(1);
                    bodyPanel.add(noBodyPanel, BorderLayout.CENTER);
                }
                requestsPanel.updateUI();
            }
        });
        //////////////////////////////////////////


        //////////////////////making Auth panel

        JLabel tokenLabel = new JLabel("TOKEN");
        JLabel prefixLabel = new JLabel("PREFIX");
        JTextField tokenField = new JTextField("");
        tokenField.setMinimumSize(new Dimension(FRAME_WIDTH/8,20));
        tokenField.setPreferredSize(new Dimension(2*FRAME_WIDTH/9,20));
        JTextField prefixField = new JTextField("");
        prefixField.setMinimumSize(new Dimension(FRAME_WIDTH/8,20));
        prefixField.setPreferredSize(new Dimension(2*FRAME_WIDTH/9,20));
        JCheckBox isEnabled = new JCheckBox("ENABLED");
        isEnabled.setHorizontalTextPosition(SwingConstants.LEFT);

        authPanel.add(tokenLabel);
        authPanel.add(tokenField);
        authPanel.add(prefixLabel);
        authPanel.add(prefixField);
        authPanel.add(isEnabled);
        //////////////////////////////////////////////////////


        /////////////////////making header panel
        JButton addHeaderButton = new JButton("Add new header");

        addHeaderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                headerPanel.add(formMaker());
                headerPanel.updateUI();
            }
        });
        headerPanel.add(addHeaderButton);
        /////////////////////////////////////////////////////


        ////////////////////making query panel
        JLabel urlPreviewLabel = new JLabel("URL preview");
        JTextField urlPreviewField = new JTextField();
        urlPreviewField.setPreferredSize(new Dimension(FRAME_WIDTH/6,30));
        JButton copyURLButton = new JButton();

        queryPanel.add(urlPreviewLabel);
        queryPanel.add(urlPreviewField);
        queryPanel.add(copyURLButton);
        JButton addQueryButton = new JButton("Add new query");
        addQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryPanel.add(formMaker());
                queryPanel.updateUI();
            }
        });
        queryPanel.add(addQueryButton);

        //////////////////////////////////////////////////////

        newRequestPanel.add(tabs, BorderLayout.CENTER);


    }

    private void makeResponsePanel() {
        responsePanel = new JPanel();
        responsePanel.setBackground(Color.DARK_GRAY);
        //responsePanel.setBounds(2*FRAME_WIDTH/3,0,FRAME_WIDTH/3,FRAME_HEIGHT);
        responsePanel.setLayout(new BorderLayout());
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

    private void darkTheme() {
    }

    private void lightTheme() {
    }

}
