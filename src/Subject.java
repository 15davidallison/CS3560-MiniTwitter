import java.util.LinkedList;
public class Subject {
	private LinkedList<Observer> followers = new LinkedList<Observer>();
	
	public void attach(Observer follower) {
		followers.add(follower);
	}
	
	public void notifyObservers(String tweet) {
		for (Observer follower : followers) {
			follower.update(this, tweet);
		}
	}
}
