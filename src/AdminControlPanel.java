/**
 * @author David Allison
 * @description Instantiation of this class launches a Java Swing UI for
 * 				performing admin level MiniTwitter tasks. Can only be 
 * 				instantiated a single time (singleton).
 */

import javax.swing.*;  
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.awt.Component;
import java.awt.Dialog;
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
	
	/**
	 * @return the singleton instance of AdminControlPanel
	 */
	public static AdminControlPanel getInstance() {
		if (pointer == null) {
			pointer = new AdminControlPanel();
		}
		return pointer;
	}
	
	/**
	 * Private constructor for use with singleton pattern
	 */
	private AdminControlPanel() {
		userTree = new UserTree();
		userTree.fillWithDummyData();
		root = userTree.getRoot();
	}
	
	/**
	 * Launches the admin control panel for MiniTwitter
	 * @wbp.parser.entryPoint
	 */
	public void launchPanel() {
		// set up admin frame
		JFrame adminFrame = new JFrame("Admin Control Panel");
		adminFrame.setResizable(false);
		adminFrame.setBounds(100, 100, 540, 423);
	    adminFrame.getContentPane().setLayout(null);
	    adminFrame.addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent windowEvent){
	    		System.exit(0);
	    	}        
	    });
	    
	    
	    // set up message center
	    JTextArea messageCenter = new JTextArea();
	    messageCenter.setBounds(10, 360, 510, 20);
	    adminFrame.getContentPane().add(messageCenter);
	    messageCenter.setEditable(false);
	    adminFrame.getContentPane().add(messageCenter);
	    
	    // set up tree panel
	    JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 265, 343);
		adminFrame.getContentPane().add(scrollPane);
		
		JLabel lblNewLabel = new JLabel("  Users");
		scrollPane.setColumnHeaderView(lblNewLabel);
	    
	    JTree tree = userTree.getJTree();
	    scrollPane.setViewportView(tree);
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
	    
	    // Set up buttons
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setBounds(282, 11, 246, 70);
		adminFrame.getContentPane().add(buttonPanel);
		buttonPanel.setLayout(null);
	    
	    JTextArea userArea = new JTextArea(1,10);
	    userArea.setBounds(10, 11, 106, 20);
	    buttonPanel.add(userArea);
	    
	    JButton addUser = new JButton("Add User");
	    addUser.setBounds(126, 10, 110, 23);
	    addUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode result;
				if (userArea.getText().equals("")) {
					messageCenter.setText("Invalid Username!");
					return;
				}
				if (currentNodeSelection != null && 
						currentNodeSelection.getUserObject() instanceof UserGroup) {
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
	    buttonPanel.add(addUser);
	    
	    JTextArea groupArea = new JTextArea(1,10);
	    groupArea.setBounds(10, 39, 106, 20);
	    buttonPanel.add(groupArea);
	    
	    JButton addGroup = new JButton("Add Group");
	    addGroup.setBounds(126, 38, 110, 23);
	    addGroup.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		DefaultMutableTreeNode result;
				if (groupArea.getText().equals("")) {
					messageCenter.setText("Invalid group name!");
					return;
				}
				if (currentNodeSelection != null && 
						currentNodeSelection.getUserObject() instanceof UserGroup) {
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
	    buttonPanel.add(addGroup);
	    
	    JButton openUser = new JButton("Open User View");
	    openUser.setBounds(292, 91, 226, 23);
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
	    adminFrame.getContentPane().add(openUser);
	    
	    // set up visitor controlled buttons
	    JButton idVerify = new JButton("Validate IDs");
	    idVerify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog d = new JDialog(adminFrame, "Validate IDs");
		    	JLabel l = new JLabel("All IDs are valid: " + userTree.validateNames());
		    	d.getContentPane().setLayout(new FlowLayout());
		    	d.getContentPane().add(l);
		    	d.setVisible(true);
			}
		});
	    idVerify.setBounds(10, 5, 223, 23);
		JPanel panel0 = new JPanel();
		panel0.setBounds(285, 145, 243, 33);
	    panel0.setLayout(null);
	    panel0.add(idVerify);
	    adminFrame.getContentPane().add(panel0);
	    
	    
	    JButton numUsers = dialogButton(adminFrame, "Total Number of Users",
	    		"Number of users: ", new NumUsers(userTree));
	    JPanel panel1 = new JPanel();
	    panel1.setBounds(285, 189, 243, 33);
	    panel1.setLayout(null);
	    panel1.add(numUsers);
	    adminFrame.getContentPane().add(panel1);
	    
	    JButton numGroups = dialogButton(adminFrame, "Total Number of Groups",
	    		"Number of groups: ", new NumGroups(userTree));
	    JPanel panel2 = new JPanel();
	    panel2.setBounds(285, 233, 243, 33);
	    panel2.setLayout(null);
	    panel2.add(numGroups);
	    adminFrame.getContentPane().add(panel2);
	    
	    JButton numTweets = dialogButton(adminFrame, "Total Number of Tweets",
	    		"Number of Tweets: ", new NumTweets(userTree));
	    JPanel panel3 = new JPanel();
	    panel3.setBounds(285, 277, 243, 33);
	    panel3.setLayout(null);
	    panel3.add(numTweets);
	    adminFrame.getContentPane().add(panel3);
	    
	    JButton percentPos = dialogButton(adminFrame, "Positive Percentage of Tweets",
	    		"Percentage of Tweets that are positive: ", new PercentPosTweets(userTree));
	    JPanel panel4 = new JPanel();
	    panel4.setBounds(285, 321, 243, 33);
	    panel4.setLayout(null);
	    panel4.add(percentPos);
	    adminFrame.getContentPane().add(panel4);
	    
	    adminFrame.setVisible(true); 

	}
	
	/**
	 * Button generator function: creates a button which, when clicked, launches a dialog
	 * window.
	 * @param owner: the JFrame which to associate the button
	 * @param title: the header text for the dialog window
	 * @param desc: the text to be displayed in the dialog window
	 * @param data: takes in a object that implements the StatType interface.
	 * 				contains the method getData() as a way to pass a function into this method. 
	 * @return the newly created button
	 */
	private JButton dialogButton(JFrame owner, String title, String desc, StatType data) {
		JButton button = new JButton(title);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog d = new JDialog(owner, title);
		    	JLabel l = new JLabel(desc + data.getData());
		    	d.getContentPane().setLayout(new FlowLayout());
		    	d.getContentPane().add(l);
		    	d.setVisible(true);
			}
		});
		button.setBounds(10, 5, 223, 23);
		return button;
	}
	
	/**
	 * @author David Allison
	 * @description Implements a DefaultTreeCellRenderer to make groups show up as file icons
	 * 				in tree view
	 */
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
