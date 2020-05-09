package GUI;


import com.github.weisj.darklaf.DarkLaf;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;


public class Display {

    private static ArrayList<JFrame> workSpaces;
    private static JFrame showingFrame;

    public Display() {
        workSpaces = new ArrayList<>();
        makeWorkSpace("Insomnia");
        showingFrame = workSpaces.get(0);
        showingFrame.setVisible(true);
    }

    private static void makeWorkSpace(String name) {
        JFrame frame = new InsomniaFrame(name);
        workSpaces.add(frame);
    }

    public static void addWordSpace() {
        String name = JOptionPane.showInputDialog("Enter WorkSpace Name");
        makeWorkSpace(name);
    }

    public static void changeWorkSpace() {
        JDialog dialog = new JDialog();
        ButtonGroup buttonGroup = new ButtonGroup();
        ArrayList<JRadioButton> radioButtons = new ArrayList<>();
        for (int i = 0; i < workSpaces.size(); i++) {
            JRadioButton jrb = new JRadioButton(workSpaces.get(i).getName());
            buttonGroup.add(jrb);
            radioButtons.add(jrb);
            dialog.add(jrb);
        }
        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < radioButtons.size(); i++) {
                    if (radioButtons.get(i).isSelected()){
                        showingFrame.setVisible(false);
                        showingFrame = workSpaces.get(i);
                        showingFrame.setVisible(true);
                        break;
                    }
                }
                dialog.dispose();
            }
        });
        dialog.add(OKButton);
        dialog.setSize(new Dimension(500, 100));
        dialog.setVisible(true);
    }

    public static JFrame getFrame() {
        return showingFrame;
    }

}
