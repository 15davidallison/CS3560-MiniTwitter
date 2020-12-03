package stat;
import sysentry.UserTree;

/**
 * @author David Allison
 * Concrete StatType to pass tree.getNumUsers() into a function
 */
public class NumUsers implements StatType {
	
	private UserTree tree;
	
	/**
	 * Constructor
	 * @param tree: tree to gather data from
	 */
	public NumUsers(UserTree tree) {
		this.tree = tree;
	}
	
	/**
	 * @return number of users in tree
	 */
	public int getData() {
		return tree.getNumUsers();
	}

}
