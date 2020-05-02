package GUI;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Reader;

import static GUI.Display.FRAME_WIDTH;
public class MiddlePanel {
    JPanel middlePanel;
    JPanel northMiddlePanel;
    JTabbedPane tabs ;
    JPanel bodyPanel ;
    JPanel authPanel ;
    JPanel queryPanel ;
    JPanel headerPanel ;

    public MiddlePanel() {
        middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());
        middlePanel.setBackground(Color.GRAY);
        northMiddlePanel =new JPanel();
        northMiddlePanel.setLayout(new GridLayout(1, 3));
        String[] methodsName = {"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"};
        northMiddlePanel.add(new JComboBox(methodsName));
        northMiddlePanel.add(new JTextField());
        northMiddlePanel.add(new JButton("Send"));

        middlePanel.add(northMiddlePanel, BorderLayout.NORTH);

         tabs = new JTabbedPane();
         bodyPanel = new JPanel();
        bodyPanel.setLayout(new BorderLayout());
         authPanel = new JPanel();
         queryPanel = new JPanel();
         headerPanel = new JPanel();

        tabs.addTab("Body", bodyPanel);
        tabs.addTab("Auth", authPanel);
        tabs.addTab("Query", queryPanel);
        tabs.addTab("Header", headerPanel);

        makeBodyPanel();
        makeAuthPanel();
        makeHeaderPanel();
        makeQueryPanel();

        middlePanel.add(tabs, BorderLayout.CENTER);

    }
    private void makeQueryPanel(){
        ////////////////////making query panel
        JLabel urlPreviewLabel = new JLabel("URL preview");
        JTextField urlPreviewField = new JTextField();
        urlPreviewField.setPreferredSize(new Dimension(FRAME_WIDTH / 6, 30));
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
    }
    private void makeAuthPanel(){
        //////////////////////making Auth panel

        JLabel tokenLabel = new JLabel("TOKEN");
        JLabel prefixLabel = new JLabel("PREFIX");
        JTextField tokenField = new JTextField("");
        tokenField.setMinimumSize(new Dimension(FRAME_WIDTH / 8, 20));
        tokenField.setPreferredSize(new Dimension(2 * FRAME_WIDTH / 9, 20));
        JTextField prefixField = new JTextField("");
        prefixField.setMinimumSize(new Dimension(FRAME_WIDTH / 8, 20));
        prefixField.setPreferredSize(new Dimension(2 * FRAME_WIDTH / 9, 20));
        JCheckBox isEnabled = new JCheckBox("ENABLED");
        isEnabled.setHorizontalTextPosition(SwingConstants.LEFT);

        JPanel firstLine = new JPanel();
        firstLine.add(tokenLabel);
        firstLine.add(tokenField);
        JPanel secondLine = new JPanel();
        secondLine.add(prefixLabel);
        secondLine.add(prefixField);
        JPanel thirdLine = new JPanel();
        thirdLine.setPreferredSize(firstLine.getPreferredSize());
        thirdLine.add(isEnabled);
        authPanel.add(firstLine);
        authPanel.add(secondLine);
        authPanel.add(thirdLine);
        //////////////////////////////////////////////////////
    }
    private void makeBodyPanel(){
        ////////////////////////////// making body panel


        String[] bodyTab = {"No Body", "Form", "JASON", "Binary"};
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
                    middlePanel.updateUI();
                }
            }
        });
        JsonViewerPanel jsonViewerPanel = new JsonViewerPanel();
        JPanel JSONPanel = new JPanel(new BorderLayout());
        JScrollPane sp = new JScrollPane(jsonViewerPanel);
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
                    bodyPanel.remove(1);
                    bodyPanel.add(formPanel, BorderLayout.CENTER);
                } else if (cb.getSelectedIndex() == 2) {
                    bodyPanel.remove(1);
                    bodyPanel.add(JSONPanel, BorderLayout.CENTER);
                } else if (cb.getSelectedIndex() == 3) {
                    bodyPanel.remove(1);
                    bodyPanel.add(binaryPanel, BorderLayout.CENTER);
                } else {
                    bodyPanel.remove(1);
                    bodyPanel.add(noBodyPanel, BorderLayout.CENTER);
                }
                middlePanel.updateUI();
            }
        });
        //////////////////////////////////////////
    }
    private void makeHeaderPanel(){
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
    }

    private JPanel formMaker() {
        JPanel formPanel = new JPanel();

        JTextField nameField = new JTextField("Name");
        nameField.setMinimumSize(new Dimension(FRAME_WIDTH / 8 - 80, 25));
        nameField.setPreferredSize(new Dimension(FRAME_WIDTH / 8 - 30, 25));
        nameField.setMaximumSize(new Dimension(FRAME_WIDTH / 8 + 20, 25));
        JTextField valueField = new JTextField("Value");
        valueField.setMinimumSize(new Dimension(FRAME_WIDTH / 8 - 80, 25));
        valueField.setPreferredSize(new Dimension(FRAME_WIDTH / 8 - 30, 25));
        valueField.setMaximumSize(new Dimension(FRAME_WIDTH / 8 + 20, 25));
        JCheckBox isActive = new JCheckBox();
        JButton removeForm = new JButton("x");
        removeForm.setPreferredSize(new Dimension(20, 20));

        formPanel.add(nameField);
        formPanel.add(valueField);
        formPanel.add(isActive);
        formPanel.add(removeForm);
        return formPanel;
    }
    private class handler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: needs a list of forms maybe
        }
    }
    public JPanel getMiddlePanel() {
        return middlePanel;
    }

}

