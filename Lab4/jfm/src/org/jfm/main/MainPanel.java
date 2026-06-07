package org.jfm.main;

import java.awt.BorderLayout;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import org.jfm.event.Broadcaster;
import org.jfm.event.ChangeDirectoryEvent;
import org.jfm.event.ChangeDirectoryListener;
import org.jfm.event.ChangeViewEvent;
import org.jfm.event.ChangeViewListener;
import org.jfm.po.AddUserAction;
import org.jfm.po.ButtonsPanel;

import org.jfm.po.CopyAction;
import org.jfm.po.EditFileAction;
import org.jfm.po.LogOutAction;
import org.jfm.po.MkdirAction;
import org.jfm.po.MoveAction;
import org.jfm.po.QuitAction;
import org.jfm.po.ViewFileAction;
import org.jfm.views.JFMView;

import edu.asu.ser335.jfm.RolesSingleton;

/**
 * Title: Java File Manager Description: Copyright: Copyright (c) 2001 Company:
 * Home
 * 
 * @author Giurgiu Sergiu
 * @version 1.0
 */
public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private BorderLayout borderLayout1 = new BorderLayout();

    private ButtonsPanel btPanel = new ButtonsPanel();
    private JSplitPane split = new JSplitPane();
    private Preferences prefs = Options.getPreferences();
    private JFMView leftPanel = null;
    private JFMView rightPanel = null;

    public MainPanel(String role) throws Exception {
	jbInit(role);
    }

    void jbInit(String role) throws Exception {
	removeSplitKeyBindings();
	this.setLayout(borderLayout1);

	this.add(btPanel, BorderLayout.SOUTH);
	this.add(split, BorderLayout.CENTER);

	leftPanel = createLeftView();
	rightPanel = createRightView();

	leftPanel.requestFocus();
	split.add(leftPanel, JSplitPane.LEFT);
	split.add(rightPanel, JSplitPane.RIGHT);
	split.setDividerLocation(350);
	setButtonsPanel(role);
	Broadcaster.addChangeViewListener(new ChangeViewListener() {
		public void viewChanged(ChangeViewEvent ev) {
		    if (Options.getActivePanel().equals(leftPanel)) {
			if (ev.getViewRep() != null) {
			    Options.getPreferences().put(Options.JFM_LEFTVIEWPANEL_PREF, ev.getViewRep().getClassName());
			}
			if (ev.getFilesystemClassName() != null) {
			    Options.getPreferences().put(Options.JFM_LEFTVIEWPANEL_FILESYSTEM_PREF,
							 ev.getFilesystemClassName());
			}
			leftPanel = createLeftView();
			split.setLeftComponent(leftPanel);
		    } else {
			if (ev.getViewRep() != null) {
			    Options.getPreferences().put(Options.JFM_RIGHTVIEWPANEL_PREF, ev.getViewRep().getClassName());
			}
			if (ev.getFilesystemClassName() != null) {
			    Options.getPreferences().put(Options.JFM_RIGHTVIEWPANEL_FILESYSTEM_PREF,
							 ev.getFilesystemClassName());
			}
			rightPanel = createRightView();
			split.setRightComponent(rightPanel);
		    }
		    split.setDividerLocation(split.getWidth() / 2);
		}
	    });

	Broadcaster.addChangeDirectoryListener(new ChangeDirectoryListener() {
		public void changeDirectory(ChangeDirectoryEvent event) {
		    if (event.getDirectory() == null || event.getDirectory().getAbsolutePath() == null)
			return;
		    if (event.getSource() == leftPanel) {
			Options.getPreferences().put(Options.JFM_LEFTVIEWPANELDIR_PREF,
						     event.getDirectory().getAbsolutePath());
		    } else {
			Options.getPreferences().put(Options.JFM_RIGHTVIEWPANELDIR_PREF,
						     event.getDirectory().getAbsolutePath());
		    }
		}
	    });
    }

    private JFMView createRightView() {
	String viewClass = prefs.get(Options.JFM_RIGHTVIEWPANEL_PREF, JFMView.DEFAULT_VIEW.getClassName());
	String filesystem = prefs.get(Options.JFM_RIGHTVIEWPANEL_FILESYSTEM_PREF, null);
	// create the default view
	JFMView view = JFMView.createView(JFMView.getViewRepresentation(viewClass), filesystem);
	prefs.put(Options.JFM_RIGHTVIEWPANEL_FILESYSTEM_PREF, view.getFilesystemName());
	// Options.setActivePanel(view);
	view.requestFocus();
	return view;
    }

    private JFMView createLeftView() {
	String viewClass = prefs.get(Options.JFM_LEFTVIEWPANEL_PREF, JFMView.DEFAULT_VIEW.getClassName());
	String filesystem = prefs.get(Options.JFM_LEFTVIEWPANEL_FILESYSTEM_PREF, null);
	// create the default view
	// JFMView view =
	// JFMView.createView(JFMView.getViewRepresentation(viewClass),"org.jfm.filesystems.baracuda.JFMBaracudaFilesystem");
	JFMView view = JFMView.createView(JFMView.getViewRepresentation(viewClass), filesystem);
	prefs.put(Options.JFM_LEFTVIEWPANEL_FILESYSTEM_PREF, view.getFilesystemName());
	// Options.setActivePanel(view);
	return view;
    }

    private void removeSplitKeyBindings() {
	ActionMap newSplitActionMap = new ActionMap();
	split.setActionMap(newSplitActionMap);
    }

    private void setButtonsPanel(String userRole) {
		
	Vector<JButton> buttons = new Vector<JButton>();

	JButton f3Button = new JButton("View (F3)");
	ViewFileAction view = new ViewFileAction();
	f3Button.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F3"), "viewButton");
	f3Button.getActionMap().put("viewButton", view);
	f3Button.addActionListener(view);

	JButton f4Button = new JButton("Edit (F4)");
	EditFileAction edit = new EditFileAction();
	f4Button.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "editButton");
	f4Button.getActionMap().put("editButton", edit);
	f4Button.addActionListener(edit);

	JButton f5Button = new JButton("Copy (F5)");
	CopyAction copy = new CopyAction();
	f5Button.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F5"), "copyButton");
	f5Button.getActionMap().put("copyButton", copy);
	f5Button.addActionListener(copy);

	JButton f6Button = new JButton("Move (F6)");
	MoveAction move = new MoveAction();
	f6Button.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F6"), "moveButton");
	f6Button.getActionMap().put("moveButton", move);
	f6Button.addActionListener(move);

	JButton f7Button = new JButton("Mkdir (F7)");
	MkdirAction mkdir = new MkdirAction();
	f7Button.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F7"), "mkdirButton");
	f7Button.getActionMap().put("mkdirButton", mkdir);
	f7Button.addActionListener(mkdir);

	JButton addNewUser = new JButton("Add User");
	AddUserAction addUser = new AddUserAction();
	addNewUser.addActionListener(addUser);

	JButton f10Button = new JButton("Quit");
	QuitAction quit = new QuitAction();
	f10Button.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F10"), "quitButton");
	f10Button.getActionMap().put("quitButton", quit);
	f10Button.addActionListener(quit);

	JButton logoutButton=new JButton("Logout");
	LogOutAction logout=new LogOutAction();
	logoutButton.addActionListener(logout);

	String[] rpm = RolesSingleton.getRoleMapping().getPrivilegesForRole(userRole); 

	// SER335: You need to figure out uner what roles to allow these buttons to be added
	// You can comment each line out if you like and see how the UI changes.
	/*
	    buttons.addElement(f3Button); //View Button			
	    buttons.addElement(f4Button); //Edit Button
	    buttons.addElement(f5Button); //Copy Button
	    buttons.addElement(f6Button); //Move Button
	    buttons.addElement(f7Button); //Mkdir Button
	*/
	for (int i = 0; rpm != null && i < rpm.length; i++) {
	    System.out.println("Privileges for " + userRole + " role: " + rpm[i] + i);
			
	    //Add User
	    if (rpm[i].equals(CommonConstants.ADDUSER_ACTION)) {
		buttons.addElement(addNewUser);
	    }
	}		
	buttons.addElement(f10Button);    //Quit Button, for everyone
	buttons.addElement(logoutButton); //Logout Button, for everyone
	
	// above declared buttons are set to button pannel
	btPanel.setButtons(buttons);
    }
}
