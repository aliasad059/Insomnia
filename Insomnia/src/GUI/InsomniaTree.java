package GUI;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.HashMap;

public class InsomniaTree extends JTree {
    DefaultMutableTreeNode requests;
//    ArrayList<DefaultMutableTreeNode> nodes;
    HashMap<DefaultMutableTreeNode,ArrayList<DefaultMutableTreeNode>> nodes;

    /**
     * Returns a <code>JTree</code> with a sample model.
     * The default model used by the tree defines a leaf node as any node
     * without children.
     *
     * @see DefaultTreeModel#asksAllowsChildren
     */
    public InsomniaTree() {
        requests = new DefaultMutableTreeNode("Requests");
        nodes = new HashMap<>();
    }
    public void addFolder(String folderName){
        DefaultMutableTreeNode folder = new DefaultMutableTreeNode(folderName);

    }
    public void addRequest(String name , String method){
    }
}
