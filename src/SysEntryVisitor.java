
/**
 * @author David Allison
 * Interface to make sure all visitors are  able to process 
 * User and UserGroup objects
 */
public interface SysEntryVisitor {
	public int visit(User user);
	public int visit(UserGroup group);
}
