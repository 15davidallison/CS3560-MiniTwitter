import java.util.HashSet;
/**
 * @author David Allison
 * Visitor responsible for validating all user/group names
 */
public class NameValidatorVisitor implements SysEntryVisitor {
	private static HashSet<String> names = new HashSet<String>();

	
	/**
	 * @return true if name is unique from all other ids and
	 * 		   name doesn't contain any spaces, false otherwise
	 */
	public int visit(User user) {
		boolean unique = names.add(user.toString());
		if (unique && !user.toString().contains(" ")) {
			return 1;
		}
		return 0;
	}

	/**
	 * @return true if name is unique from all other ids and
	 * 		   name doesn't contain any spaces, false otherwise
	 */
	public int visit(UserGroup group) {
		boolean unique = names.add(group.toString());
		if (unique && !group.toString().contains(" ")) {
			return 1;
		}
		return 0;
	}

}
