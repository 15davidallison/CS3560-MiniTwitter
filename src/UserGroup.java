import java.util.HashSet;
import javax.swing.tree.DefaultMutableTreeNode;


public class UserGroup implements SysEntry {
	private String groupId;
	private UserGroup group;
	private HashSet<SysEntry> children;
	public DefaultMutableTreeNode node;
	
	public UserGroup(String id) {
		groupId = id;
		group = null;
		children = new HashSet<SysEntry>();
	}
	
	public UserGroup(String id, UserGroup parent) {
		groupId = id;
		group = parent;
		parent.addChild(this);
		children = new HashSet<SysEntry>();
	}
	
	public void addChild(SysEntry u) {
		children.add(u);
	}
	
	public HashSet<SysEntry> getChildren() {
		return children;
	}
	
	public String toString() {
		return groupId;
	}

	public int accept(SysEntryVisitor visitor) {
		return visitor.visit(this);
	}
}
