import java.util.HashSet;

public class User {
	private String userId;
	private HashSet<String> followers;
	private HashSet<String> followings;
	
	public User(String id) {
		userId = id;
		followers = new HashSet<String>();
		followings = new HashSet<String>();
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String id) {
		userId = id;
	}
	public boolean follow(String id) {
		return followings.add(id);
	}
	
	public boolean unfollow(String id) {
		return followings.remove(id);
	}
 }
