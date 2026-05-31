/**
 * 
 */
package org.jfm.main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//Added for Task H1
import java.nio.charset.StandardCharsets;

//Added for Task H1
import java.util.Map;

import edu.asu.ser335.jfm.RolesSingleton;
//Added for Task H1
import edu.asu.ser335.jfm.UsersSingleton;
import edu.asu.ser335.jfm.SaltsSingleton;
import io.whitfin.siphash.SipHasher;

/**
 * @author Nikhil Hiremath
 *
 */

public class LoginPannel extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel labelUsername = new JLabel("Enter username: ");
	private JLabel labelPassword = new JLabel("Enter password: ");
	private JLabel labelRole = new JLabel("Enter Role: ");
	private JLabel message;
	private JTextField textUsername = new JTextField(20);
	private JPasswordField fieldPassword = new JPasswordField(20);
	// private JTextField textRole = new JTextField(20);
	private JButton buttonLogin = new JButton("Login");
	private JPanel newPanel;

	private JComboBox<String> roleList;
	public static String role;

	public static MainFrame mainFrame;
	
	public LoginPannel() {
		super("Login Pannel");

		// create a new panel with GridBagLayout manager
		newPanel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		// add components to the panel
		// UserName
		constraints.gridx = 0;
		constraints.gridy = 0;
		newPanel.add(labelUsername, constraints);

		constraints.gridx = 1;
		newPanel.add(textUsername, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;

		// Password
		newPanel.add(labelPassword, constraints);

		constraints.gridx = 1;
		newPanel.add(fieldPassword, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;

		// Role
		newPanel.add(labelRole, constraints);
		constraints.gridx = 1;

		// newPanel.add(textRole, constraints);
		// drop down

		roleList = new JComboBox<String>(RolesSingleton.getRoleMapping().getDisplayRoles());

		// add to the parent container (e.g. a JFrame):
		newPanel.add(roleList, constraints);

		// System.out.println("Selected role: " + role);

		constraints.gridx = 0;
		constraints.gridy = 3;

		message = new JLabel();
		newPanel.add(message, constraints);
		constraints.gridx = 1;

		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.CENTER;
		newPanel.add(buttonLogin, constraints);
		// Adding the listeners to components..

		buttonLogin.addActionListener(this);

		// set border for the panel
		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Login Panel"));

		// add the panel to this frame
		add(newPanel);

		pack();
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String userName = textUsername.getText();
		String password = String.valueOf(fieldPassword.getPassword());
		role = (String) roleList.getSelectedItem();

		// Validate User and redirect to Main application on successful login
		if (validateUser(userName, password, role)) {
			message.setText(" Hello " + userName + "");

			// on successful login redirect to next screen
			mainFrame = new MainFrame(role);
			// Validate frames that have preset sizes
			mainFrame.validate();
			// Center the window
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = mainFrame.getSize();
			if (frameSize.height > screenSize.height) {
				frameSize.height = screenSize.height;
			}
			if (frameSize.width > screenSize.width) {
				frameSize.width = screenSize.width;
			}
			mainFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
			mainFrame.setVisible(true);

			// Close Login Window (JPanel)
			dispose();
		} else {
			message.setText(" Invalid user.. ");
		}
	}


	// Login Validation
	/**
	 * Task H1: Implement login functionality (18 points)
	 *
	 * Write your code inside the validateUser() method in the LoginPannel.java file. Implement the following
	 * functionality:
	 *
	 * 1. (3) For each login event, check that the user, role, and user-role mapping exists in the userRoleMapping
	 * table from UsersSingleton.
	 *
	 * 2. (3) If the user, role, or user-role mapping does not exist, then the method will return a ‘false’.
	 *
	 * 3. (9) If the user, role, and user-role mapping exists:
	 * a. Generate a salted password using the ‘siphash’ library.
	 * b. Verify the generated salt password against the salted password stored in the appropriate Singleton. A user
	 * should be successfully validated only if their username, salted password, and role match. The function will
	 * return ‘true’ on successful validation.
	 *
	 * The following login credentials (which use the salts) can be used to validate the user:
	 * User: adam Role: admin Password: 123456
	 * User: zampa Role: developer Password: 123456
	 * User: tim Role: user Password: 123456
	 */

	public boolean validateUser(String uName, String pwd, String role) {
		try {
			for (Map.Entry<String, String> entry : UsersSingleton.getUserRoleMapping().entrySet()) {
				//validate password if username and role match system records
				if (entry.getKey().equals(uName) && entry.getValue().equals(role)) {

					//get users salt
					SaltsSingleton saltsSingleton = SaltsSingleton.getUserSalts();
					byte[] salt = saltsSingleton.getUserSalt(uName).getBytes(StandardCharsets.UTF_8);

					//generate salted password w/user input password and user salt
					long genSaltedPwd = SipHasher.hash(salt, pwd.getBytes(StandardCharsets.UTF_8));

					//get salted password stored in system records
					long saltedPwd = Long.parseLong(UsersSingleton.getUserPasswordMapping().get(uName));

					//return true if generated salted password matches system stored salted password
					if(genSaltedPwd == saltedPwd)
						return true;
					//return false if salted passwords do not match
					else
						return false;
				}
			}
			//return false if username and role do not match system records
			return false;

		} catch (java.lang.Exception e) {
			throw new RuntimeException(e);
		}
	}
}
