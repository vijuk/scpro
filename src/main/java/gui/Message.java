package gui;

import javax.swing.*;
/**
 * Insert the type's description here.
 * Creation date: (11/30/00 1:15:43 AM)
 * @author: 
 */

public class Message extends JOptionPane {
/**
 * Message constructor comment.
 */
public Message() {
	super();
	initialize();
}
/**
 * Message constructor comment.
 * @param layout java.awt.LayoutManager
 */
public Message(java.awt.LayoutManager layout) {
	super(layout);
}
/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// lib.Util.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("Message");
		setSize(262, 90);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		Message aMessage;
		aMessage = new Message();
		frame.setContentPane(aMessage);
		frame.setSize(aMessage.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of gui.Message");
		exception.printStackTrace(System.out);
	}
}
}
