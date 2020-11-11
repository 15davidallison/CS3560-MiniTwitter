
public class PercentPosTweets implements StatType {

private UserTree tree;
	
	public PercentPosTweets(UserTree tree) {
		this.tree = tree;
	}

	public int getData() {
		return tree.percentGoodTweets();
	}

}
