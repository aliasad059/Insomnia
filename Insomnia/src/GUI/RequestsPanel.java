package GUI;

import org.omg.CORBA.CODESET_INCOMPATIBLE;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.Highlighter;
import javax.swing.text.IconView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static GUI.InsomniaFrame.FRAME_WIDTH;


public class RequestsPanel extends JPanel {
    private GroupLayout requestsPanelLayout;
    private JButton addNewRequestButton, insomniaButton;
    private GroupLayout.ParallelGroup parallel;
    private GroupLayout.SequentialGroup sequential;
    private ArrayList<JButton> requestsButton;
    private JTextField filterField;


    public RequestsPanel() {
        requestsButton = new ArrayList<>();
        setLayout(new BorderLayout());
        JPanel northRequestPanel = new JPanel();
        JPanel centerRequestPanel = new JPanel(new BorderLayout());
        JPanel northCenterRequestPanel = new JPanel();
        JPanel centerCenterRequestPanel = new JPanel();

        requestsPanelLayout = new GroupLayout(centerCenterRequestPanel);
        centerCenterRequestPanel.setLayout(requestsPanelLayout);
        centerCenterRequestPanel.setBorder(new TitledBorder("Requests"));
        requestsPanelLayout.setAutoCreateGaps(true);
        requestsPanelLayout.setAutoCreateContainerGaps(true);

        insomniaButton = new JButton("Insomnia");
        insomniaButton.setPreferredSize(new Dimension(200, 30));
        insomniaButton.addActionListener(new handler());
        addNewRequestButton = new JButton("+");
        addNewRequestButton.setPreferredSize(new Dimension(50, 25));
        filterField = new JTextField("");
        filterField.setPreferredSize(new Dimension(150, 25));
        insomniaButton.setBackground(Color.MAGENTA);

        northRequestPanel.add(insomniaButton);
        northCenterRequestPanel.add(filterField);
        northCenterRequestPanel.add(addNewRequestButton);

        add(northRequestPanel, BorderLayout.NORTH);
        centerRequestPanel.add(northCenterRequestPanel, BorderLayout.NORTH);
        centerRequestPanel.add(centerCenterRequestPanel, BorderLayout.CENTER);
        add(centerRequestPanel, BorderLayout.CENTER);

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
        if (method.substring(0, 3).equals("GET")) {
            label.setForeground(Color.MAGENTA);
        }
        if (method.substring(0, 3).equals("POS")) {
            label.setForeground(Color.GREEN);
        }
        if (method.substring(0, 3).equals("PUT")) {
            label.setForeground(Color.YELLOW);
        }
        if (method.substring(0, 3).equals("PAT")) {
            label.setForeground(Color.ORANGE);
        }
        if (method.substring(0, 3).equals("DEL")) {
            label.setForeground(Color.RED);
        }
        if (method.substring(0, 3).equals("OPT")) {
            label.setForeground(Color.BLUE);
        }
        if (method.substring(0, 3).equals("HEA")) {
            label.setForeground(Color.CYAN);
        }

        JButton requestButton = new JButton(name);
        label.setLabelFor(requestButton);
        parallel.addGroup(requestsPanelLayout.createSequentialGroup().
                addComponent(label).addComponent(requestButton));
        sequential.addGroup(requestsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(label).addComponent(requestButton));
        updateUI();
        requestsButton.add(requestButton);
    }

    class handler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addNewRequestButton) {
                JDialog newRequestDialog = new JDialog();
                newRequestDialog.setVisible(true);
                JTextField nameFiled = new JTextField("My Request");
                nameFiled.setBorder(new TitledBorder("Name"));
                String[] methodsName = {"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"};
                JComboBox cb = new JComboBox(methodsName);
                cb.setSelectedIndex(0);

                JButton createRequestButton = new JButton("Creat");
                createRequestButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addRequest(nameFiled.getText(), ((String) cb.getSelectedItem()).substring(0, 3));
                        newRequestDialog.setVisible(false);
                        newRequestDialog.dispose();
                    }
                });

                newRequestDialog.setLayout(new BorderLayout());
                newRequestDialog.add(nameFiled, BorderLayout.CENTER);
                newRequestDialog.add(cb, BorderLayout.EAST);
                newRequestDialog.add(createRequestButton, BorderLayout.SOUTH);
                newRequestDialog.setTitle("New Request");
                newRequestDialog.setBounds(300, 200, 500, 125);

                newRequestDialog.setVisible(true);
            } else if (e.getSource() == insomniaButton) {
                JPopupMenu popupMenu = new JPopupMenu("Insomnia");
                popupMenu.setVisible(true);
                JMenuItem creatWorkSpace = new JMenuItem("Creat Workspace");
                creatWorkSpace.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Display.addWordSpace();
                    }
                });
                JMenuItem changeWorkSpace = new JMenuItem("Change Workspace");
                changeWorkSpace.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Display.changeWorkSpace();
                    }
                });
                popupMenu.add(changeWorkSpace);
                popupMenu.add(creatWorkSpace);
                popupMenu.show(insomniaButton, insomniaButton.getBounds().x, (int) (insomniaButton.getBounds().getY()
                        + insomniaButton.getBounds().height));
            }
        }
    }
}
