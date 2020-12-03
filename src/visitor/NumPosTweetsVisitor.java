package visitor;
import sysentry.*;

/**
 * @author David Allison
 * Visitor responsible for counting number of positive tweets
 */
import java.util.LinkedList;
import java.util.ListIterator;
public class NumPosTweetsVisitor implements SysEntryVisitor {
	
	// Bank of "positive" words to search for
	private String[] goodWords = {"good", "great", "best", "happy", "haha", "lol"}; 

	
	/**
	 * @return an integer value. To get percentage, divide NumPosTweetsVisitor results
	 * by numTweetsVisitor results.
	 */
	public int visit(User user) {
		int numPos = 0;
		LinkedList<String> tweets = user.getTweets();
		ListIterator<String> i = tweets.listIterator();
		// look through all tweets for a user
		while (i.hasNext()) {
			String candidate = i.next().toLowerCase();
			// check for a substring containing each "positive" word
			for (int j = 0; j < goodWords.length; j++) {
				if (candidate.contains(goodWords[j])) {
					numPos++;
					break;
				}
			}
		}
		return numPos;
	}

	/**
	 * @return 0 if object type is a UserGroup
	 */
	public int visit(UserGroup group) {
		return 0;
	}

}
