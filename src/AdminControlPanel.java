import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;  
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class AdminControlPanel {
	private static AdminControlPanel pointer; 
	
	private static JFrame adminFrame;
	private JPanel treePanel;
	private DefaultMutableTreeNode currentNodeSelection;
	
	private static Hashtable<String, User> users;
	private static Hashtable<String, UserGroup> groups;
	private static UserGroup root;
	
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
		users.put("David", new User("David", allison));
		users.put("Emily", new User("Emily", allison));
		UserGroup huber = new UserGroup("Huber", root);
		groups.put("Huber", huber);
		users.put("Liam", new User("Liam", huber));
		users.put("Alita", new User("Alita", huber));
	}
	
	private int addUser(String name, String group) {
		if (users.containsKey(name)) {
			// user already exists
			return 1;
		}
		if (name == "") {
			return 2;
		}
		// else add user to users
		users.put(name, new User(name, groups.get(group)));
		return 0;
	}
	
	// overloaded addUser method if no group is given -> add to root
	private int addUser(String name) {
		return addUser(name, "Root");
	}
	
	private int addGroup(String name, String group) {
		if (groups.containsKey(name)) {
			// group already exists
			return 1;
		}
		if (name == "") {
			return 2;
		}
		// else add user to users
		groups.put(name, new UserGroup(name, groups.get(group)));
		return 0;
	}
	
	// overloaded addGroup method if no group is given -> add to root
	private int addGroup(String name) {
		return addGroup(name, "Root");
	}
	
	private JTree constructTree(UserGroup root) {
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
		root.setNode(rootNode);
		currentNodeSelection = new DefaultMutableTreeNode();
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
			nextChild.setNode(nextChildNode);
			parentNode.add(nextChildNode);
			traverseTree(nextChild, nextChildNode);
		}
	}
	
	public void launchPanel() {
		System.out.println(users.getClass());
		// set up admin frame
		adminFrame = new JFrame("Admin Control Panel");
		adminFrame.setSize(800,600);
	    adminFrame.setLayout(new FlowLayout());
	    adminFrame.addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent windowEvent){
	    		System.exit(0);
	    	}        
	    });
	    
	    // set up message center
	    JTextArea messageCenter = new JTextArea(1,50);
	    messageCenter.setEditable(false); 
	    
	    // set up tree panel
	    treePanel = new JPanel();
	    JTree tree = constructTree(root);
	    tree.setCellRenderer(new customTreeRenderer());
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    tree.addTreeSelectionListener(new TreeSelectionListener() {
	        public void valueChanged(TreeSelectionEvent e) {
	        	DefaultMutableTreeNode current = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	        	if (current != null) {
	        		currentNodeSelection = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	        	}
	        	messageCenter.setText("");
	        }
	    });
	    DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
	    JScrollPane treeView = new JScrollPane(tree);
	    treePanel.add(treeView);
	    
	    // Set up buttons
	    JTextArea userArea = new JTextArea(1,10);
	    JButton addUser = new JButton("Add User");
	    addUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode parentNode;
				UserGroup selection = groups.get(currentNodeSelection.toString());
				// if current selection is a group, add this user to the selected group
				if (selection != null) {
					parentNode = selection.getNode();
				} else { // default: "Root"
					parentNode = (DefaultMutableTreeNode)model.getRoot();
				}
				// if user does not exist, add user
				int status = addUser(userArea.getText());
				switch(status) {
					case 0: // user successfully added
						DefaultMutableTreeNode newUser = new DefaultMutableTreeNode(userArea.getText());
						users.get(userArea.getText()).setNode(newUser);
						parentNode.add(newUser);
						model.reload();
						messageCenter.setText("User successfully added!");
						break;
					case 1:
						messageCenter.setText("User already exists!");
						break;
					case 2:
						messageCenter.setText("Invalid Username!");
						break;
					default:
						messageCenter.setText("Unknown Error");
				}
				// reset for expected behavior next time
				userArea.setText("");
				currentNodeSelection = (DefaultMutableTreeNode)model.getRoot();
			}
	    });
	    
	    JTextArea groupArea = new JTextArea(1,10);
	    JButton addGroup = new JButton("Add Group");
	    addGroup.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		DefaultMutableTreeNode parentNode;
				UserGroup selection = groups.get(currentNodeSelection.toString());
				// if current selection is a group, add this user to the selected group
				if (selection != null) {
					parentNode = selection.getNode();
				} else { // default: "Root"
					parentNode = (DefaultMutableTreeNode)model.getRoot();
				}
				// if group does not exist, add group
				int status = addGroup(groupArea.getText());
				switch(status) {
					case 0: // group successfully added
						DefaultMutableTreeNode newGroup = new DefaultMutableTreeNode(groupArea.getText());
						groups.get(groupArea.getText()).setNode(newGroup);
						parentNode.add(newGroup);
						model.reload();
						messageCenter.setText("Group successfully added!");
						break;
					case 1:
						messageCenter.setText("Group already exists!");
						break;
					case 2:
						messageCenter.setText("Invalid Username!");
						break;
					default:
						messageCenter.setText("Unknown Error");
				}	
				// reset for expected behavior next time
				groupArea.setText("");
				currentNodeSelection = (DefaultMutableTreeNode)model.getRoot();
	    	}
	    });
	    
	    JButton openUser = new JButton("Open User View");
	    openUser.setPreferredSize(new Dimension(400, 30));
	    openUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (users.get(currentNodeSelection.toString()) != null) {
					System.out.println(currentNodeSelection.toString());
	    		} else {
	    			messageCenter.setText("Please select a user.");
	    		}
			}
		});
	    
	    // add all 
	    adminFrame.add(treePanel);
	    JPanel userPanel = new JPanel();
	    userPanel.add(userArea);
	    userPanel.add(addUser);
	    adminFrame.add(userPanel);
	    
	    JPanel groupPanel = new JPanel();
	    groupPanel.add(groupArea);
	    groupPanel.add(addGroup);
	    adminFrame.add(groupPanel);
	    
	    adminFrame.add(openUser);
	    
	    JPanel dialogPanel = new JPanel();
	    dialogPanel.add(dialogButton(adminFrame, "Total Number of Users",
	    		"Number of users: " + users.size()));
	    dialogPanel.add(dialogButton(adminFrame, "Total Number of Groups",
	    		"Number of groups: " + groups.size()));
	    dialogPanel.add(dialogButton(adminFrame, "Total Number of Tweets",
	    		"Number of Tweets: " + numTweets()));
	    dialogPanel.add(dialogButton(adminFrame, "Positive Percentage of Tweets",
	    		"Percentage of Tweets that are positive: " + percentGoodTweets()));
	    adminFrame.add(dialogPanel);
	   
	    adminFrame.add(messageCenter);
	    adminFrame.setVisible(true); 
	}
	
	public JButton dialogButton(JFrame owner, String title, String desc) {
		JButton button = new JButton(title);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog d = new JDialog(owner, title);
		    	JLabel l = new JLabel(desc);
		    	d.setLayout(new FlowLayout());
		    	d.add(l);
		    	d.setVisible(true);
			}
		});
		return button;
	}
	
	public int numTweets() {
		int total = 0;
		Set<String> keys = users.keySet();
		for (String key: keys) {
			total += users.get(key).numTweets();
		}
		return total;
	}
	
	public double percentGoodTweets() {
		return .25;
	}
	
	private static class customTreeRenderer extends DefaultTreeCellRenderer {
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			if (value instanceof DefaultMutableTreeNode) {
				if (groups.get(value.toString()) != null) {
					// object is a valid group
					setIcon(UIManager.getIcon("FileView.directoryIcon"));
				}
			}
			return this;
		}
	}
}
