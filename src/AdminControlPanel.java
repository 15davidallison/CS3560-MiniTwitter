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
	private DefaultMutableTreeNode currentNodeSelection;
	private DefaultMutableTreeNode root;
	private UserTree userTree;
	
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
		JFrame adminFrame = new JFrame("Admin Control Panel");
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
	    JPanel treePanel = new JPanel();
	    treePanel.setLayout(new FlowLayout());
	    
	    JTree tree = userTree.getJTree();
	    for (int i = 0; i < tree.getRowCount(); i++) {
	    	   tree.expandRow(i);
	    	}
	    tree.setCellRenderer(new customTreeRenderer());
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    tree.addTreeSelectionListener(new TreeSelectionListener() {
	        public void valueChanged(TreeSelectionEvent e) {
	        	DefaultMutableTreeNode current = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	        	if (current != null) {
	        		currentNodeSelection = current;
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
				DefaultMutableTreeNode result;
				if (userArea.getText().equals("")) {
					messageCenter.setText("Invalid Username!");
					return;
				}
				if (currentNodeSelection != null && 
						currentNodeSelection.getUserObject() instanceof UserGroup) {
					parentNode = currentNodeSelection;
					result = userTree.addUser(userArea.getText(), currentNodeSelection);
				} else {
					result = userTree.addUser(userArea.getText());
				} 
				if (result != null) {
					model.reload();
					messageCenter.setText("User successfully added!");
				} else {
					messageCenter.setText("User already exists!");
				}
				userArea.setText("");
				currentNodeSelection = (DefaultMutableTreeNode)model.getRoot();
			}
	    });
	    
	    JTextArea groupArea = new JTextArea(1,10);
	    JButton addGroup = new JButton("Add Group");
	    addGroup.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		DefaultMutableTreeNode parentNode;
	    		DefaultMutableTreeNode result;
				if (groupArea.getText().equals("")) {
					messageCenter.setText("Invalid group name!");
					return;
				}
				if (currentNodeSelection != null && 
						currentNodeSelection.getUserObject() instanceof UserGroup) {
					parentNode = currentNodeSelection;
					result = userTree.addGroup(groupArea.getText(), currentNodeSelection);
				} else {
					result = userTree.addGroup(groupArea.getText());
				} 
				if (result != null) {
					model.reload();
					messageCenter.setText("Group successfully added!");
				} else {
					messageCenter.setText("Group already exists!");
				}
				groupArea.setText("");
				currentNodeSelection = (DefaultMutableTreeNode)model.getRoot();
		    }
	    });
	    
	    JButton openUser = new JButton("Open User View");
	    openUser.setPreferredSize(new Dimension(400, 30));
	    openUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentNodeSelection != null && currentNodeSelection.toString() != null && 
						currentNodeSelection.getUserObject() instanceof User) {
					new UserView(currentNodeSelection, userTree);
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
	    		"Number of users: ", new NumUsers(userTree)));
	    dialogPanel.add(dialogButton(adminFrame, "Total Number of Groups",
	    		"Number of groups: ", new NumGroups(userTree)));
	    dialogPanel.add(dialogButton(adminFrame, "Total Number of Tweets",
	    		"Number of Tweets: ", new NumTweets(userTree)));
	    dialogPanel.add(dialogButton(adminFrame, "Positive Percentage of Tweets",
	    		"Percentage of Tweets that are positive: ", new PercentPosTweets(userTree)));
	    adminFrame.add(dialogPanel);
	   
	    adminFrame.add(messageCenter);
	    adminFrame.setVisible(true); 
	}
	
	public JButton dialogButton(JFrame owner, String title, String desc, StatType data) {
		JButton button = new JButton(title);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog d = new JDialog(owner, title);
		    	JLabel l = new JLabel(desc + data.getData());
		    	d.setLayout(new FlowLayout());
		    	d.add(l);
		    	d.setVisible(true);
			}
		});
		return button;
	}
	
	private static class customTreeRenderer extends DefaultTreeCellRenderer {
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, 
				boolean expanded, boolean leaf, int row, boolean hasFocus) {
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
