import java.util.HashSet;
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
	
	/**
	 * @return Size of nameList (number of total users in tree)
	 */
	public int getNumUsers() {
		return listNames.size();
	}
	
	/**
	 * @return Size of groupList (number of total groups in tree)
	 */
	public int getNumGroups() {
		return listGroups.size();
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
