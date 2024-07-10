package player;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.lang.String;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import javax.media.*;
/**
 * Insert the type's description here.
 * Creation date: (11/5/00 10:09:34 PM)
 * @author: 
 */
public class Player implements ControllerListener, Runnable {
	Thread thread = null;
	boolean m_Playing = false;
	// media player
	protected int timex = -1;
	boolean closing = false;
	boolean application = true;
	boolean fullScreenMode = true;
	protected GainControl gain = null;
	javax.media.Player player = null;
	// component in which video is playing
	Container visualComponent = null;
	// controls gain, position, start, stop
	Component controlComponent = null;
	// displays progress during download
	Component progressBar = null;
/**
 * Insert the method's description here.
 * Creation date: (11/11/00 3:40:29 PM)
 * @param screen java.awt.Component
 */
public Player(Container screen) {
	visualComponent = screen;
	//init();
}
/**
* Stop media file playback and release resources before
* leaving the page.
*/

public javax.media.Player getPlayer()
{
    return player;
}

public void close() {
	if (player != null) {
		stop();
		player.close();
		//player.deallocate();
		player = null;
		//visualComponent.removeAll();
		// Block till we receive ControllerClosedEvent
		try {
			Thread.currentThread().sleep(100);
		} catch (InterruptedException ie) {
		}
	}
}
/**
* This controllerUpdate function must be defined in order
* to implement a ControllerListener interface.  This
* function will be called whenever there is a media event.
*/

public synchronized void controllerUpdate(ControllerEvent ce) {
	try {
		lib.Util.println("Inside controllerUpdate 1");
		if (ce instanceof RealizeCompleteEvent) {
			lib.Util.println("RealizeCompleteEvent");
			if (visualComponent == null) {
				//visualComponent = new Window();
				fullScreenMode = true;
			} else {
				//visualComponent.removeAll();
				fullScreenMode = false;
			}
			// Get the player's visual component, if any, and put it in a Window above
			// the control panel.
			Component vis = player.getVisualComponent();
			if (vis != null) {
				visualComponent.removeAll();
				visualComponent.add(vis);
				visualComponent.setSize(vis.getPreferredSize());
				visualComponent.setVisible(true);
				visualComponent.validate();
				visualComponent.repaint();
				visualComponent.getParent().validate();
				visualComponent.getParent().repaint();
			}
			// Start the player
			player.start();
		} else
			if (ce instanceof ControllerClosedEvent) {
				lib.Util.println("ControllerClosedEvent");
				if (closing)
				{
					//System.exit(0);
					lib.Util.println("Closing");
				}
				else {
					timex = -1;
					gain = null;
					if (fullScreenMode) {
						// Get rid of the visual component
						/*if (visualComponent != null) {
						//((Window) visualComponent).dispose();
						visualComponent = null;
						}*/
					}
				}
				player = null;
			} else
				if (ce instanceof EndOfMediaEvent) {
					lib.Util.println("EndOfMediaEvent");
					rewind(); // Enable this lines to repeat playing - Viju
					play();
				} else
					if (ce instanceof PrefetchCompleteEvent) {
						lib.Util.println("PrefetchCompleteEvent");
						// Get the GainControl from the player, if any, to control sound volume
						gain = (GainControl) player.getControl("javax.media.GainControl");
						//repaint();
					}
	} catch (Exception e) {
		lib.Util.println("Exception occured: " + e);
	}
}
   void Fatal (String s)
   {
	  // Applications will make various choices about what
	  // to do here.  We print a message and then exit

	  System.err.println("FATAL ERROR: " + s);
	  throw new Error(s);  // Invoke the uncaught exception
						   // handler System.exit() is another
						   // choice
   }   
/**
 * Returns information about this applet.
 * @return a string of information about this applet
 */
public String getAppletInfo() {
	return "Player\n" + 
		"\n" + 
		"Insert the type's description here.\n" + 
		"Creation date: (11/5/00 10:09:34 PM)\n" + 
		"@author: \n" + 
		"";
}
	public void loadMovie(java.io.File movieFile) {
	// Prepend a "file:" if no protocol is specified
	String movieURL = movieFile.toString();
	lib.Util.println("File name : " + movieURL);
	if (movieURL.indexOf(":") < 3)
	    movieURL = "file:" + movieURL;
	// Try to create a player
		try {
		//Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, new Boolean(true));
	    player = Manager.createPlayer(new MediaLocator(movieURL));
	    player.addControllerListener(this);
	    player.realize();
		} catch (Exception e) {
			lib.Util.println("Error creating player");
			return;
		}
	}
public static void main(java.lang.String[] args) {
/*
try {
//		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	      lib.Util.println("**");

	Player applet = new Player();
	  applet.application = true;

	java.awt.Frame frame = new java.awt.Frame("Applet");

	//frame.addWindowListener(applet );
	frame.add("Center", applet );
	frame.setSize(350, 250);
	frame.show();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//applet.pack();

		Dimension frameSize = applet .getSize();
		if (frameSize.height > screenSize.height)
				frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
				frameSize.width = screenSize.width;
		applet.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

//		applet .addWindowListener(new java.awt.event.WindowAdapter() {
	//		public void windowClosed(java.awt.event.WindowEvent e) {
				//System.exit(0);
			//};
//		});
		applet.setVisible(true);
applet.init();
applet.start();
	      lib.Util.println("**");
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of Main");
		exception.printStackTrace(System.out);
	}
*/
}
	    // Stop the player
	public void pause() {
	if (player != null)
	    player.stop();
	}
public void play() {
	try {
/*		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = applet .getSize();
		if (frameSize.height > screenSize.height)
				frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
				frameSize.width = screenSize.width; 
		applet.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		applet.init();
		applet.start();
*/
		start();
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of Main");
		exception.printStackTrace(System.out);
	}
}
	// Seek back to zero
	public void rewind() {
	if (player != null)
	    player.setMediaTime(new Time(0));
	}
/**
 * Paints the applet.
 * If the applet does not need to be painted (e.g. if it is only a container for other
 * awt components) then this method can be safely removed.
 * 
 * @param g  the specified Graphics window
 * @see #update
 */
/**
 * Contains the thread execution loop.
 */
public void run() {}
   /**
	* Start media file playback.  This function is called the
	* first time that the Applet runs and every
	* time the user re-enters the page.
	*/

   public void start()
   {
	  // Call start() to prefetch and start the player.
	      lib.Util.println("**1");

	  if (player != null) player.start();
   }      
/**
* Stop media file playback and release resources before
* leaving the page.
*/

public void stop() {
	if (player != null) {
		player.stop();
	}
}
}
