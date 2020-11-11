
public class NumTweets implements StatType {

private UserTree tree;
	
	public NumTweets(UserTree tree) {
		this.tree = tree;
	}

	public int getData() {
		return tree.getNumTweets();
	}


}
