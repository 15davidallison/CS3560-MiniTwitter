
public class NumUsers implements StatType {
	
	private UserTree tree;
	
	public NumUsers(UserTree tree) {
		this.tree = tree;
	}

	public int getData() {
		return tree.getNumUsers();
	}

}
