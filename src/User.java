import java.util.HashSet;
import javax.swing.tree.DefaultMutableTreeNode;

public class User implements UserComposite {
	private String userId;
	private UserGroup group;
	private HashSet<String> followers;
	private HashSet<String> followings;
	private String[] tweets;
	public DefaultMutableTreeNode node;
	
	public User(String id, UserGroup g) {
		userId = id;
		group = g;
		group.addChild(this);
		followers = new HashSet<String>();
		followings = new HashSet<String>();
		tweets = new String[0];
	}
	
	public String getName() {
		return userId;
	}
	
	public String getGroup() {
		return group.getName();
	}
	
	public void setGroup(UserGroup g) {
		group = g;
	}
	
	public boolean follow(String id) {
		return followings.add(id);
	}
	
	public int numTweets() {
		return tweets.length;
	}
	
	public boolean isLeaf() {
		return true;
	}
	
	public void setNode(DefaultMutableTreeNode n) {
		node = n;
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}
 }
