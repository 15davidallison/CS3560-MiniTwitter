import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class User extends Subject implements SysEntry,Observer  {
	private String userId;
	private UserGroup group;
	private HashSet<String> followings;
	private LinkedList<String> tweets;
	public DefaultMutableTreeNode node;
	private UserView userView;
	
	public User(String id, UserGroup g) {
		userId = id;
		group = g;
		group.addChild(this);
		followings = new HashSet<String>();
		tweets = new LinkedList<String>();
		follow(userId); // automatically follow yourself
		attach(this);
	}
	
	public boolean follow(String id) {
		return followings.add(id);
	}
	
	public String[] getFollowings() {
		Object[] objArr = followings.toArray();
		String[] strArr = new String[objArr.length];
		for (int i = 0; i < objArr.length; i++) {
			strArr[i] = (String)objArr[i];
		}
		return strArr;
	}
	
	public int numTweets() {
		return tweets.size();
	}
	
	public LinkedList<String> getTweets() {
		return tweets;
	}
	
	public void postTweet(String tweet) {
		tweets.add(tweet);
	}
	
	public String toString() {
		return userId;
	}

	public int accept(SysEntryVisitor visitor) {
		return visitor.visit(this);
	}
	
	public void setUserView(UserView uv) {
		userView = uv;
	}

	public void update(Subject subject, String tweet) {
		userView.addToFeed(((User)subject).toString(), tweet);
	}
 }
