package visitor;
import sysentry.*;

/**
 * @author David Allison
 * Visitor responsible for counting number of Groups
 */
public class NumGroupsVisitor implements SysEntryVisitor {

	/**
	 * @return 0 if object type is a User
	 */
	public int visit(User user) {
		return 0;
	}

	/**
	 * @return 1 if object type is a UserGroup
	 */
	public int visit(UserGroup group) {
		return 1;
	}

}
