
/**
 * @author David Allison
 * Concrete StatType to pass tree.getNumGroups() into a function
 */
public class NumGroups implements StatType {

	private UserTree tree;
	
	/**
	 * Constructor
	 * @param tree: tree to gather data from
	 */
	public NumGroups(UserTree tree) {
		this.tree = tree;
	}

	/**
	 * @return number of groups in tree
	 */
	public int getData() {
		return tree.getNumGroups();
	}

}
