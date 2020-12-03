package sysentry;
import visitor.*;
/**
 * @author David Allison
 * Tree structure to hold all tree nodes and generate a JTree with this info.
 * Also helps in implementation of visitor pattern
 */
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.*;  
import javax.swing.tree.*;

public class UserTree {
	private HashSet<String> listNames;
	private HashSet<String> listGroups;
	DefaultMutableTreeNode root;
	private JTree tree;
	
	/**
	 * Constructor which automatically places "Root" at the root of the tree
	 */
	public UserTree() {
		listNames = new HashSet<String>();
		listGroups = new HashSet<String>();
		root = addGroup("Root");
		tree = new JTree(root);
	}
	
	/** 
	 * Method to gather JTree for rendering in the UI
	 * @return UserTree.tree
	 */
	public JTree getJTree() {
		return tree;
	}
	
	/**
	 * @return UserTree.root
	 */
	public DefaultMutableTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Helper function for visitor pattern to return a list of all nodes in UserTree
	 * @return: LinkedList of all entries in tree
	 */
	private LinkedList<SysEntry> getAllEntries() {
		return mapTree(root, new LinkedList<SysEntry>());
	}
	
	/**
	 * Helper function for visitor pattern to return a list of all nodes in UserTree
	 * @param root: root of tree to traverse down from
	 * @param listSoFar: LinkedList to accumulate entries to
	 * @return LinkedList of all entries in tree
	 */
	private LinkedList<SysEntry> mapTree(DefaultMutableTreeNode root, LinkedList<SysEntry> listSoFar) {
		listSoFar.add((SysEntry)root.getUserObject());
		int numChildren = root.getChildCount();
		for (int i = 0; i < numChildren; i++) {
			listSoFar = mapTree((DefaultMutableTreeNode)root.getChildAt(i), listSoFar);
		}
		return listSoFar;
	}
	
	/**
	 * Finds a given user in the tree. Utilizes depth-first-search
	 * @param name: name to find in this UserTree
	 * @return: node containing target user or null if user is not in tree
	 */
	public DefaultMutableTreeNode findUser(String name) {
		return DFS(root, name);
	}
	
	
	/**
	 * Depth-First-Search to find node corresponding to a given user name, utility for findUser
	 * @param root: root of tree to traverse down from
	 * @param name: name to search for
	 * @return: node containing target user or null if user is not in tree
	 */
	private DefaultMutableTreeNode DFS(DefaultMutableTreeNode root, String name) {
		if (root.toString().equals(name)) {
			return root;
		} 
		int numChildren = root.getChildCount();
		for (int i = 0; i < numChildren; i++) {
			DefaultMutableTreeNode childResult = DFS((DefaultMutableTreeNode)root.getChildAt(i), name);
			if (childResult != null) {
				return childResult;
			}
		}
		return null;
	}
	
	/**
	 * Method to use visitor pattern to determine total number of users
	 * @return number of users in UserTree
	 */
	public int getNumUsers() {
		int count = 0;
		LinkedList<SysEntry> allEntries = getAllEntries();
		ListIterator<SysEntry> i = allEntries.listIterator();
		while (i.hasNext()) {
			count += i.next().accept(new NumUsersVisitor());
		}
		return count;
	}
	
	/**
	 * Method to use visitor pattern to determine total number of groups
	 * @return number of groups in UserTree
	 */
	public int getNumGroups() {
		int count = 0;
		LinkedList<SysEntry> allEntries = getAllEntries();
		ListIterator<SysEntry> i = allEntries.listIterator();
		while (i.hasNext()) {
			count += i.next().accept(new NumGroupsVisitor());
		}
		return count;
	}
	
	/**
	 * Method to use visitor pattern to determine total number of tweets
	 * @return number of tweets for all users in UserTree
	 */
	public int getNumTweets() {
		int count = 0;
		LinkedList<SysEntry> allEntries = getAllEntries();
		ListIterator<SysEntry> i = allEntries.listIterator();
		while (i.hasNext()) {
			count += i.next().accept(new NumTweetsVisitor());
		}
		return count;
	}
	
	/**
	 * Method to use visitor pattern to determine positive percentage of tweets
	 * @return integer percentage (0-100)% of tweets in UserTree that meet positive criteria 
	 */
	public int percentGoodTweets() {
		int goodTweets = 0;
		LinkedList<SysEntry> allEntries = getAllEntries();
		ListIterator<SysEntry> i = allEntries.listIterator();
		while (i.hasNext()) {
			goodTweets += i.next().accept(new NumPosTweetsVisitor());
		}
		
		int totalTweets = getNumTweets();
		if (totalTweets != 0) {
			double percent = (double)goodTweets/(double)totalTweets;
			int intVal = (int)(percent*100);
			return intVal;
		}
		return 0;
	}
	
	/**
	 * Method to use visitor pattern to determine if all names are valid
	 * @return true if all names are valid, false otherwise
	 */
	public boolean validateNames() {
		LinkedList<SysEntry> allEntries = getAllEntries();
		ListIterator<SysEntry> i = allEntries.listIterator();
		while (i.hasNext()) {
			if (i.next().accept(new NameValidatorVisitor()) == 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method to use visitor pattern to determine the most recent user to post a tweet
	 * @return the userID of the most recent user to post
	 */
	public String mostRecent() {
		String result = "null";
		LinkedList<SysEntry> allEntries = getAllEntries();
		ListIterator<SysEntry> i = allEntries.listIterator();
		while (i.hasNext()) {
			SysEntry next = i.next();
			if (next.accept(new mostRecentVisitor()) == 1) {
				result = next.toString();
			}
		}
		return result;
	}
	
	/**
	 * @param user: a String to be tested for existence
	 * @return: true if user corresponds to a valid name in listNames
	 * 		  	false if user can be added
	 */
	public boolean validateUser(String user) {
		return listNames.contains(user);
	}
	
	/**
	 * Overloaded addUser method to assume root parent
	 * @param name: String identifier for the User.userId
	 * @param parent: DefaultMutableTreeNode containing a UserGroup
	 * @return Newly created DefaultMutableTreeNode if name is unique, else: null
	 */
	public DefaultMutableTreeNode addUser(String name) {
		return addUser(name, root);
	}
	
	/**
	 * Add a user to the tree
	 * @param name: String identifier for the User.userId
	 * @param parent: DefaultMutableTreeNode containing a UserGroup
	 * @return Newly created DefaultMutableTreeNode if name is unique, else: null
	 */
	public DefaultMutableTreeNode addUser(String name, DefaultMutableTreeNode parent) {
		if (!listNames.contains(name)) { // check to see if name is already used
			DefaultMutableTreeNode newUser = new DefaultMutableTreeNode(new User(name, 
					(UserGroup)parent.getUserObject()));
			parent.add(newUser);
			listNames.add(name);
			return newUser;
		}
		// name already exists
		return null;		
	}
	
	/**
	 * Overloaded addGroup() method to create root/assume root node as parent
	 * @param name: String identifier for the UserGroup.groupId
	 * @param parent: DefaultMutableTreeNode containing a UserGroup
	 * @return Newly created DefaultMutableTreeNode if name is unique, else: null
	 */
	public DefaultMutableTreeNode addGroup(String name) {
		// if no nodes have been made yet, create a root node
		if (listGroups.size() == 0) {
			DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new UserGroup(name));
			listGroups.add(name);
			return rootNode;
		// else assume root directory
		} else {
			return addGroup(name, root);
		}
	}
	
	/**
	 * Add a group to the tree
	 * @param name: String identifier for the UserGroup.groupId
	 * @param parent: DefaultMutableTreeNode containing a UserGroup
	 * @return Newly created DefaultMutableTreeNode if name is unique, else: null
	 */
	public DefaultMutableTreeNode addGroup(String name, DefaultMutableTreeNode parent) {
		if (!listGroups.contains(name)) {
			DefaultMutableTreeNode newGroup = new DefaultMutableTreeNode(new UserGroup(name, 
					(UserGroup)parent.getUserObject()));
			parent.add(newGroup);
			listGroups.add(name);
			return newGroup;
		}
		// name already exists
		return null;
	}
	
	/**
	 * Fill tree with placeholder data for testing
	 */
	public void fillWithDummyData() {
		addUser("Frank");
		addUser("Katie");
		addUser("Patty");
		addUser("Karen");
		addUser("Rick");
		addUser("Kathleen");
		DefaultMutableTreeNode huber = addGroup("Huber");
		DefaultMutableTreeNode allison = addGroup("Allison");
		addUser("Liam", huber);
		addUser("Alita", huber);
		addUser("David", allison);
		addUser("Emily", allison);
		
		
	}
}
