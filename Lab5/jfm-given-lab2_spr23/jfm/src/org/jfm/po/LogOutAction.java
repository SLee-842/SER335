package org.jfm.po;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JFrame;

import org.jfm.main.LoginPannel;
import org.jfm.main.Options;

/**
 * Title:        Java File Manager
 * Description:  
 * Copyright:    Copyright (c) 2001
 * Company:      Home
 * @author Giurgiu Sergiu
 * @version 1.0
 */

public class LogOutAction extends JFrame implements Action {

	private static final long serialVersionUID = 1L;
	public LogOutAction() {
	  }
  private boolean enabled=true;
  public Object getValue(String key) {
    return null;
  }
  public void putValue(String key, Object value) {
  }
  public void setEnabled(boolean b) {
    enabled=b;
  }
  public boolean isEnabled() {
    return enabled;
  }
  public void addPropertyChangeListener(PropertyChangeListener listener) {
  }
  public void removePropertyChangeListener(PropertyChangeListener listener) {
  }
  
//  public void dispose() {
//	    JFrame parent = (JFrame) this.getTopLevelAncestor();
//	    parent.dispose();
//	}
// 
  public void actionPerformed(ActionEvent e) {
	 Options.savePreferences();
	 // on successful login redirect to next screen
     LoginPannel frame = new LoginPannel();
     //Validate frames that have preset sizes
     frame.validate();
     //Center the window
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
     LoginPannel.mainFrame.dispose();
  }

}