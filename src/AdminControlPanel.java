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
	
	private static DefaultMutableTreeNode root;
	private static UserTree userTree;
	
	public static AdminControlPanel getInstance() {
		if (pointer == null) {
			pointer = new AdminControlPanel();
		}
		return pointer;
	}
	
	private AdminControlPanel() {
		userTree = new UserTree();
		userTree.fillWithDummyData();
		root = userTree.getRoot();

	}
	
	public void launchPanel() {
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
	    treePanel.setLayout(new FlowLayout());
	    
	    JTree tree = userTree.getJTree();
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
//	    JTextArea userArea = new JTextArea(1,10);
//	    JButton addUser = new JButton("Add User");
//	    addUser.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				DefaultMutableTreeNode parentNode;
//				UserGroup selection = groups.get(currentNodeSelection.toString());
//				// if current selection is a group, add this user to the selected group
//				if (selection != null) {
//					parentNode = selection.getNode();
//				} else { // default: "Root"
//					parentNode = (DefaultMutableTreeNode)model.getRoot();
//				}
//				// if user does not exist, add user
//				int status = addUser(userArea.getText());
//				switch(status) {
//					case 0: // user successfully added
//						DefaultMutableTreeNode newUser = new DefaultMutableTreeNode(userArea.getText());
//						users.get(userArea.getText()).setNode(newUser);
//						parentNode.add(newUser);
//						model.reload();
//						messageCenter.setText("User successfully added!");
//						break;
//					case 1:
//						messageCenter.setText("User already exists!");
//						break;
//					case 2:
//						messageCenter.setText("Invalid Username!");
//						break;
//					default:
//						messageCenter.setText("Unknown Error");
//				}
//				// reset for expected behavior next time
//				userArea.setText("");
//				currentNodeSelection = (DefaultMutableTreeNode)model.getRoot();
//			}
//	    });
//	    
//	    JTextArea groupArea = new JTextArea(1,10);
//	    JButton addGroup = new JButton("Add Group");
//	    addGroup.addActionListener(new ActionListener() {
//	    	public void actionPerformed(ActionEvent e) {
//	    		DefaultMutableTreeNode parentNode;
//				UserGroup selection = groups.get(currentNodeSelection.toString());
//				// if current selection is a group, add this user to the selected group
//				if (selection != null) {
//					parentNode = selection.getNode();
//				} else { // default: "Root"
//					parentNode = (DefaultMutableTreeNode)model.getRoot();
//				}
//				// if group does not exist, add group
//				int status = addGroup(groupArea.getText());
//				switch(status) {
//					case 0: // group successfully added
//						DefaultMutableTreeNode newGroup = new DefaultMutableTreeNode(groupArea.getText());
//						groups.get(groupArea.getText()).setNode(newGroup);
//						parentNode.add(newGroup);
//						model.reload();
//						messageCenter.setText("Group successfully added!");
//						break;
//					case 1:
//						messageCenter.setText("Group already exists!");
//						break;
//					case 2:
//						messageCenter.setText("Invalid Username!");
//						break;
//					default:
//						messageCenter.setText("Unknown Error");
//				}	
//				// reset for expected behavior next time
//				groupArea.setText("");
//				currentNodeSelection = (DefaultMutableTreeNode)model.getRoot();
//	    	}
//	    });
	    
	    JButton openUser = new JButton("Open User View");
	    openUser.setPreferredSize(new Dimension(400, 30));
	    openUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentNodeSelection.toString() != null) {
					System.out.println(currentNodeSelection.toString());
	    		} else {
	    			messageCenter.setText("Please select a user.");
	    		}
			}
		});
	    
	    // add all 
	    adminFrame.add(treePanel);
	    JPanel userPanel = new JPanel();
//	    userPanel.add(userArea);
//	    userPanel.add(addUser);
	    adminFrame.add(userPanel);
	    
	    JPanel groupPanel = new JPanel();
//	    groupPanel.add(groupArea);
//	    groupPanel.add(addGroup);
	    adminFrame.add(groupPanel);
	    
	    adminFrame.add(openUser);
	    
	    JPanel dialogPanel = new JPanel();
	    dialogPanel.add(dialogButton(adminFrame, "Total Number of Users",
	    		"Number of users: " + userTree.getNumUsers()));
	    dialogPanel.add(dialogButton(adminFrame, "Total Number of Groups",
	    		"Number of groups: " + userTree.getNumGroups()));
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
		return -1;
	}
	
	public double percentGoodTweets() {
		return .25;
	}
	
	private static class customTreeRenderer extends DefaultTreeCellRenderer {
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			if (value instanceof DefaultMutableTreeNode) {
				 DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				if (node.getUserObject() instanceof UserGroup) {
					// object is a valid group
					setIcon(UIManager.getIcon("FileView.directoryIcon"));
				}
			}
			return this;
		}
	}
}
