package GUI;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * this class relate other GUI class to each other
 */
public class Display {

    private static ArrayList<InsomniaFrame> workSpaces;
    private static InsomniaFrame showingFrame;

    public Display() {
        workSpaces = new ArrayList<>();
        makeWorkSpace("Insomnia");
        showingFrame.setVisible(true);
    }
    public Display(ArrayList<InsomniaFrame> workSpaces,InsomniaFrame showingFrame){
        workSpaces = new ArrayList<>();
        Display.setWorkSpaces(workSpaces);
        Display.setShowingFrame(showingFrame);
        showingFrame.setVisible(true);
    }

    /**
     * makes a new work space
     * @param name
     */
    private static void makeWorkSpace(String name) {
        new File("./save/workSpaces/"+name).mkdirs();
        InsomniaFrame frame = new InsomniaFrame(name);
        workSpaces.add(frame);
        if (showingFrame != null) {
            showingFrame.setVisible(false);
        }
        showingFrame = frame;
        showingFrame.setVisible(true);
    }

    /**
     * calls make workspace if the entered name is correct
     */
    public static void addWorkSpace() {
        String name = JOptionPane.showInputDialog("Enter WorkSpace Name");
        try {
            if (!name.equals(""))
                makeWorkSpace(name);
        } catch (NullPointerException e) {
        }
    }

    /**
     * change current workspace to another
     * means makes current frame invisible and makes another visible
     */
    public static void changeWorkSpace() {
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        JPanel rbPanel = new JPanel(new GridLayout(5, 1));
        rbPanel.setBorder(new TitledBorder("Workspaces"));
        ButtonGroup buttonGroup = new ButtonGroup();
        ArrayList<JRadioButton> radioButtons = new ArrayList<>();
        for (int i = 0; i < workSpaces.size(); i++) {
            JRadioButton jrb = new JRadioButton(workSpaces.get(i).getTitle());
            buttonGroup.add(jrb);
            radioButtons.add(jrb);
            rbPanel.add(jrb);
        }
        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < radioButtons.size(); i++) {
                    if (radioButtons.get(i).isSelected()) {
                        showingFrame.setVisible(false);
                        showingFrame = workSpaces.get(i);
                        showingFrame.setVisible(true);
                        break;
                    }
                }
                dialog.dispose();
            }
        });
        dialog.add(rbPanel, BorderLayout.CENTER);
        dialog.add(OKButton, BorderLayout.SOUTH);
        dialog.setSize(new Dimension(400, 300));
        dialog.setVisible(true);
    }

    public static ArrayList<InsomniaFrame> getWorkSpaces() {
        return workSpaces;
    }
    public static JFrame getShowingFrame(){
        return showingFrame;
    }

    public static void setShowingFrame(InsomniaFrame showingFrame) {
        Display.showingFrame = showingFrame;
    }

    public static void setWorkSpaces(ArrayList<InsomniaFrame> workSpaces) {
        Display.workSpaces = workSpaces;
    }
}