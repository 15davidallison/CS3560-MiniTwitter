
public class NumTweetsVisitor implements SysEntryVisitor {

	public int visit(User user) {
		return user.numTweets();
	}

	public int visit(UserGroup group) {
		return 0;
	}

}
