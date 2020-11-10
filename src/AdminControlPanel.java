import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.*;  
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class AdminControlPanel {
	private static AdminControlPanel pointer; 
	
	private JFrame adminFrame;
	private JPanel treePanel;
	private JLabel currentLabel;
	private JLabel userLabel;
	private JLabel groupLabel;
	private DefaultMutableTreeNode currentNodeSelection;
	
	private Hashtable<String, User> users;
	private Hashtable<String, UserGroup> groups;
	private UserGroup root;
	
	public static AdminControlPanel getInstance() {
		if (pointer == null) {
			pointer = new AdminControlPanel();
		}
		return pointer;
	}
	
	private AdminControlPanel() {
		users = new Hashtable<String, User>();
		groups = new Hashtable<String, UserGroup>();
		root = new UserGroup("Root");
		groups.put("Root", root);
		constructDummyTree(root);
	}
	
	private void constructDummyTree(UserGroup root) {
		users.put("Frank", new User("Frank", root));
		users.put("Katie", new User("Katie", root));
		users.put("Karen", new User("Karen", root));
		users.put("Rick", new User("Rick", root));
		users.put("Kathleen", new User("Kathleen", root));
		UserGroup allison = new UserGroup("Allison", root);
		groups.put("Allison", allison);
		users.put("Emily", new User("Emily", allison));
		UserGroup huber = new UserGroup("Huber", root);
		groups.put("Huber", huber);
		users.put("Liam", new User("Liam", huber));
		users.put("Alita", new User("Alita", huber));
	}
	
	private JTree constructTree(UserGroup root) {
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
		currentNodeSelection = rootNode;
		traverseTree(root, rootNode);
		return new JTree(rootNode);
	}
	
	private void traverseTree(UserComposite parent, DefaultMutableTreeNode parentNode) {
		if (parent.isLeaf()) {
			return;
		}
		// parent is of type UserGroup
		Iterator<UserComposite> i = ((UserGroup)parent).getChildren().iterator();
		while (i.hasNext()) {
			UserComposite nextChild = i.next();
			DefaultMutableTreeNode nextChildNode = new DefaultMutableTreeNode(nextChild.getName());
			parentNode.add(nextChildNode);
			traverseTree(nextChild, nextChildNode);
		}
	}
	
	public void launchPanel() {
		// set up admin frame
		adminFrame = new JFrame("Admin Control Panel");
		adminFrame.setSize(700,500);
	    adminFrame.setLayout(new GridBagLayout());
	    adminFrame.addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent windowEvent){
	    		System.exit(0);
	    	}        
	    });
	    currentLabel = new JLabel("",JLabel.LEFT);
	    userLabel = new JLabel("",JLabel.RIGHT);
	    groupLabel = new JLabel("",JLabel.RIGHT);
	    
	    // set up tree panel
	    treePanel = new JPanel();
	    treePanel.setLayout(new GridBagLayout());
	    JTree tree = constructTree(root);
	    currentLabel.setText(currentNodeSelection.toString());
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    tree.addTreeSelectionListener(new TreeSelectionListener() {
	        public void valueChanged(TreeSelectionEvent e) {
	        	currentNodeSelection = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	        	if (currentNodeSelection != null) {
	        		// if the selection is valid, display what it is
	        		currentLabel.setText(currentNodeSelection.toString());
	        		// if the selection is a user, display name and group
	        		if (users.get(currentNodeSelection.toString()) != null) {
		        		userLabel.setText(users.get(currentNodeSelection.toString()).getName());
		        		groupLabel.setText(users.get(currentNodeSelection.toString()).getGroup());
		        	} else { // the selection is a group
		        		userLabel.setText("NA");
		        		if (currentNodeSelection.toString() == "Root") {
		        			groupLabel.setText("NA");
		        		} else {
		        			groupLabel.setText(groups.get(currentNodeSelection.toString()).getGroup());
		        		}
		        	}
	        	}
	        	
	        	
	        }
	    });
	    JScrollPane treeView = new JScrollPane(tree);
	    treePanel.add(treeView);
	    
	    
	    adminFrame.add(treePanel);
	    adminFrame.add(currentLabel);
	    adminFrame.add(userLabel);
	    adminFrame.add(groupLabel);
	    adminFrame.setVisible(true); 
	}
	
	public int numUsers() {
		return users.size();
	}
	
	public int numTweets() {
		return -1;
	}
	
	public int numGoodTweets() {
		return -1;
	}
}
