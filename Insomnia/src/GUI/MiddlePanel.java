package GUI;


import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import HttpClient.GUIClient;
import HttpClient.Request;
import com.github.jutil.json.gui.*;

import static GUI.InsomniaFrame.FRAME_WIDTH;

/**
 * the middle panel in insomnia
 */
public class MiddlePanel extends JPanel {
    JPanel northMiddlePanel;
    JTabbedPane tabs;
    JPanel bodyPanel;
    JPanel bodyFormPanel;
    JPanel authPanel;
    JPanel queryPanel;
    JPanel headerPanel;
    JComboBox bodyTabStatus, requestMethodType;
    JPanel binaryPanel;
    JButton fileChooserButton, sendRequest , copyURLButton;
    JTextField url, filePath;
    JPanel JSONPanel;
    JPanel noBodyPanel;
    JsonViewerPanel jsonViewerPanel;
    JTextField urlPreviewField;
    Request owner;
    ArrayList<Form> formData, queries, headers;


    /**
     * this panel is divided by border layout into north and center
     * in north the are controlling buttons
     * center panel holds a tabbed pane with different items
     */
    public MiddlePanel() {
        formData = new ArrayList<>();
        queries = new ArrayList<>();
        headers = new ArrayList<>();

        setLayout(new BorderLayout());
        northMiddlePanel = new JPanel();
        northMiddlePanel.setLayout(new GridLayout(1, 3));
        String[] methodsName = {"GET", "POST", "PUT", "PATCH", "DELETE"};
        requestMethodType = new JComboBox(methodsName);
        requestMethodType.addActionListener(new handler());
        northMiddlePanel.add(requestMethodType);
        northMiddlePanel.add(url = new JTextField());
        url.addFocusListener(new handler());
        northMiddlePanel.add(sendRequest = new JButton("Send" + '\u21E8'));
        sendRequest.addActionListener(new handler());
        add(northMiddlePanel, BorderLayout.NORTH);
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

        add(tabs, BorderLayout.CENTER);

    }

    /**
     * makes query panel
     */
    private void makeQueryPanel() {
        ////////////////////making query panel
        JLabel urlPreviewLabel = new JLabel("URL Preview");
        urlPreviewField = new JTextField();
        urlPreviewField.setPreferredSize(new Dimension(FRAME_WIDTH / 6, 30));
        urlPreviewField.setEnabled(false);
         copyURLButton = new JButton("Copy URL");
        copyURLButton.addActionListener(new handler());
        queryPanel.add(urlPreviewLabel);
        queryPanel.add(urlPreviewField);
        queryPanel.add(copyURLButton);
        queryPanel.add(new Form(queryPanel));

        //////////////////////////////////////////////////////
    }

    /**
     * makes auth panel
     */
    private void makeAuthPanel() {
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

    /**
     * makes body panel and its subcomponents
     */
    private void makeBodyPanel() {
        String[] bodyTab = {"No Body", "Form", "JASON", "Binary"};
        bodyTabStatus = new JComboBox(bodyTab);
        bodyPanel.add(bodyTabStatus, BorderLayout.NORTH);

        binaryPanel = new JPanel();
        binaryPanel.setLayout(new FlowLayout());
        fileChooserButton = new JButton("Choose file");
        filePath = new JTextField("         Path of chosen file          ");
        binaryPanel.add(fileChooserButton);
        binaryPanel.add(filePath);

        fileChooserButton.addActionListener(new handler());

        jsonViewerPanel = new JsonViewerPanel();

        JSONPanel = new JPanel(new BorderLayout());
        JSONPanel.add(jsonViewerPanel, BorderLayout.CENTER);

        bodyFormPanel = new JPanel();

        bodyFormPanel.add(new Form(bodyFormPanel));

        noBodyPanel = new JPanel();

        bodyPanel.add(noBodyPanel);

        bodyTabStatus.addActionListener(new handler());
    }

    /**
     * makes a header panel
     */
    private void makeHeaderPanel() {
        headerPanel.add(new Form(headerPanel));
    }

    /**
     * handling main events of middle panel
     */
    private class handler implements ActionListener, FocusListener {
        /**
         * choosing the format of the body panel as selected in combobox
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendRequest) {
                initializeRequest();
                GUIClient.runRequest(owner);
            }
            if (e.getSource() == bodyTabStatus) {
                JComboBox cb = (JComboBox) e.getSource();
                if (cb.getSelectedIndex() == 1) {
                    bodyPanel.remove(1);
                    bodyPanel.add(bodyFormPanel, BorderLayout.CENTER);
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
                updateUI();
            }
            if (e.getSource() == requestMethodType) {
                JComboBox cb = (JComboBox) e.getSource();
                owner.setMethod((String) cb.getSelectedItem());
                updateUI();
            }
            if (e.getSource() == copyURLButton){
                StringSelection stringSelection = new StringSelection(urlPreviewField.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
            /**
             * if user chooses to open binary file
             */
            if (e.getSource() == fileChooserButton) {
                JFileChooser fc = new JFileChooser();
                int i = fc.showOpenDialog(null);
                if (i == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    String filepath = f.getPath();
                    JOptionPane.showMessageDialog(null, filepath, "You have chosen following file ...", 1);
                    filePath.setText(filepath);
                    updateUI();
                }
            }
        }

        /**
         * Invoked when a component gains the keyboard focus.
         *
         * @param e the event to be processed
         */
        @Override
        public void focusGained(FocusEvent e) {
            if (e.getSource() == url) {
                urlPreviewField.setText(url.getText());
                setQueries();
            }
        }

        /**
         * Invoked when a component loses the keyboard focus.
         *
         * @param e the event to be processed
         */
        @Override
        public void focusLost(FocusEvent e) {
            if (e.getSource() == url) {
                urlPreviewField.setText(url.getText());
                setQueries();
            }
        }
    }

    private void initializeRequest() {
        owner.setMethod((String) requestMethodType.getSelectedItem());

        owner.setUri(urlPreviewField.getText());

        if (!urlPreviewField.getText().equals("")) {
            owner.setCompleted(true);
        }
        //no body
        if (bodyTabStatus.getSelectedIndex() == 0) {
            owner.setUpload(null);
            owner.setData(null);
            owner.setJson(null);
        }
        //form data
        else if (bodyTabStatus.getSelectedIndex() == 1) {
            String formDataString = "";
            for (Form formDatum : formData) {
                if (formDatum.IsActive()) {
                    formDataString += formDatum.getNameField().getText();
                    formDataString += '=';
                    formDataString += formDatum.getValueField().getText();
                    formDataString += '&';
                }
            }
            //removing the last & of the string
            formDataString = formDataString.substring(0, formDataString.length() - 1);
            owner.setData(formDataString);
            owner.setJson(null);
            owner.setUpload(null);
        }
        //json
        else if (bodyTabStatus.getSelectedIndex() == 2) {
            owner.setJson(jsonViewerPanel.getText());
            owner.setData(null);
            owner.setUpload(null);
        }
        //binary file
        else {
            owner.setUpload(filePath.getText());
            owner.setData(null);
            owner.setJson(null);
        }
        owner.setSaveRequest(true);

        String headerString = "";
        for (Form header : headers) {
            if (header.IsActive()) {
                headerString += header.getNameField().getText();
                headerString += ":";
                headerString += header.getValueField().getText();
                headerString += ";";
            }
        }
        if (!headerString.equals("")) {
            headerString = headerString.substring(0, headerString.length() - 1);
        }
        System.out.println(headerString);
        owner.setHeaders(headerString);
    }

    /**
     * this class makes us form
     * a form has name ,value ,status and remove items
     */
    class Form extends JPanel {
        private JPanel formOwner;
        private JTextField nameField, valueField;
        private JCheckBox isActive;
        private JButton removeForm;
        private Form form;

        public Form(JPanel formOwner) {
            this.setBorder(BorderFactory.createLoweredBevelBorder());
            this.formOwner = formOwner;
            nameField = new JTextField("Name");
            //nameField.setMinimumSize(new Dimension(FRAME_WIDTH / 8 - 50, 25));
            nameField.setPreferredSize(new Dimension(FRAME_WIDTH / 8 + 50, 25));
            nameField.setMaximumSize(new Dimension(FRAME_WIDTH / 8 + 100, 25));
            valueField = new JTextField("Value");
            //valueField.setMinimumSize(new Dimension(FRAME_WIDTH / 8 - 50, 25));
            valueField.setPreferredSize(new Dimension(FRAME_WIDTH / 8 + 50, 25));
            valueField.setMaximumSize(new Dimension(FRAME_WIDTH / 8 + 100, 25));
            isActive = new JCheckBox();

            removeForm = new JButton("x");
            removeForm.setPreferredSize(new Dimension(20, 20));
            this.add(nameField);
            this.add(valueField);
            this.add(isActive);
            this.add(removeForm);

            isActive.addActionListener(new handler());
            removeForm.addActionListener(new handler());
            nameField.addFocusListener(new handler());
            valueField.addFocusListener(new handler());
            if (formOwner == queryPanel) {
                System.out.println("ADD to query");
                queries.add(this);
            } else if (formOwner == headerPanel) {
                System.out.println("ADD to headers");
                headers.add(this);
            } else if (formOwner == bodyFormPanel) {
                System.out.println("ADD to form");

                formData.add(this);
                System.out.println();
            }
            form = this;
        }

        public boolean IsActive() {
            return isActive.isSelected();
        }

        public JTextField getNameField() {
            return nameField;
        }

        public JTextField getValueField() {
            return valueField;
        }

        /**
         * handling form class events
         */
        class handler implements FocusListener, ActionListener {
            /**
             * if the user focused on the fields , make a new form
             *
             * @param e
             */
            @Override
            public void focusGained(FocusEvent e) {
                //add a new form
                if (e.getSource() == nameField || e.getSource() == valueField) {
                    formOwner.add(new Form(formOwner));
                }
            }

            /**
             * if the focus lost
             *
             * @param e
             */
            @Override
            public void focusLost(FocusEvent e) {
                //update query as the fields changed
                if (e.getSource() == nameField || e.getSource() == valueField) {
                    setQueries();
                }
            }

            /**
             * if the form should be enabled/disabled or be removed
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == isActive) {
                    if (isActive.isSelected()) {
                        form.setBorder(BorderFactory.createRaisedBevelBorder());
                    } else {
                        form.setBorder(BorderFactory.createLoweredBevelBorder());
                    }
                    if (formOwner == queryPanel) {
                        setQueries();
                    }
                } else if (e.getSource() == removeForm) {
                    int a = JOptionPane.showConfirmDialog(form, "Are You Sure?");
                    if (a == JOptionPane.YES_OPTION) {
                        formOwner.remove(form);
                        if (formOwner == queryPanel) {
                            queries.remove(form);
                        } else if (formOwner == headerPanel) {
                            headers.remove(form);
                        } else if (formOwner == bodyFormPanel) {
                            formData.remove(form);
                        }
                    }
                }
            }
        }
    }
    private void setQueries(){
        String urlPreview = url.getText();
        urlPreview += '?';
        for (Form queryForm : queries) {
            if (queryForm.IsActive()) {
                urlPreview += queryForm.getNameField().getText();
                urlPreview += '=';
                urlPreview += queryForm.getValueField().getText();
                urlPreview += '&';
            }
        }
        urlPreview = urlPreview.substring(0, urlPreview.length() - 1);
        urlPreviewField.setText(urlPreview);
    }
    public void setOwner(Request owner) {
        this.owner = owner;
    }
}

