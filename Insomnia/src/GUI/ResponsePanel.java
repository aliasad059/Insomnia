package GUI;

import com.github.jutil.json.gui.JsonViewerPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static GUI.InsomniaFrame.FRAME_WIDTH;

/**
 * the right panel of the insomnia panel
 */
public class ResponsePanel extends JPanel {
    JPanel northResponsePanel,
            messageBodyTab, headerTab,
            rowPanel, JSONPanel;
    JLabel statusLabel, timeLabel, sizeLabel;
    DefaultTableModel tableModel;
    JTabbedPane tabs;
    JTextPane rowTextPane, JSONTextPane;
    String[] cmStrings = {"Row", "JSON"};
    String[] tableColumnsString = {"NAME", "VALUE"};
    //ArrayList<ArrayList<String>> tableNameValue = new ArrayList<ArrayList<String>>();
    JComboBox cb;
    JTable headerTable;
    JButton copyCB;

    public ResponsePanel() {

        setLayout(new BorderLayout());

        northResponsePanel = new JPanel(new GridLayout(1, 3));

        statusLabel = new JLabel("ERROR", JLabel.CENTER);
        statusLabel.setAlignmentX(0);
        statusLabel.setBorder(new LineBorder(Color.GRAY));
        statusLabel.setPreferredSize(new Dimension(FRAME_WIDTH / 20, 20));
        timeLabel = new JLabel("0 ms", JLabel.CENTER);
        timeLabel.setPreferredSize(new Dimension(FRAME_WIDTH / 20, 20));
        timeLabel.setBorder(new LineBorder(Color.GRAY));
        sizeLabel = new JLabel("0 kB", JLabel.CENTER);
        sizeLabel.setPreferredSize(new Dimension(FRAME_WIDTH / 20, 20));
        sizeLabel.setBorder(new LineBorder(Color.GRAY));

        copyCB = new JButton("Copy To Clipboard");
        northResponsePanel.add(statusLabel);
        northResponsePanel.add(timeLabel);
        northResponsePanel.add(sizeLabel);

        add(northResponsePanel, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        messageBodyTab = new JPanel(new BorderLayout());
        headerTab = new JPanel(new BorderLayout());
        tabs.addTab("Message Body", messageBodyTab);
        tabs.addTab("Header", headerTab);
        add(tabs);

        rowPanel = new JPanel(new BorderLayout());
        rowTextPane = new JTextPane();
        rowTextPane.setEditable(false);
        rowPanel.add(new JScrollPane(rowTextPane), BorderLayout.CENTER);
        JSONPanel = new JPanel(new BorderLayout());
        JsonViewerPanel jsonViewerPanel = new JsonViewerPanel();
        JSONPanel.add(jsonViewerPanel, BorderLayout.CENTER);

        cb = new JComboBox(cmStrings);

        messageBodyTab.add(cb, BorderLayout.NORTH);
        messageBodyTab.add(rowPanel, BorderLayout.CENTER);

        cb.addActionListener(new handler());

        headerTable = new JTable( tableModel = new DefaultTableModel());
        headerTable.setDefaultEditor(Object.class, null);
        JScrollPane sp = new JScrollPane(headerTable);
        headerTab.add(sp, BorderLayout.CENTER);
        headerTab.add(copyCB, BorderLayout.NORTH);

        copyCB.addActionListener(new handler());
    }

    /**
     * handling the main events of the response panel
     */
    class handler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            /**
             * changing the status as user changes the combobox selected item
             */
            if (e.getSource() == cb) {
                JComboBox comboBox = (JComboBox) e.getSource();
                if (comboBox.getSelectedIndex() == 0) {
                    messageBodyTab.remove(1);
                    messageBodyTab.add(rowPanel, BorderLayout.CENTER);
                    updateUI();
                } else {
                    messageBodyTab.remove(1);
                    messageBodyTab.add(JSONPanel, BorderLayout.CENTER);
                    updateUI();
                }
            }
            /**
             * coping the table to the clipboard
             */
            if (e.getSource() == copyCB) {
                String string = "KEYS\tVALUES";
                string += '\n';
                for (int i = 0; i < headerTable.getRowCount(); i++) {
                    for (int j = 0; j < 2; j++) {
                        string += headerTable.getValueAt(i,j);
                        string += '\t';
                    }
                    string += '\n';
                }
                StringSelection stringSelection = new StringSelection(string);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        }
    }

    public void setSizeLabel(String size) {
        this.sizeLabel.setText(size);
    }

    public void setStatusLabel(String status) {
        this.statusLabel.setText(status);
    }

    public void setTimeLabel(String time) {
        this.timeLabel.setText(time);
    }

    public void setHeaders(Map<String, List<String>> headers) {
        headerTable.removeAll();
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"KEY","VALUE"},0);
        headers.forEach((k, v) -> tableModel.addRow( new String[]{k, v.toString()}));
        headerTable.setModel(tableModel);
    }

    public void setJSONBodyText(String bodyText) {
    }

    public void setRowBodyText(String bodyText) {
        rowTextPane.setText(bodyText);
    }
}
