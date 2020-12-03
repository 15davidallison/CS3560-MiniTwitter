package ui;
import sysentry.*;

/**
 * @author David Allison
 * @description Instantiation of this class launches a Java Swing UI for
 * 				performing user-specific MiniTwitter tasks.
 */

import javax.swing.*;  
import javax.swing.tree.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserView {
	private DefaultMutableTreeNode userNode;
	private User user;
	private UserTree data;
	private DefaultListModel<String> feedModel;
	
	/**
	 * UserView Constructor
	 * @param subject: the tree node corresponding to a user. This node MUST hold a User object
	 * @param fullData: a reference to the tree to which the node belongs, used for traversal
	 */
	public UserView(DefaultMutableTreeNode subject, UserTree fullData) {
		data = fullData;
		userNode = subject;
		user = (User)userNode.getUserObject();
		user.setUserView(this);
		launchPanel();
	}
	
	/**
	 * Launches the UserView panel for a specified user.
	 * This method is automatically called by the constructor
	 */
	private void launchPanel() {
		// create new frame
		JFrame userFrame = new JFrame("User View: " + user);
		userFrame.setResizable(false);
		userFrame.setBounds(100, 100, 362, 476);
		userFrame.setLayout(null);
		
		// set up message center
		JTextArea messageCenter = new JTextArea();
		messageCenter.setEditable(false);
		messageCenter.setBounds(10, 397, 331, 44);
		userFrame.add(messageCenter);
		messageCenter.setText("Creation Time: " + user.getPrettyCreationTime() + 
				"\nLast update from user: " + user.getPrettyLastUpdated());
	    
	    // set up list view
		
		// convert existing data (if any) into the model
		DefaultListModel<String> model = new DefaultListModel<String>();
		String[] sourceData = user.getFollowings();
		for (int i = 0; i < sourceData.length; i++) {
			model.addElement(sourceData[i]);
		}
		
		JScrollPane followingView = new JScrollPane();
		followingView.setBounds(10, 47, 331, 103);
		userFrame.add(followingView);
		JList<String> followingList = new JList<String>(model);
		followingView.setViewportView(followingList);
		
		JLabel listLabel = new JLabel("  Currently Following:");
		followingView.setColumnHeaderView(listLabel);
		
	    // set up follow field and button
		JTextArea userArea = new JTextArea();
		userArea.setBounds(10, 13, 150, 20);
		userFrame.add(userArea);
		JButton followUser = new JButton("Follow User");
		followUser.setBounds(191, 12, 150, 23);
		followUser.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
				String followCandidate = userArea.getText();
				if (followCandidate.equals("")) {
					messageCenter.setText("Please enter a user to follow.");
					return;
				}
				if (data.validateUser(followCandidate)) { // user exists
					if(user.follow(followCandidate)) { // successfully added
						messageCenter.setText("Successfully followed " + followCandidate + ".");
						model.addElement(followCandidate);
						((User)data.findUser(followCandidate).getUserObject()).attach(user);
					} else { // already following candidate
						messageCenter.setText("You are already following " + followCandidate + ".");
					}
				} else {
					messageCenter.setText(followCandidate + " does not refer to a real user.");
				}
				userArea.setText("");
			}
		});
		userFrame.add(followUser);
		
		// set up feed view
		feedModel = new DefaultListModel<String>();
		JList<String> feedList = new JList<String>(feedModel);
		JScrollPane feedView = new JScrollPane(feedList);
		feedView.setBounds(10, 283, 331, 103);
		feedView.setViewportView(feedList);
		userFrame.add(feedView);
		JLabel newsFeedLabel = new JLabel("  News Feed:");
		feedView.setColumnHeaderView(newsFeedLabel);
		
		// set up tweet posting area
		JLabel promptLabel = new JLabel("What's on your mind?");
		promptLabel.setBounds(10, 161, 127, 14);
		userFrame.add(promptLabel);
		
		JTextArea tweetArea = new JTextArea();
		tweetArea.setBounds(10, 179, 331, 59);
		userFrame.add(tweetArea);
		JButton postTweet = new JButton("Post Tweet");
		postTweet.setBounds(191, 249, 150, 23);
		postTweet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tweetCandidate = tweetArea.getText();
				if (tweetCandidate.length() > 144) {
					messageCenter.setText("Tweets can be at most 144 characters.");
					return;
				}
				if (tweetCandidate.equals("")) {
					messageCenter.setText("Enter text in order to post a tweet.");
					return;
				}
				user.postTweet(tweetCandidate);
				user.notifyObservers(tweetCandidate);
				messageCenter.setText("Successfully posted tweet!");
				tweetArea.setText("");
			}
		});
		userFrame.add(postTweet);
		
		// set frame to visible
		userFrame.setVisible(true); 
	}
	
	
	/** 
	 * Helper function for posting tweets. Users keep a reference to their
	 * UserView so this method can be called by a user.
	 * @param userId: the user that posted the message
	 * @param tweet: the message to be posted to the feed
	 */
	public void addToFeed(String userId, String tweet) {
		feedModel.add(0, " - " + userId + ": " + tweet);
	}
}
