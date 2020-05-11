package GUI;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * it is not still completely implemented
 */
public class InsomniaTree extends JTree {
    DefaultMutableTreeNode root;
    ArrayList<DefaultMutableTreeNode> nodes;
//    HashMap<DefaultMutableTreeNode,ArrayList<DefaultMutableTreeNode>> nodes;

    /**
     * Returns a <code>JTree</code> with a sample model.
     * The default model used by the tree defines a leaf node as any node
     * without children.
     *
     * @see DefaultTreeModel#asksAllowsChildren
     */
    public InsomniaTree() {

        addTreeSelectionListener(new handler());
        addMouseListener(new handler());
        root = new DefaultMutableTreeNode("Requests");
        nodes = new ArrayList<>();
    }

    public void addFolder(String folderName) {
        DefaultMutableTreeNode folder = new DefaultMutableTreeNode(folderName);
        folder.add(makeRequestNode("My Request","GET"));
        root.add(folder);
    }

    public DefaultMutableTreeNode makeRequestNode(String name, String method) {
        DefaultMutableTreeNode newRequestNode = new DefaultMutableTreeNode(method+"   "+name);
        return newRequestNode;
    }

    class handler extends MouseAdapter implements TreeSelectionListener {
        /**
         * {@inheritDoc}
         *
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
        }

        /**
         * {@inheritDoc}
         *
         * @param e
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
        }

        /**
         * Called whenever the value of the selection changes.
         *
         * @param e the event that characterizes the change.
         */
        @Override
        public void valueChanged(TreeSelectionEvent e) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) InsomniaTree.this.getLastSelectedPathComponent();

            if (node != null && node.getChildCount() == 0){
                //TODO: change the panel as nodes panel
            }
        }
    }
}
