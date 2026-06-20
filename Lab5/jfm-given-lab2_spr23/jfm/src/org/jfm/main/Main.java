package org.jfm.main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.UIManager;

import org.jfm.filesystems.JFMFileSystem;
import org.jfm.views.JFMView;

import edu.asu.ser335.jfm.RolesSingleton;
import edu.asu.ser335.jfm.SaltsSingleton;
import edu.asu.ser335.jfm.UsersSingleton;
/**
 * Title: Java File Manager Description: Copyright: Copyright (c) 2001 Company:
 * Home
 * 
 * @author Giurgiu Sergiu
 * @version 1.0
 */
public class Main {
	private boolean packFrame = false;

	/** Construct the application */
	public Main() {

		// Initial the Login Pannel
		LoginPannel pannel = new LoginPannel();

		// Validate frames that have preset sizes
		// Pack frames that have useful preferred size info, e.g. from their layout
		if (packFrame) {
			pannel.pack();
		} else {
			pannel.validate();
		}
		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = pannel.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		pannel.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		pannel.setVisible(true);
	}

	/** Main method */
	public static void main(String[] args) {
		try {
			// Added ser335 to load the initial state of the needed authentication/authorization stuff
			UsersSingleton.getUsers();
			RolesSingleton.getRoleMapping();
			SaltsSingleton.getUserSalts();
			
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFMView.registerViews();
			JFMFileSystem.registerFilesystems();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unexpected error, exiting");
			System.exit(-1);
		}
		// final SplashWindow splash=new SplashWindow("someimage.gif");
		// splash.setVisible(true);

		new Main();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// splash.dispose();
			}
		});
	}
}
