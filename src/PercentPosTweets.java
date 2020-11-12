
/**
 * @author David Allison
 * Concrete StatType to pass tree.percentGoodTweets() into a function
 */
public class PercentPosTweets implements StatType {

	private UserTree tree;
	
	/**
	 * Constructor
	 * @param tree: tree to gather data from
	 */
	public PercentPosTweets(UserTree tree) {
		this.tree = tree;
	}
	
	/**
	 * @return integer value of percentage of tweets that are positive
	 */
	public int getData() {
		return tree.percentGoodTweets();
	}

}
