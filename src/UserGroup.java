import java.util.HashSet;

public class UserGroup implements UserComposite {
	private String groupId;
	private UserGroup group = null;
	private HashSet<UserComposite> children;
	
	public UserGroup(String id) {
		groupId = id;
		children = new HashSet<UserComposite>();
	}
	
	public UserGroup(String id, UserGroup parent) {
		groupId = id;
		group = parent;
		parent.addChild(this);
		children = new HashSet<UserComposite>();
	}
	
	public String getName() {
		return groupId;
	}
	
	public void addChild(UserComposite u) {
		children.add(u);
	}
	
	public HashSet<UserComposite> getChildren() {
		return children;
	}
	
	public boolean isLeaf() {
		return false;
	}
	
	public String getGroup() {
		if (group == null) {
			return "Root";
		}
		return group.getName();
	}
}
