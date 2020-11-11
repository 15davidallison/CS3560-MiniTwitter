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
	
	public UserTree() {
		listNames = new HashSet<String>();
		listGroups = new HashSet<String>();
		root = addGroup("Root"); // tree is automatically created with a root folder
		tree = new JTree(root);
	}
	
	/**
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
	
	private LinkedList<SysEntry> getAllEntries() {
		return DFS(root, new LinkedList<SysEntry>());
	}
	
	private LinkedList<SysEntry> DFS(DefaultMutableTreeNode root, LinkedList<SysEntry> listSoFar) {
		listSoFar.add((SysEntry)root.getUserObject());
		int numChildren = root.getChildCount();
		for (int i = 0; i < numChildren; i++) {
			listSoFar = DFS((DefaultMutableTreeNode)root.getChildAt(i), listSoFar);
		}
		return listSoFar;
	}
	
	/**
	 * @return Size of nameList (number of total users in tree)
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
	 * @return Size of groupList (number of total groups in tree)
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
	
	public int getNumTweets() {
		int count = 0;
		LinkedList<SysEntry> allEntries = getAllEntries();
		ListIterator<SysEntry> i = allEntries.listIterator();
		while (i.hasNext()) {
			count += i.next().accept(new NumTweetsVisitor());
		}
		return count;
	}
	
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
