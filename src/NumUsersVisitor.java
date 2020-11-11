
public class NumUsersVisitor implements SysEntryVisitor {

	public int visit(User user) {
		return 1;
	}

	public int visit(UserGroup group) {
		return 0;
	}

}
