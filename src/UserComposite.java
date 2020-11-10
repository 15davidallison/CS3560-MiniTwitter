import javax.swing.tree.*;
public interface UserComposite {
	public String getName();
	public String getGroup();
	public DefaultMutableTreeNode getNode();
	public void setNode(DefaultMutableTreeNode n);
	public boolean isLeaf();
}