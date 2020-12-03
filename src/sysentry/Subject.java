package sysentry;
/**
 * @author David Allison
 * Used in observer pattern in order to handle notifying a list of followers
 */
import java.util.LinkedList;
public class Subject {
	// list of followers for a given user
	private LinkedList<Observer> followers = new LinkedList<Observer>();
	
	/**
	 * @param follower: the User wanting to follow this Subject
	 */
	public void attach(Observer follower) {
		followers.add(follower);
	}
	
	/**
	 * @param tweet: the tweet to be delivered to all followers
	 */
	public void notifyObservers(String tweet) {
		for (Observer follower : followers) {
			follower.update(this, tweet);
		}
	}
}
