/**
 * 
 */
package org.jfm.po;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

/**
 * @author Nikhil Hiremath
 *
 */
public class ChangePasswordAction extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private boolean enabled = true;

	public Object getValue(String key) {
		// should return something else in here.
		return null;
	}

	public void putValue(String key, Object value) {
	}

	public void setEnabled(boolean b) {
		enabled = b;
	}

	public boolean isEnabled() {
		return true;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// redirect to Change Password Screen
		ChangePasswordPannel frame = new ChangePasswordPannel();

		// Validate frames that have preset sizes
		frame.validate();

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
		dispose();
	}

}
