package visitor;
import sysentry.*;

/**
 * @author David Allison
 * Visitor responsible for counting number of tweets
 */
public class NumTweetsVisitor implements SysEntryVisitor {

	/**
	 * @return if object type is User, call numTweets() to count
	 * number of tweets for this User
	 */
	public int visit(User user) {
		return user.numTweets();
	}

	/**
	 * @return 0 if object type is a UserGroup
	 */
	public int visit(UserGroup group) {
		return 0;
	}

}
