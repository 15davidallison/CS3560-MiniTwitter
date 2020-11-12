
/**
 * @author David Allison
 * Concrete StatType to pass tree.getNumTweets() into a function
 */
public class NumTweets implements StatType {

	private UserTree tree;
	
	/**
	 * Constructor
	 * @param tree: tree to gather data from
	 */
	public NumTweets(UserTree tree) {
		this.tree = tree;
	}

	/**
	 * @return number of tweets in tree
	 */
	public int getData() {
		return tree.getNumTweets();
	}


}
