
public class NumGroupsVisitor implements SysEntryVisitor {

	public int visit(User user) {
		return 0;
	}

	public int visit(UserGroup group) {
		return 1;
	}

}
