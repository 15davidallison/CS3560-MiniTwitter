/**
 * @author David Allison
 * User type of SysEntry in a UserTree. Represents a single active user
 * on MiniTwitter. Inherits a list of followers from Observer
 */
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.tree.DefaultMutableTreeNode;

public class User extends Subject implements SysEntry,Observer  {
	private String userId;
	private UserGroup group;
	private HashSet<String> followings;
	private LinkedList<String> tweets;
	public DefaultMutableTreeNode node;
	private UserView userView;
	
	/**
	 * Constructor that creates a User. Automatically follows self
	 * @param id: name of this User
	 * @param g: parent Group for this User
	 */
	public User(String id, UserGroup g) {
		userId = id;
		group = g;
		group.addChild(this);
		followings = new HashSet<String>();
		tweets = new LinkedList<String>();
		follow(userId);
		attach(this);
	}
	
	/**
	 * @param id: name of user wanting to follow
	 * @return: true if not already following User with corresponding id, false otherwise
	 */
	public boolean follow(String id) {
		return followings.add(id);
	}
	
	/**
	 * @return an array of all users currently followed by this User
	 */
	public String[] getFollowings() {
		Object[] objArr = followings.toArray();
		String[] strArr = new String[objArr.length];
		for (int i = 0; i < objArr.length; i++) {
			strArr[i] = (String)objArr[i];
		}
		return strArr;
	}
	
	/**
	 * @return number of tweets associated with this User
	 */
	public int numTweets() {
		return tweets.size();
	}
	
	/**
	 * @return LinkedList of all tweets associated with this User
	 */
	public LinkedList<String> getTweets() {
		return tweets;
	}
	
	/**
	 * Adds a tweet to User.tweets. NOT responsible for distributing tweets
	 * to all followers.
	 * @param tweet: message to be posted as a tweet
	 */
	public void postTweet(String tweet) {
		tweets.add(tweet);
	}
	
	/**
	 * Overrides SysEntry.toString() and Object.toString()
	 * @return name of this User
	 */
	@Override
	public String toString() {
		return userId;
	}

	/**
	 * Method to implement visitor pattern, inherited from Visitable
	 * @return the integer value determined by the visitor for this User
	 */
	@Override
	public int accept(SysEntryVisitor visitor) {
		return visitor.visit(this);
	}
	
	/**
	 * Associates this User with its corresponding UserView on creation of the UserView.
	 * This allows update() to call userView.addToFeed()
	 * @param uv: UserView associated with this User
	 */
	public void setUserView(UserView uv) {
		userView = uv;
	}

	/**
	 * Method used by observer pattern to post tweets to this follower's feed
	 * @param subject: the User sending this tweet
	 * @param tweet: message to be posted to this User's feed
	 */
	public void update(Subject subject, String tweet) {
		userView.addToFeed(((User)subject).toString(), tweet);
	}
 }
