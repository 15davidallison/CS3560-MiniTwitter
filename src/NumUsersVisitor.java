
/**
 * @author David Allison
 * Visitor responsible for counting number of Users
 */
public class NumUsersVisitor implements SysEntryVisitor {

	/**
	 * @return 1 if object type is a User
	 */
	public int visit(User user) {
		return 1;
	}
	
	/**
	 * @return 0 if object type is a UserGroup
	 */
	public int visit(UserGroup group) {
		return 0;
	}

}
