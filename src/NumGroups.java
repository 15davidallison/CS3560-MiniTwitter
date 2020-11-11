
public class NumGroups implements StatType {

	private UserTree tree;
	
	public NumGroups(UserTree tree) {
		this.tree = tree;
	}

	public int getData() {
		return tree.getNumGroups();
	}

}
