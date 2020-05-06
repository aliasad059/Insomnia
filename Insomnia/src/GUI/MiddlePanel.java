package GUI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import com.github.jutil.json.gui.*;
import static GUI.Display.FRAME_WIDTH;

public class MiddlePanel {
    JPanel middlePanel;
    JPanel northMiddlePanel;
    JTabbedPane tabs;
    JPanel bodyPanel;
    JPanel bodyFormPanel;
    JPanel authPanel;
    JPanel queryPanel;
    JPanel headerPanel;

    JTextField urlPreviewField;

    public MiddlePanel() {
        middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());
        middlePanel.setBackground(Color.GRAY);
        northMiddlePanel = new JPanel();
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

    private void makeQueryPanel() {
        ////////////////////making query panel
        JLabel urlPreviewLabel = new JLabel("URL Preview");
        JTextField urlPreviewField = new JTextField();
        urlPreviewField.setPreferredSize(new Dimension(FRAME_WIDTH / 6, 30));
        JButton copyURLButton = new JButton("Copy URL");
        queryPanel.add(urlPreviewLabel);
        queryPanel.add(urlPreviewField);
        queryPanel.add(copyURLButton);
        queryPanel.add(new Form(queryPanel));

        //////////////////////////////////////////////////////
    }

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

    private void makeBodyPanel() {
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
                    //TODO: change the text
                }
            }
        });
        JsonViewerPanel jsonViewerPanel = new JsonViewerPanel();
        JPanel JSONPanel = new JPanel(new BorderLayout());
        JScrollPane sp = new JScrollPane(jsonViewerPanel);
        JSONPanel.add(sp, BorderLayout.CENTER);

        bodyFormPanel = new JPanel();
        //JScrollPane formScroller = new JScrollPane();
        //formPanel.setLayout(new BoxLayout(formPanel,BoxLayout.PAGE_AXIS));

        bodyFormPanel.add(new Form(bodyFormPanel));

        JPanel noBodyPanel = new JPanel();

        bodyPanel.add(noBodyPanel);

        bodyTabStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                middlePanel.updateUI();
            }
        });
        //////////////////////////////////////////
    }

    private void makeHeaderPanel() {
        /////////////////////making header panel

        headerPanel.add(new Form(headerPanel));
        /////////////////////////////////////////////////////
    }


    public JPanel getMiddlePanel() {
        return middlePanel;
    }

    private class handler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: needs a list of forms maybe
        }
    }

    class Form extends JPanel {
        JPanel owner;
        JTextField nameField, valueField;
        JCheckBox isActive;
        JButton removeForm;
        Form form;

        public Form(JPanel owner) {
            this.owner = owner;
            nameField = new JTextField("Name");
            //nameField.setMinimumSize(new Dimension(FRAME_WIDTH / 8 - 50, 25));
            nameField.setPreferredSize(new Dimension(FRAME_WIDTH / 8+50, 25));
            nameField.setMaximumSize(new Dimension(FRAME_WIDTH / 8 + 100, 25));
            valueField = new JTextField("Value");
            //valueField.setMinimumSize(new Dimension(FRAME_WIDTH / 8 - 50, 25));
            valueField.setPreferredSize(new Dimension(FRAME_WIDTH / 8+50, 25));
            valueField.setMaximumSize(new Dimension(FRAME_WIDTH / 8 + 100, 25));
            isActive = new JCheckBox();
            isActive.doClick();
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

            form = this;
        }

        class handler implements FocusListener, ActionListener {

            @Override
            public void focusGained(FocusEvent e) {
                if (e.getSource() == nameField || e.getSource() == valueField) {

                    owner.add(new Form(owner));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("Focus lost");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == isActive) {
                    if (isActive.isSelected()) {
                        form.setBorder(BorderFactory.createRaisedBevelBorder());
                    } else {
                        form.setBorder(BorderFactory.createLoweredBevelBorder());
                    }
                } else if (e.getSource() == removeForm) {
                    int a = JOptionPane.showConfirmDialog(form, "Are you sure?");
                    if (a == JOptionPane.YES_OPTION) {
                        owner.remove(form);
                    }
                }
            }
        }
    }


}

