import java.util.HashSet;

import javax.swing.*;  
import javax.swing.tree.*;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserView {
	private DefaultMutableTreeNode userNode;
	private User user;
	private UserTree data;
	private DefaultListModel<String> feedModel;
	
	public UserView(DefaultMutableTreeNode subject, UserTree fullData) {
		data = fullData;
		userNode = subject;
		user = (User)userNode.getUserObject();
		user.setUserView(this);
		launchPanel();
	}
	
	public void launchPanel() {
		JFrame userFrame = new JFrame("User View: " + user);
		userFrame.setSize(800,600);
		userFrame.setLayout(new FlowLayout());
		
		// set up message center
	    JTextArea messageCenter = new JTextArea(1,40);
	    messageCenter.setEditable(false); 
	    
	    // set up list view
	    JPanel followingPanel = new JPanel();
		followingPanel.setLayout(new FlowLayout());
		
		// convert existing data (if any) into the model
		DefaultListModel<String> model = new DefaultListModel<String>();
		String[] sourceData = user.getFollowings();
		for (int i = 0; i < sourceData.length; i++) {
			model.addElement(sourceData[i]);
		}
		
		JList<String> followingList = new JList<String>(model);
		JScrollPane followingView = new JScrollPane(followingList);
		followingPanel.add(followingView);
		
	    // set up follow field and button
		JTextArea userArea = new JTextArea(1,10);
		JButton followUser = new JButton("Follow User");
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
		
		// set up feed view
	    JPanel feedPanel = new JPanel();
		feedPanel.setLayout(new FlowLayout());
		feedModel = new DefaultListModel<String>();
		JList<String> feedList = new JList<String>(feedModel);
		JScrollPane feedView = new JScrollPane(feedList);
		feedPanel.add(feedView);
		
		// set up tweet posting area
		JTextArea tweetArea = new JTextArea(4,40);
		JButton postTweet = new JButton("Post Tweet");
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
		

		JPanel followPanel = new JPanel();
		followPanel.add(userArea);
		followPanel.add(followUser);
		userFrame.add(followPanel);
		
		userFrame.add(followingPanel);
		
		JPanel tweetPanel = new JPanel();
		tweetPanel.add(tweetArea);
		tweetPanel.add(postTweet);
		userFrame.add(tweetPanel);
		
		userFrame.add(feedPanel);
		
		userFrame.add(messageCenter);
		userFrame.setVisible(true); 
	}
	
	public void addToFeed(String userId, String tweet) {
		feedModel.add(0, " - " + userId + ": " + tweet);
	}
}
