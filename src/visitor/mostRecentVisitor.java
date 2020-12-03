package visitor;
import sysentry.*;
/**
 * @author David Allison
 * Visitor responsible for finding the most recent visitor
 */
public class mostRecentVisitor implements SysEntryVisitor {
	private static long mostRecent;

	/**
	 * @return 1 if this user is the most recent so far, 0 otherwise
	 */
	public int visit(User user) {
		if (user.getLastUpdated() > mostRecent) {
			mostRecent = user.getLastUpdated();
			return 1;
		}
		return 0;
	}

	/**
	 * @return 0, groups cannot post
	 */
	public int visit(UserGroup group) {
		return 0;
	}

}
