
/**
 * @author David Allison
 * Interface to ensure all users can send out tweets to followers
 * Used for observer pattern.
 */
public interface Observer {
	public void update(Subject subject, String tweet);
}
