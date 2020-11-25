/**
 * @author David Allison
 * Composite SysEntry containing other UserGroups or Users
 */
import java.util.HashSet;
import javax.swing.tree.DefaultMutableTreeNode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserGroup implements SysEntry {
	private String groupId;
	private UserGroup group;
	private HashSet<SysEntry> children;
	public DefaultMutableTreeNode node;
	private long creationTime;
	
	/**
	 * Constructor for UserGroup with no parents (useful for creating root)
	 * @param id: name for this group
	 */
	public UserGroup(String id) {
		groupId = id;
		group = null;
		children = new HashSet<SysEntry>();
		creationTime = System.currentTimeMillis();
	}
	
	/**
	 * Overloaded constructor for UserGroup to specify name and parent group
	 * @param id: name of group
	 * @param parent: parent group
	 */
	public UserGroup(String id, UserGroup parent) {
		groupId = id;
		group = parent;
		creationTime = System.currentTimeMillis();
		parent.addChild(this); // automatically associate this group with its parent
		children = new HashSet<SysEntry>();
	}
	
	/**
	 * @param u: SysEntry to add to this UserGroup as a child
	 */
	public void addChild(SysEntry u) {
		children.add(u);
	}
	
	/**
	 * @return all children for this group
	 */
	public HashSet<SysEntry> getChildren() {
		return children;
	}
	
	/**
	 * Overrides SysEntry.toString() and Object.toString()
	 * @return name of this UserGroup
	 */
	@Override
	public String toString() {
		return groupId;
	}

	/**
	 * Method to implement visitor pattern, inherited from Visitable
	 * @return the integer value determined by the visitor for this UserGroup
	 */
	@Override
	public int accept(SysEntryVisitor visitor) {
		return visitor.visit(this);
	}

	/**
	 * @return the creation time of this User
	 */
	@Override
	public long getCreationTime() {
		return creationTime;
	}
	
	/**
	 * @return a formatted string of creation time of this User
	 */
	@Override
	public String getPrettyCreationTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");    
		Date resultDate = new Date(creationTime);
		return sdf.format(resultDate);
	}
}
