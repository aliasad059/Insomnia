package GUI;

import com.github.jutil.json.gui.JsonViewerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
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
    String[] tableColumnsString = {"NAME", "VALUE"};
    String[][] table_NAME_VALUE = {{"dkdk", "djdjd"}, {"dkdk", "djdjd"}};
    //ArrayList<ArrayList<String>> tableNameValue = new ArrayList<ArrayList<String>>();
    JComboBox cb;
    JTable headerTable;
    JButton copyCB;

    public ResponsePanel() {

        responsePanel = new JPanel();
        responsePanel.setLayout(new BorderLayout());

        northResponsePanel = new JPanel(new FlowLayout());

        statusLabel = new JLabel("ERROR", JLabel.CENTER);
        statusLabel.setPreferredSize(new Dimension(FRAME_WIDTH / 20, 10));
        timeLabel = new JLabel("0 ms", JLabel.CENTER);
        timeLabel.setPreferredSize(new Dimension(FRAME_WIDTH / 20, 10));
        sizeLabel = new JLabel("0 kB", JLabel.CENTER);
        sizeLabel.setPreferredSize(new Dimension(FRAME_WIDTH / 20, 10));

        copyCB = new JButton("Copy TO Clipboard");
        northResponsePanel.add(statusLabel);
        northResponsePanel.add(timeLabel);
        northResponsePanel.add(sizeLabel);
        //northResponsePanel.add(copyCB);

        responsePanel.add(northResponsePanel, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        messageBodyTab = new JPanel(new BorderLayout());
        headerTab = new JPanel(new BorderLayout());
        tabs.addTab("Message Body", messageBodyTab);
        tabs.addTab("Header", headerTab);
        responsePanel.add(tabs);

        rowPanel = new JPanel(new BorderLayout());
        rowTextPane = new JTextPane();
        rowPanel.add(rowTextPane, BorderLayout.CENTER);
        JSONPanel = new JPanel(new BorderLayout());
        JsonViewerPanel jsonViewerPanel = new JsonViewerPanel();
        JSONPanel.add(jsonViewerPanel, BorderLayout.CENTER);

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
                    responsePanel.updateUI();
                } else {
                    messageBodyTab.remove(1);
                    messageBodyTab.add(JSONPanel, BorderLayout.CENTER);
                    responsePanel.updateUI();
                }
            }
        });


        headerTable = new JTable(table_NAME_VALUE, tableColumnsString);
        headerTable.setDefaultEditor(Object.class, null);
        JScrollPane sp = new JScrollPane(headerTable);
        headerTab.add(sp, BorderLayout.CENTER);
        headerTab.add(copyCB, BorderLayout.NORTH);

        copyCB.addActionListener(new handler());

    }

    public JPanel getResponsePanel() {
        return responsePanel;
    }

    class handler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == copyCB) {
                String string = "";
                for (int i = 0; i < tableColumnsString.length; i++) {
                    string += tableColumnsString[i];
                    string+= '\t';
                }
                string+= '\n';
                for (int i = 0; i < table_NAME_VALUE.length; i++) {
                    for (int j = 0; j < table_NAME_VALUE[i].length; j++) {
                        string+=table_NAME_VALUE[i][j];
                        string+= '\t';
                    }
                    string+= '\n';
                }
                StringSelection stringSelection = new StringSelection(string);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        }
    }
}
