package GUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * left panel
 * this panel is divided by 2 border Layout
 * north part is for the insomnia button
 * center part is for another panel that is also divided by border layout , its north part is for the filter and add
 * and its center part is for the folders and requests
 */

public class RequestsPanel extends JPanel {
    private JButton insomniaButton;
    private ArrayList<JButton> requestsButton;
    private JTextField filterField;
    private JButton addFolderORRequest;

    //to see what panel is for folder button
    private HashMap<JButton, JPanel> foldersPanel;
    JPanel centerCenterRequestPanel;

    //the last right-clicked folder
    JPanel folderPanelToAddComponent;
    //height of each request
    private int folderPanelToAddComponentHeight = 40;


    public RequestsPanel() {
        requestsButton = new ArrayList<>();
        foldersPanel = new HashMap<>();
        setLayout(new BorderLayout());
        JPanel northRequestPanel = new JPanel();
        JPanel centerRequestPanel = new JPanel(new BorderLayout());
        JPanel northCenterRequestPanel = new JPanel();
        centerCenterRequestPanel = new JPanel();
        centerCenterRequestPanel.setLayout(new BoxLayout(centerCenterRequestPanel, BoxLayout.PAGE_AXIS));

        insomniaButton = new JButton("Insomnia "+'\u23F7');
        insomniaButton.setPreferredSize(new Dimension(200, 30));
        insomniaButton.addActionListener(new handler());
        addFolderORRequest = new JButton("+");
        addFolderORRequest.setPreferredSize(new Dimension(50, 25));

        filterField = new JTextField("Filter");
        filterField.setPreferredSize(new Dimension(125, 25));

        northRequestPanel.add(insomniaButton);
        northCenterRequestPanel.add(filterField);
        northCenterRequestPanel.add(addFolderORRequest);

        add(northRequestPanel, BorderLayout.NORTH);
        centerRequestPanel.add(northCenterRequestPanel, BorderLayout.NORTH);
        centerRequestPanel.add(centerCenterRequestPanel, BorderLayout.CENTER);
        add(centerRequestPanel, BorderLayout.CENTER);

        addFolderORRequest.addActionListener(new handler());
    }

    /**
     * get a request and add it to the main panel
     * @param panel request to add
     */
    private void addRequest(JPanel panel) {

        panel.setMaximumSize(new Dimension(this.getSize().width, 40));
        panel.setSize(new Dimension(this.getSize().width, 40));

        centerCenterRequestPanel.add(panel);
        updateUI();
    }

    /**
     * makes a request
     * @param name name of request
     * @param method method of request
     * @return the made request
     */
    private JPanel makeRequest(String name, String method) {
        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel,BoxLayout.LINE_AXIS));
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
        panel.add(label);
        panel.add(requestButton);
        panel.setVisible(true);
        updateUI();
        requestsButton.add(requestButton);
        return panel;
    }

    /**
     * make a folder
     * @param name folder's name
     */
    private void addFolder(String name) {
        JButton newFolder = new JButton(name);
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(newFolder);
        panel.setMaximumSize(new Dimension(this.getSize().width, 40));
        panel.setSize(new Dimension(this.getSize().width, 40));


        centerCenterRequestPanel.add(panel);
        updateUI();
        foldersPanel.put(newFolder, panel);
        JPopupMenu menu = new JPopupMenu("Creat");
        JMenuItem creat = new JMenuItem("Creat Request");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem close = new JMenuItem("Close");
        menu.add(open);
        menu.add(close);
        menu.add(creat);
        /**
         * telling the gui what to do if the user chooses this item
         * first make a dialog and give the entered info to creat request
         */
        creat.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
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
                        folderPanelToAddComponent.setMaximumSize(new Dimension(
                                folderPanelToAddComponent.getWidth(), folderPanelToAddComponentHeight += 40
                        ));
                        folderPanelToAddComponent.setSize(new Dimension(
                                folderPanelToAddComponent.getWidth(), folderPanelToAddComponentHeight += 40
                        ));
                        folderPanelToAddComponent.
                                add(makeRequest(nameFiled.getText(), ((String) cb.getSelectedItem()).substring(0, 3)));
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
            }
        });
        /**
         * telling the gui what to do if the user chooses this item
         * it makes all the panels(requests) visible
         */
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 1; i < folderPanelToAddComponent.getComponentCount(); i++) {

                    folderPanelToAddComponent.getComponent(i).setVisible(true);
                    folderPanelToAddComponent.setMaximumSize(new Dimension(
                            folderPanelToAddComponent.getWidth(), folderPanelToAddComponentHeight
                    ));
                    folderPanelToAddComponent.setSize(new Dimension(
                            folderPanelToAddComponent.getWidth(), folderPanelToAddComponentHeight
                    ));
                }
                updateUI();
            }
        });
        /**
         * telling the gui what to do if the user chooses this item
         * it makes all the panels(requests) invisible
         */
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 1; i < folderPanelToAddComponent.getComponentCount(); i++) {
                    folderPanelToAddComponent.getComponent(i).setVisible(false);
                    folderPanelToAddComponent.setMaximumSize(new Dimension(
                            folderPanelToAddComponent.getWidth(), 40
                    ));
                    folderPanelToAddComponent.setSize(new Dimension(
                            folderPanelToAddComponent.getWidth(), 40
                    ));
                }
                updateUI();
            }
        });
        /**
         * helps us to show the popup if the user click right
         */
        newFolder.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param ev
             */
            @Override
            public void mousePressed(MouseEvent ev) {
                folderPanelToAddComponent = foldersPanel.get(ev.getComponent());
                if (ev.isPopupTrigger()) {
                    menu.show(ev.getComponent(), ev.getX(), ev.getY());
                }
            }

            /**
             * {@inheritDoc}
             *
             * @param ev
             */
            @Override
            public void mouseReleased(MouseEvent ev) {
                folderPanelToAddComponent = foldersPanel.get(ev.getComponent());
                if (ev.isPopupTrigger()) {
                    menu.show(ev.getComponent(), ev.getX(), ev.getY());
                }
            }
        });
    }

    /**
     * handling the main component of the requests panel
     */
    class handler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            /**
             * if the add button is pressed
             * it means the usesr needs a new request or folder
             * make a popup to choose if the user wants to make new folder or request
             * making a dialog based on the selected item
             * passing the entered info to its related method
             */
            if (e.getSource() == addFolderORRequest) {
                JPopupMenu popupMenu = new JPopupMenu("Insomnia");
                popupMenu.setVisible(true);
                JMenuItem newRequest = new JMenuItem("New Request");
                newRequest.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
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
                                addRequest(makeRequest(nameFiled.getText(), ((String) cb.getSelectedItem()).substring(0, 3)));
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
                    }
                });
                JMenuItem newFolder = new JMenuItem("New Folder");
                newFolder.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JDialog newFolderDialog = new JDialog();
                        newFolderDialog.setVisible(true);
                        JTextField nameFiled = new JTextField("My Folder");
                        nameFiled.setBorder(new TitledBorder("Name"));

                        JButton creatFolderButton = new JButton("Creat");
                        creatFolderButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                addFolder('\u269B'+"      "+nameFiled.getText()+"      "+'\u269B');
                                newFolderDialog.setVisible(false);
                                newFolderDialog.dispose();
                            }
                        });

                        newFolderDialog.setLayout(new BorderLayout());
                        newFolderDialog.add(nameFiled, BorderLayout.CENTER);
                        newFolderDialog.add(creatFolderButton, BorderLayout.EAST);
                        newFolderDialog.setTitle("New Folder");
                        newFolderDialog.setBounds(300, 200, 500, 80);

                        newFolderDialog.setVisible(true);
                    }
                });
                popupMenu.add(newRequest);
                popupMenu.add(newFolder);
                popupMenu.show(addFolderORRequest, filterField.getBounds().x, filterField.getBounds().y);

            }
            /**
             * if the insomnia button is pressed
             * it means the user needs to make a new workspace or change to another one
             */
            else if (e.getSource() == insomniaButton) {
                JPopupMenu popupMenu = new JPopupMenu("Insomnia");
                popupMenu.setVisible(true);
                JMenuItem creatWorkSpace = new JMenuItem("Creat Workspace");
                creatWorkSpace.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Display.addWorkSpace();
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
                popupMenu.show(insomniaButton, insomniaButton.getBounds().x, (insomniaButton.getBounds().y
                        + insomniaButton.getBounds().height));
            }
        }
    }
}
