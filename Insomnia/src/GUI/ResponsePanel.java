package GUI;

import HttpClient.Response;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static GUI.InsomniaFrame.FRAME_WIDTH;

/**
 * the right panel of the insomnia panel
 */
public class ResponsePanel extends JPanel {
    JPanel northResponsePanel,
            messageBodyTab, headerTab,
            rowPanel, JSONPanel, previewPanel;
    JLabel statusLabel, timeLabel, sizeLabel;
    JsonViewerPanel jsonViewerPanel;
    DefaultTableModel tableModel;
    JTabbedPane tabs;
    JTextPane previewTextPane, JSONTextPane;
    JTextArea rowTextArea;
    String[] cmStrings = {"Row", "Preview", "JSON"};
    String[] tableColumnsString = {"NAME", "VALUE"};
    //ArrayList<ArrayList<String>> tableNameValue = new ArrayList<ArrayList<String>>();
    JComboBox cb;
    JTable headerTable;
    JButton copyCB;
    /**
     * creat a response panel
     */
    public ResponsePanel() {
        setResponsePanel();
    }

    /**
     * creat a response panel as Response object
     * @param response
     */
    public ResponsePanel(Response response) {
        setResponsePanel();
        updatePanel(response);
    }

    /**
     * sets the changes to the panel
     */
    private void setResponsePanel() {
        setLayout(new BorderLayout());

        northResponsePanel = new JPanel(new GridLayout(1, 3));

        statusLabel = new JLabel("Error", JLabel.CENTER);
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

        previewPanel = new JPanel(new BorderLayout());
        previewTextPane = new JTextPane();
        previewTextPane.setEditable(false);
        previewPanel.add(new JScrollPane(previewTextPane), BorderLayout.CENTER);

        rowPanel = new JPanel(new BorderLayout());
        rowTextArea = new JTextArea();
        rowTextArea.setEditable(false);
        rowPanel.add(new JScrollPane(rowTextArea), BorderLayout.CENTER);

        JSONPanel = new JPanel(new BorderLayout());
        jsonViewerPanel = new JsonViewerPanel();
        jsonViewerPanel.setEditable(false);
        JSONPanel.add(jsonViewerPanel, BorderLayout.CENTER);

        cb = new JComboBox(cmStrings);

        messageBodyTab.add(cb, BorderLayout.NORTH);
        messageBodyTab.add(rowPanel, BorderLayout.CENTER);

        cb.addActionListener(new handler());

        headerTable = new JTable(tableModel = new DefaultTableModel());
        headerTable.setDefaultEditor(Object.class, null);
        JScrollPane sp = new JScrollPane(headerTable);
        headerTab.add(sp, BorderLayout.CENTER);
        headerTab.add(copyCB, BorderLayout.NORTH);

        copyCB.addActionListener(new handler());
    }

    /**
     * handling the main events of the response panel
     */
    public class handler implements ActionListener {

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
                } else if (comboBox.getSelectedIndex() == 1) {
                    messageBodyTab.remove(1);
                    messageBodyTab.add(previewPanel, BorderLayout.CENTER);
                } else {
                    messageBodyTab.remove(1);
                    messageBodyTab.add(JSONPanel, BorderLayout.CENTER);
                }
                updateUI();
            }
            /**
             * coping the table to the clipboard
             */
            if (e.getSource() == copyCB) {
                String string = "KEYS\tVALUES";
                string += '\n';
                for (int i = 0; i < headerTable.getRowCount(); i++) {
                    for (int j = 0; j < 2; j++) {
                        string += headerTable.getValueAt(i, j);
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

    /**
     * set size label
     * @param size size
     */
    public void setSizeLabel(String size) {
        this.sizeLabel.setText(size);
    }
    /**
     * set status label
     * @param status status
     */
    public void setStatusLabel(JLabel status) {
        status.setAlignmentX(0);
        status.setBorder(new LineBorder(Color.GRAY));
        status.setPreferredSize(new Dimension(FRAME_WIDTH / 20, 20));
        northResponsePanel.remove(0);
        northResponsePanel.add(status, 0);
        this.statusLabel = status;
        this.updateUI();
    }
    /**
     * set time label
     * @param time time
     */
    public void setTimeLabel(String time) {
        this.timeLabel.setText(time);
    }

    /**
     * set headers map
     * @param headers headers
     */
    public void setHeaders(Map<String, List<String>> headers) {
        headerTable.removeAll();
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"KEY", "VALUE"}, 0);
        headers.forEach((k, v) -> tableModel.addRow(new String[]{k, v.toString()}));
        headerTable.setModel(tableModel);
    }
    /**
     * setJSONBodyContent
     * @param bodyText bodyText
     */
    public void setJSONBodyContent(String bodyText) {
        jsonViewerPanel.setText(bodyText);
    }
    /**
     * setRowBodyContent
     * @param bodyText bodyText
     */
    public void setRowBodyContent(String bodyText) {
        rowTextArea.setText(bodyText);
    }
    /**
     * setPreviewContent
     * @param image image
     */
    public void setPreviewContent(Image image) {
        previewTextPane.insertIcon(new ImageIcon(image));
    }

    /**
     * reset panel
     */
    public void resetPanel() {
        this.removeAll();
        setResponsePanel();
    }

    /**
     * update panel as Response object
     * @param response response
     */
    public void updatePanel(Response response) {
        if (response != null) {
            setTimeLabel(response.getResponseTime());
            setSizeLabel(response.getResponseSize());
            setStatusLabel(response.getStatusLabel());

            if (response.doesContainPic()) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getBody());
                try {
                    Image image = ImageIO.read(byteArrayInputStream);
                    setPreviewContent(image);
                } catch (IOException ignored) {
                }
            }
            if (response.isJson()) {
                setJSONBodyContent(new String(response.getBody()));
            }
            setRowBodyContent(new String(response.getBody()));
            setHeaders(response.getHeaders());
            revalidate();
        }
    }
}
