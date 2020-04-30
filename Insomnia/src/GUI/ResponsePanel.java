package GUI;

import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static GUI.Display.FRAME_WIDTH;

public class ResponsePanel {
    JPanel responsePanel, northResponsePanel,
            messageBodyTab, headerTab,
            rowPanel, JSONPanel;
    JLabel statusLabel, timeLabel, sizeLabel;
    JTabbedPane tabs;
    JTextPane rowTextPane, JSONTextPane;
    String[] cmStrings = {"Row", "JSON"};
    JComboBox cb;
    JTable headerTable;
    JButton copyCB;

    public ResponsePanel() {

        responsePanel = new JPanel();
        responsePanel.setLayout(new BorderLayout());

        northResponsePanel = new JPanel();

        statusLabel = new JLabel("Error",JLabel.CENTER);
        statusLabel.setPreferredSize(new Dimension(FRAME_WIDTH/20,20));
        timeLabel = new JLabel("0 ms",JLabel.CENTER);
        timeLabel.setPreferredSize(new Dimension(FRAME_WIDTH/20,20));
        sizeLabel = new JLabel("0 kB",JLabel.CENTER);
        sizeLabel.setPreferredSize(new Dimension(FRAME_WIDTH/20,20));

        copyCB = new JButton("Copy TO Clipboard");
        northResponsePanel.add(statusLabel);
        northResponsePanel.add(timeLabel);
        northResponsePanel.add(sizeLabel);
        northResponsePanel.add(copyCB);

        responsePanel.add(northResponsePanel, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        messageBodyTab = new JPanel(new BorderLayout());
        headerTab = new JPanel(new BorderLayout());
        tabs.addTab("Message Body",messageBodyTab);
        tabs.addTab("Header",headerTab);
        responsePanel.add(tabs);

        rowPanel = new JPanel();
        rowTextPane = new JTextPane();
        rowPanel.add(rowTextPane);
        JSONPanel = new JPanel();
        JSONTextPane = new JTextPane();
        JSONPanel.add(JSONTextPane);

        cb = new JComboBox(cmStrings);

        messageBodyTab.add(cb, BorderLayout.NORTH);
        messageBodyTab.add(rowPanel, BorderLayout.CENTER);

        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                if (comboBox.getSelectedIndex() == 0) {
                    messageBodyTab.remove(1);
                    messageBodyTab.add(rowPanel, BorderLayout.CENTER);
                } else {
                    messageBodyTab.remove(1);
                    messageBodyTab.add(JSONPanel,BorderLayout.CENTER);
                }
            }
        });

        String[] columns = {"NAME","VALUE"};
        String[][] NAME_VALUE = {{"dkdk","djdjd"},{"dkdk","djdjd"}};
        headerTable = new JTable(NAME_VALUE,columns);
        headerTable.setDefaultEditor(Object.class, null);
        JScrollPane sp=new JScrollPane(headerTable);
        headerTab.add(sp,BorderLayout.CENTER);

    }

    public JPanel getResponsePanel() {
        return responsePanel;
    }
}
