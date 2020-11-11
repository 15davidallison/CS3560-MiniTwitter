import java.util.LinkedList;
import java.util.ListIterator;
public class NumPosTweetsVisitor implements SysEntryVisitor {
	
	private String[] goodWords = {"good", "great", "best", "happy", "haha", "lol"}; 

	
	/**
	 * Returns an integer value, to get percentage, divide NumPosTweetsVisitor 
	 * by numTweets visitor.
	 */
	public int visit(User user) {
		int numPos = 0;
		LinkedList<String> tweets = user.getTweets();
		ListIterator<String> i = tweets.listIterator();
		while (i.hasNext()) {
			String candidate = i.next().toLowerCase();
			for (int j = 0; j < goodWords.length; j++) {
				if (candidate.contains(goodWords[j])) {
					numPos++;
					break;
				}
			}
		}
		return numPos;
	}

	public int visit(UserGroup group) {
		return 0;
	}

}
