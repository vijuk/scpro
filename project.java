import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.lang.*;
import java.net.*;
import java.io.*;
import java.net.InetAddress;
import javax.media.*;
import javax.media.protocol.*;
import javax.media.protocol.DataSource;
import javax.media.format.*;
import javax.media.control.TrackControl;
import javax.media.control.QualityControl;
import javax.media.rtp.*;
import javax.media.rtp.rtcp.*;
import com.sun.media.rtp.*;
import java.util.Vector;
import javax.media.rtp.event.*;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.format.FormatChangeEvent;
import javax.media.control.BufferControl;
/**
 * This class is the main class of the client - server application
 * @Tindu Radhakrishnan
 * @Nishka Jacob Koshy
 * @Sini P.S
 */
 public class project
 {
	/**
	 * This is the main class of the whole program.
	 * we call the loginWindow from this main class ...
	 */
	public static void main(String arg[])
	 {

		int i = 1;
		new loginWindow("Please login...").start();
        new InvokeServerWindow().start();
		//   new InvokeVoiceServer().start();
          }
 }
/**
 * This class is used to wait for client's request and invoke ServerWindow
 * when the request comes.
 * This is done in a seperate Thread
 * This class itself is a Thread
 */
class InvokeServerWindow extends Thread
{
  ServerSocket server = null;
  boolean isListening = true;
  public InvokeServerWindow()
  {
  }
  public void run()
  {
    try
    {
     server = new ServerSocket( 3000);
     System.out.println("Connected to port 3000 of local host..");
     while(isListening)
     new ServerWindow(server.accept()).start();
     server.close();
    }
    catch(Exception e)
    { e.printStackTrace(); }
  }
}
/**
 * This class waits for voicecommunication request from clients
 * If a request comes it creates a seperate thread to handle it.
 */
 /*class InvokeVoiceServer extends Thread
 {
    ServerSocket voiceServer = null;
    boolean isListening = true;
    public InvokeVoiceServer()
    {
    }
    public void run()
    {
        try
		{
		voiceServer = new ServerSocket( 3500);
		System.out.println("Connected to port 3500 of local host..");
		while(isListening)
			new ServerVoice(voiceServer.accept()).start();
        voiceServer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
 }*/
 /**
  * This class is the ServerVoice Class
  */
 class ServerVoice extends Thread
{
	int result;
	Socket clientSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	String sOutputLine = null;
	/**
	* The constructor
	*/
	public ServerVoice(Socket clientSocket)
	{
		try
		{
		this.clientSocket = clientSocket;
	    out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}
		catch(Exception e) {}
	}
	/**
	* This is the run method
	*/
	public void run()
	{
		try
		{
		result = JOptionPane.showConfirmDialog(null,"Client requesting voice communication",null,2);
		if(result != 0)
			out.println("Rejected");
		else
			{
				out.println("Accepted");
				Thread.sleep(2000);
				//startReceiver();
			}
		}
		catch(Exception e){}
	}
}
/**
 * This class creates the login window .
 * The window contains a Login area and a Password area
 * The entered data is compared with the database and if correct will proceed to
 * the next frame.If the information is not correct then an alert will be shown.
 */
 class loginWindow extends Thread implements ActionListener
 {
	//------------------------- Data types --------------------------------
	GridBagLayout g1;
	GridBagConstraints gc;
	JFrame loginFrame;
	JButton bOk,bCancel;
	JPanel pLogin;
	JLabel lLogin,lPassword;
	JTextField tLogin;
	TextField tPassword;
	char c='*';
	//------------------ for invalid password message ---------------------
	int b =0;
	String message = "Invalid Password";


	//-------------------------- Constructor ------------------------------
	/**
	 * Constructor of loginWindow
	 * initialises all the datatypes.
	 */
	public loginWindow(String sLabel)
	{
		loginFrame=new JFrame(sLabel);

		//Labels
		lLogin   =new JLabel("ENTER Login ");
		lPassword=new JLabel("ENTER PASSWORD");
		//TextFields
		tLogin   =new JTextField(15);
		tPassword=new TextField(10);
		//Buttons
		bOk=new JButton("OK");
		bCancel=new JButton("CANCEL");
		//Panels
		pLogin=new JPanel();
		//Layout settings
		g1=new GridBagLayout();
        gc=new GridBagConstraints();
        pLogin.setLayout(g1);
		gc.anchor=GridBagConstraints.NORTHWEST;
	}
	/**
	 * buildLogin()
	 * Builds the frame with,
	 * Title - Welcome Lab1
	 * Login field and Password field
	 * Two button, one for "OK" and other for "Cancel"
	*/
	public void buildLogin()
	{
		//setting the Login
		gc.gridx=2;
		gc.gridy=1;
		g1.setConstraints(lLogin,gc);
		pLogin.add(lLogin);
		//setting TextField For User Name
		gc.gridx=4;
		gc.gridy=1;
		g1.setConstraints(tLogin,gc);
		pLogin.add(tLogin);
		//setting Enter Password
		gc.gridx=2;
		gc.gridy=16;
		g1.setConstraints(lPassword,gc);
		pLogin.add(lPassword);
		//setting TextField for Password
		gc.gridx=4;
		gc.gridy=16;
		g1.setConstraints(tPassword,gc);
		pLogin.add(tPassword);
		//tPassword.setEchoCharacter(c);
		//setting OK Button
        gc.anchor=GridBagConstraints.NORTHWEST;
		gc.gridx=6;
		gc.gridy=150;
		g1.setConstraints(bOk,gc);
		pLogin.add(bOk);
		//setting Cancel Button
		gc.gridx=8;
		gc.gridy=150;
		g1.setConstraints(bCancel,gc);
		pLogin.add(bCancel);
		//Adding Panel pLogin To loginFrame
		loginFrame.getContentPane().add(pLogin,BorderLayout.CENTER);
		//Setting size and making visible
		loginFrame.setSize(500,200);
		loginFrame.show();
		//Adding Action Listeners For Buttons
		bOk.addActionListener(this);
		bCancel.addActionListener(this);
	}
	/**
	* The run method
	*/
	public void run()
	 {
		buildLogin();
	 }
	/**
	 * actionPerformed
	 * This function is invoked upon mouseclick on the Ok button.
	 * It compares the information with the database and if correct
	 * creates and object of the ** class and invokes the  build function
	 * of that class
	*/
	public void actionPerformed(ActionEvent ae)
	{
		if(bOk == ae.getSource())
		{
			/*try
			{
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				System.out.println("e");
				Connection con = DriverManager.getConnection("Jdbc:Odbc:Id","","");
				System.out.println("e");
				Statement st = con.createStatement();
				System.out.println("e");
				ResultSet rs = st.executeQuery("Select* from Id");
				b=0;
				while(rs.next())
				{

					if(tn.getText().equals(rs.getString(1)) && tp.getText().equals(rs.getString(2)))
					{
						frame1 ff = new frame1();
						ff.build(tn.getText());
						b=1;
					}

				}//end of while
				if(b==0)
					JOptionPane.showMessageDialog(null,message,"Alert",JOptionPane.ERROR_MESSAGE);

			}//end of try
			catch(Exception e)
			{
				System.out.println(e);
			}*/
/*			selectLab secectTheLab = new selectLab();
			selectTheLab.buildSelectLab();*/



			new SelectMethod().start();
			loginFrame.hide();

		}//end of if(bOk)

	}//end of actionPerformed
}// end of loginWindow

class SelectMethod extends Thread implements ActionListener //WindowListener
{
	//----------------- Data Types ----------------------------------------
	JLabel lSelectMethod;
    JPanel p1,p2,p3,p4,p5,p6,p7,p8;
	JFrame selectFrame = null;
	JButton bType,bVoice,bLogout;
	//constructor
	public  SelectMethod()
	{
		//Frame
		try
		{
		selectFrame=new JFrame("Communicating with Lab2");
		}
		catch(Exception e )
		{}
		//Labels
		lSelectMethod=new JLabel("SELECT A METHOD");
		//Buttons
		bLogout = new JButton("Logout");
		bType = new JButton("TYPE");
		bVoice=new JButton("VOICE");
		//Panels
		p1=new JPanel();
		p2=new JPanel();
		p3=new JPanel();
		p4=new JPanel();
		p5=new JPanel();
		p6=new JPanel();
		p7=new JPanel();
		p8=new JPanel();
	}
	public void run()
	{
		try
		{
		buildSelectFrame();
		}
		catch(Exception e)
		{}
	}
	/**
	* The buildSelectFrame function
	*/
	public void buildSelectFrame()
	{
		try
		{
			selectFrame.getContentPane().setLayout(new BorderLayout());
			selectFrame.getContentPane().add(p1,BorderLayout.NORTH);
			selectFrame.getContentPane().add(p2,BorderLayout.CENTER);
		}
		catch(Exception e)
		{}
		try
		{
			p3.add(lSelectMethod);
			p2.setLayout(new BorderLayout());
			p2.add(p3,BorderLayout.NORTH);
			p2.add(p4,BorderLayout.CENTER);
			p4.setLayout(new BorderLayout());
			p4.add(p5,BorderLayout.NORTH);
			p4.add(p6,BorderLayout.CENTER);
			p6.setLayout(new BorderLayout());
			p6.add(p8,BorderLayout.SOUTH);
			p6.add(p7,BorderLayout.CENTER);
			p7.add(bType);
			p7.add(bVoice);
			p8.add(bLogout);
		}
		catch(Exception e){}
		bType.addActionListener(this);
        bVoice.addActionListener(this);
		bLogout.addActionListener(this);
		//Setting size and making visible
		selectFrame.setSize(300,200);
		selectFrame.show();
	}
	//----------------- Action Performed ----------------------------------
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
		if (bType==ae.getSource())
			new Communication().start();
		if(bVoice == ae.getSource())
			new VoiceWindow ().start();
        if(bLogout == ae.getSource())
		{
			selectFrame.hide();
			new loginWindow("Please login...").start();
		}
		}
		catch(Exception e){e.printStackTrace();}
	}
}//end of class SelectMethod
class Communication extends Thread implements ActionListener
{
	//------------------Data Types ----------------
	JFrame communicationFrame;
	JPanel p1,p2,p3;
    JButton bSend;
    TextArea type;
    TextField tType;
	//for chatting
	Socket kkSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;
    String fromServer = null;
    String fromUser = null;
	int b =9;
//Constructor
	public Communication()
	{
			communicationFrame=new JFrame("Client");
			//text area
			type = new TextArea(20,50);
            tType = new TextField(44);
			tType.setText(" ");
			//Buttons
			bSend=new JButton("SEND");
			//Panels
			p1=new JPanel();
			p2=new JPanel();
			p3=new JPanel();
			//Layout
			communicationFrame.getContentPane().add(p1,BorderLayout.CENTER);
			p1.setLayout(new BorderLayout());
			p1.add(p2,BorderLayout.CENTER);
			p1.add(p3,BorderLayout.SOUTH);
			p2.add(type);
			p3.add(tType);
			p3.add(bSend);
			bSend.addActionListener(this);
	}
	//------------ Function Show ------------------
	/**
	* sets the size of the window and make it visible
	*/
	public void show()
	{
		communicationFrame.setSize(400,400);
		communicationFrame.show();
	}

	/**
	* The run method of Communication
	* This method check whether the server is online or not.
	* If not gives an alert
	* If online then builds the client window and start communication
	*/
	public void run()
	{
		try
		{
		if(check()==1)
		{
			show();
			startChat();
			System.out.println("he");
		}
		}
		catch(Exception e)
		{}
	}

	/**
	* This method checks whether the server is online or not.
	* If online then return 1 else return 0
	*/
	public int check()
	{
		try
        {
            kkSocket = new Socket("192.168.254.167", 3000);
		    System.out.println("Established connection with local host at port 3000...");
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
			return 1;
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host: ");
            System.exit(1);
        }
        catch (IOException e)
        {
			//displaying that the system is not online..
			JOptionPane.showMessageDialog(null,"Not online..","Alert",JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}
	//---------- Function for Chatting ------------
	/**
	* This method is used for the client/server communication
	*/
	public void startChat() throws IOException
	{

			while (true)//(fromServer = in.readLine()) != null
			{
				System.out.println("entering while");
				fromServer = in.readLine();
				if(fromServer  != null)
				{
					System.out.println("entering if != null");
					System.out.println("Server: " + fromServer);
					type.append("\nServer :  "+ fromServer);
				}
				if(b == 2)
					break;
				fromServer = null;
			}
			out.close();
        in.close();
        kkSocket.close();

	}

	//--------------------Action Performed --------
	public void actionPerformed(ActionEvent ae)
	{
		if(bSend == ae.getSource())
		{
			fromUser = tType.getText();
			type.append("\nClient  :"+fromUser);
			out.println(fromUser);
			tType.setText(" ");
		}

	}

}
/**
* The ServerWindow Class
*/
class ServerWindow extends Thread implements ActionListener
{
	//------------------Data Types ----------------
	JFrame serverFrame;
	JPanel pBase,pBase_Center,pBase_South;
    JButton bSend;
	TextArea type;
    TextField tType;
	Thread serverThread = null;

	//for chatting

	Socket clientSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	String sInputLine = null;
	String sOutputLine = null;

	//----------------- Constructor ---------------
	/**
	* This is the constructor
	* It receives an object of Socket and assigns it to clientSocket
	* It also creates the PrintWriter(out) and BufferedReader(in) for the socket
	*/
	public ServerWindow(Socket socket) throws Exception
	{
		super("Client 1");
		clientSocket = socket;
		out = new PrintWriter(clientSocket.getOutputStream(),true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

	/**
	* This function is used to create the frame
	*/
	public void initialise()
	{
		try
		{
		serverFrame=new JFrame("Server...");
		//text area
		type = new TextArea(20,50);
		tType = new TextField(40);
		tType.setText(" ");
		bSend=new JButton("SEND");
		//Panels
		pBase=new JPanel();
		pBase_Center=new JPanel();
		pBase_South=new JPanel();
		//Layout
		serverFrame.getContentPane().add(pBase,BorderLayout.CENTER);
		pBase.setLayout(new BorderLayout());
		pBase.add(pBase_Center,BorderLayout.CENTER);
		pBase.add(pBase_South,BorderLayout.SOUTH);
		pBase_Center.add(type);
		pBase_South.add(tType);
		pBase_South.add(bSend);
		bSend.addActionListener(this);
		//background colour
		type.setBackground(Color.white);
		sOutputLine = null;
		sInputLine  = null;
		serverFrame.setSize(400,400);
		serverFrame.show();
		}
		catch(Exception e)
		{
		  e.printStackTrace();
		}
	}

	/**
	* The run method
	*/
	public void run()
	{
		try
		{
		initialise();
		System.out.println("Received connection from client..:");
		System.out.println(clientSocket);
		 while(true)
		{
			System.out.println("while");
			sInputLine = in.readLine();
			System.out.println("readLine");
			if(sInputLine!=null)
				printInput(sInputLine);
			if(sOutputLine.equals("Bye"))
				break;
			sInputLine = null;
		}
		out.close();
		in.close();
		clientSocket.close();
		}
		catch(IOException e)
		{
		e.printStackTrace();
		}
		catch(Exception e)
		{
		System.out.println(e);
		e.printStackTrace();
		}
	}


	/**
	* Method - processInput()
	* This method prints the input from the client on the server window
	*/
	public void printInput(String sInput)
	{
		try
		{
		type.append("\nClient says :" + sInput );
		}
		catch(Exception e)
		{
		}
	}

    /**
	* This event is written for the mouseclick on the send button
	*/
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == bSend)
		{
			sOutputLine = tType.getText();
			out.println(sOutputLine);
			type.append("\n"+"Server says :"+sOutputLine);
			tType.setText(" ");
		}
	}
}

/**
* This class creates the window for the voice communication.
*/
class VoiceWindow extends Thread implements ActionListener
{
	//data types
	JFrame voiceFrame;
	JPanel pBase,pBase_North,pBase_South,pBase_Center,pBase_Center_North,pBase_Center_South,pBase_Center_Center,pBase_Center_Center_North,pBase_Center_Center_South,pBase_Center_Center_Center;
	JButton bRequest,bStart,bStop;
	//request datas
	Socket requestSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	String sInputLine = null;

	/**
	* The constructor does the initialisation
	*/
	public VoiceWindow()
	{
		voiceFrame = new JFrame("Voice");
		bRequest = new JButton("Request");
		bStart= new JButton("Start");
		bStop= new JButton("Stop");
		pBase = new JPanel();
		pBase_North = new JPanel();
		pBase_South = new JPanel();
		pBase_Center = new JPanel();
		pBase_Center_North= new JPanel();
		pBase_Center_South= new JPanel();
		pBase_Center_Center= new JPanel();
		pBase_Center_Center_North= new JPanel();
		pBase_Center_Center_South= new JPanel();
		pBase_Center_Center_Center= new JPanel();
	}
	/**
	* This is the buildVoiceWindow function which builds the window
	*/
	public void buildVoiceWindow()
	{
		pBase.setLayout(new BorderLayout());
		pBase.add(pBase_North,BorderLayout.NORTH);
		pBase.add(pBase_South,BorderLayout.SOUTH);
		pBase.add(pBase_Center,BorderLayout.CENTER);
		pBase_Center.setLayout(new BorderLayout());
		pBase_Center.add(pBase_Center_North,BorderLayout.NORTH);
		pBase_Center.add(pBase_Center_South,BorderLayout.SOUTH);
		pBase_Center.add(pBase_Center_Center,BorderLayout.CENTER);
		pBase_Center_Center.setLayout(new BorderLayout());
		pBase_Center_Center.add(pBase_Center_Center_North,BorderLayout.NORTH);
		pBase_Center_Center.add(pBase_Center_Center_South,BorderLayout.SOUTH);
		pBase_Center_Center.add(pBase_Center_Center_Center,BorderLayout.CENTER);
		pBase_North.add(bRequest);
		pBase_South.add(bStart);
		pBase_Center_Center_Center.add(bStop);
		voiceFrame.getContentPane().add(pBase,BorderLayout.CENTER);
		bRequest.addActionListener(this);
		bStart.addActionListener(this);
		bStop.addActionListener(this);
		voiceFrame.setSize(250,200);
		voiceFrame.show();
	}
	/**
	* This is the actionPerformed funtion
	* It maps the mouseClicks on the buttons
	*/
	public void actionPerformed(ActionEvent ae)
	{
		if(bRequest == ae.getSource())
		{
			try
			{
			if(checkForServer()==1)
			{
				sInputLine = in.readLine();
				if(sInputLine.equals("Accepted"))
				{
					//startTranmission
					new Transmission().start();
					System.out.println("sucess");
				}
			}
			}
			catch(Exception e)
			{
			}
		}
	}

	public int checkForServer()
	{
		try
        {
            requestSocket= new Socket("192.168.254.167", 3500);
		    System.out.println("Established connection with local host at port 3500...");
            out = new PrintWriter(requestSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
			return 1;
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host: ");
            System.exit(1);
        }
        catch (IOException e)
        {
			//displaying that the system is not online..
			JOptionPane.showMessageDialog(null,"Not online..","Alert",JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}//end of checkForServer
	/**
	* This is the run method
	*/
	public void run()
	{
		try
		{
			buildVoiceWindow();
		}
		catch(Exception e){}
	}
}

/**
* The Transmission class
*/
class Transmission extends Thread
{
	public void Trasmission()
	{
	}
	public void run()
	{
		// We need three parameters to do the transmission
			// For example,
			//   java AVTransmit2 file:/C:/media/test.mov  129.130.131.132 42050
			Format fmt = null;
			int i = 0;
			System.out.println("Stage 1");
			// Create a audio transmit object with the specified params.
			AVTransmit2 at = new AVTransmit2(new MediaLocator("dsound://"),
							 "localhost", "5000", fmt);
			System.out.println("Stage 2");
			// Start the transmission
			String result = at.start();
			System.out.println("Stage 3");
			// result will be non-null if there was an error. The return
			// value is a String describing the possible error. Print it.
			if (result != null)
				{
					System.err.println("Error : " + result);
					System.exit(0);
				}
			System.err.println("Start transmission for 6 minutes...");
			// Transmit for 60 seconds and then close the processor
			// This is a safeguard when using a capture data source
			// so that the capture device will be properly released
			// before quitting.
			// The right thing to do would be to have a GUI with a
			// "Stop" button that would call stop on AVTransmit2
			try
				{
					Thread.currentThread().sleep(600000);
				}
			catch (InterruptedException ie)
				{
				}
			System.out.println("Stage 4");

			// Stop the transmission
			at.stop();

			System.err.println("...transmission ended.");

			System.exit(0);
		}
}
class AVTransmit2
	{

    // Input MediaLocator
    // Can be a file or http or capture source
    private MediaLocator locator;
    private String ipAddress;
    private int portBase;

    private Processor processor = null;
    private RTPManager rtpMgrs[];
    private DataSource dataOutput = null;

    public AVTransmit2(MediaLocator locator,String ipAddress, String pb,
														 Format format)
		{

			this.locator = locator;
			this.ipAddress = ipAddress;
			Integer integer = Integer.valueOf(pb);
			if (integer != null)
				this.portBase = integer.intValue();
		}

    /**
     * Starts the transmission. Returns null if transmission started ok.
     * Otherwise it returns a string with the reason why the setup failed.
     */
    public synchronized String start()
		{
			String result;
			System.out.println("Stage *1");

			// Create a processor for the specified media locator
			// and program it to output JPEG/RTP
			result = createProcessor();
			if (result != null)
				return result;
			System.out.println("Stage *2");

			// Create an RTP session to transmit the output of the
			// processor to the specified IP address and port no.
			result = createTransmitter();
			System.out.println("Stage *3");

			if (result != null)
				{
					processor.close();
					processor = null;
					return result;
				}
			System.out.println("Stage *4");

			// Start the transmission
			processor.start();

			System.out.println("Stage *5");

			return null;
		}

    /**
     * Stops the transmission if already started
     */
    public void stop()
		{
			synchronized (this)
				{
					if (processor != null)
						{
							processor.stop();
							processor.close();
							processor = null;
							for (int i = 0; i < rtpMgrs.length; i++)
								{
									rtpMgrs[i].removeTargets( "Session ended.");
									rtpMgrs[i].dispose();
								}
						}
				}
		}

    private String createProcessor()
		{
			if (locator == null)
				return "Locator is null";

			DataSource ds;
			DataSource clone;
			System.out.println("Stage *--1");

			try
				{
					ds = javax.media.Manager.createDataSource(locator);
				}
			catch (Exception e)
				{
					return "Couldn't create DataSource";
				}
			System.out.println("Stage *--2");

			// Try to create a processor to handle the input media locator
			try
				{
					processor = javax.media.Manager.createProcessor(ds);
				}
			catch (NoProcessorException npe)
				{
					npe.printStackTrace();
					return "Couldn't create processor";
				}
			catch (IOException ioe)
				{
					ioe.printStackTrace();
					return "IOException creating processor";
				}
			System.out.println("Stage *--3");

			// Wait for it to configure
			boolean result = waitForState(processor, Processor.Configured);
			if (result == false)
				{
					System.out.println("False returned");
				return "Couldn't configure processor";
				}

	        System.out.println("Stage *--4");

			// Get the tracks from the processor
			TrackControl [] tracks = processor.getTrackControls();

			// Do we have atleast one track?
			if (tracks == null || tracks.length < 1)
				return "Couldn't find tracks in processor";

			// Set the output content descriptor to RAW_RTP
			// This will limit the supported formats reported from
			// Track.getSupportedFormats to only valid RTP formats.
			ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
			processor.setContentDescriptor(cd);

			Format supported[];
			Format chosen;
			boolean atLeastOneTrack = false;
			System.out.println("Stage *-5");

			// Program the tracks.
			for (int i = 0; i < tracks.length; i++)
				{
					Format format = tracks[i].getFormat();
					if (tracks[i].isEnabled())
						{

							supported = tracks[i].getSupportedFormats();

							// We've set the output content to the RAW_RTP.
							// So all the supported formats should work with RTP.
							// We'll just pick the first one.

							if (supported.length > 0)
								{
									if (supported[0] instanceof VideoFormat)
										{
											// For video formats, we should double check the
											// sizes since not all formats work in all sizes.
											chosen = checkForVideoSizes(tracks[i].getFormat(),
															supported[0]);
										}
									else
										chosen = supported[0];
										tracks[i].setFormat(chosen);
										System.err.println("Track " + i + " is set to transmit as:");
										System.err.println("  " + chosen);
										atLeastOneTrack = true;
								}
								else
								    tracks[i].setEnabled(false);
						}
						else
							tracks[i].setEnabled(false);
				}
			System.out.println("Stage *-6");

			if (!atLeastOneTrack)
				return "Couldn't set any of the tracks to a valid RTP format";

			// Realize the processor. This will internally create a flow
			// graph and attempt to create an output datasource for JPEG/RTP
			// audio frames.
			result = waitForState(processor, Controller.Realized);
			if (result == false)
				return "Couldn't realize processor";

			// Set the JPEG quality to .5.
			setJPEGQuality(processor, 0.5f);

			// Get the output data source of the processor
			dataOutput = processor.getDataOutput();
			System.out.println("Stage *-7");

			return null;
		}
//************** end of create processor() *********************

    /**
     * Use the RTPManager API to create sessions for each media
     * track of the processor.
     */
    private String createTransmitter()
		{

			// Cheated.  Should have checked the type.
			PushBufferDataSource pbds = (PushBufferDataSource)dataOutput;
			PushBufferStream pbss[] = pbds.getStreams();

			rtpMgrs = new RTPManager[pbss.length];
			SessionAddress localAddr, destAddr;
			InetAddress ipAddr;
			SendStream sendStream;
			int port;
			SourceDescription srcDesList[];

			for (int i = 0; i < pbss.length; i++)
				{
					try
					{
						rtpMgrs[i] = RTPManager.newInstance();

						// The local session address will be created on the
						// same port as the the target port. This is necessary
						// if you use AVTransmit2 in conjunction with JMStudio.
						// JMStudio assumes -  in a unicast session - that the
						// transmitter transmits from the same port it is receiving
						// on and sends RTCP Receiver Reports back to this port of
						// the transmitting host.

						port = portBase + 2*i;
						ipAddr = InetAddress.getByName(ipAddress);

						localAddr = new SessionAddress( InetAddress.getLocalHost(),
										port);

						destAddr = new SessionAddress( ipAddr, port);

						rtpMgrs[i].initialize( localAddr);

						rtpMgrs[i].addTarget( destAddr);

						System.err.println( "Created RTP session: " + ipAddress + " " + port);

						sendStream = rtpMgrs[i].createSendStream(dataOutput, i);
						sendStream.start();
					}
					catch (Exception  e)
						{
							return e.getMessage();
						}
				}

			return null;
		}


    /**
     * For JPEG and H263, we know that they only work for particular
     * sizes.  So we'll perform extra checking here to make sure they
     * are of the right sizes.
     */
    Format checkForVideoSizes(Format original, Format supported)
		{

			int width, height;
			Dimension size = ((VideoFormat)original).getSize();
			Format jpegFmt = new Format(VideoFormat.JPEG_RTP);
			Format h263Fmt = new Format(VideoFormat.H263_RTP);

			if (supported.matches(jpegFmt))
				{
					// For JPEG, make sure width and height are divisible by 8.
					width = (size.width % 8 == 0 ? size.width :
							(int)(size.width / 8) * 8);
					height = (size.height % 8 == 0 ? size.height :
							(int)(size.height / 8) * 8);
				}
			else
				if (supported.matches(h263Fmt))
				{
					// For H.263, we only support some specific sizes.
					if (size.width < 128)
						{
						width = 128;
						height = 96;
						}
					else
						if (size.width < 176)
						{
						width = 176;
						height = 144;
						}
						else
						{
							width = 352;
							height = 288;
						}
				}
				else
				{
					// We don't know this particular format.  We'll just
					// leave it alone then.
					return supported;
				}

			return (new VideoFormat(null,new Dimension(width, height),
			Format.NOT_SPECIFIED,null,	Format.NOT_SPECIFIED)).intersects(supported);
		}
// end of format

    /**
     * Setting the encoding quality to the specified value on the JPEG encoder.
     * 0.5 is a good default.
     */
    void setJPEGQuality(Player p, float val)
		{

			Control cs[] = p.getControls();
			QualityControl qc = null;
			VideoFormat jpegFmt = new VideoFormat(VideoFormat.JPEG);

			// Loop through the controls to find the Quality control for
			// the JPEG encoder.
			for (int i = 0; i < cs.length; i++)
				{

					if (cs[i] instanceof QualityControl && cs[i] instanceof Owned)
						{
							Object owner = ((Owned)cs[i]).getOwner();

							// Check to see if the owner is a Codec.
							// Then check for the output format.
							if (owner instanceof Codec)
								{
									Format fmts[] = ((Codec)owner).getSupportedOutputFormats(null);
									for (int j = 0; j < fmts.length; j++)
										{
											if (fmts[j].matches(jpegFmt))
												{
													qc = (QualityControl)cs[i];
													qc.setQuality(val);
													System.err.println("- Setting quality to " + val + " on " + qc);
													break;
												}
										}
								}
							if (qc != null)
							break;
						}
				}//end of for loop
		}// end of void function


	/****************************************************************
     * Convenience methods to handle processor's state changes.
     ****************************************************************/

    private Integer stateLock = new Integer(0);
    private boolean failed = false;

    Integer getStateLock() {
	return stateLock;
    }

    void setFailed() {
	failed = true;
    }

    private synchronized boolean waitForState(Processor p, int state) {
	p.addControllerListener(new StateListener());
	failed = false;

        // Call the required method on the processor
	if (state == Processor.Configured) {
	    p.configure();
	} else if (state == Processor.Realized) {
	    p.realize();
	}

	// Wait until we get an event that confirms the
	// success of the method, or a failure event.
	// See StateListener inner class

        try
        {
        Thread.currentThread().sleep(5000);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

         while (p.getState() < state && !failed) {
	    synchronized (getStateLock()) {
		try {
		    getStateLock().wait();
                    System.out.println("OK");
		} catch (Exception ie) {
		    return false;
		}
	    }
	}

	if (failed)
	    return false;
	else
	    return true;
    }

    /****************************************************************
     * Inner Classes
     ****************************************************************/

    class StateListener implements ControllerListener {

	public void controllerUpdate(ControllerEvent ce) {

	    // If there was an error during configure or
	    // realize, the processor will be closed
            System.out.println(ce.toString());
	    if (ce instanceof ControllerClosedEvent)
		setFailed();

	    // All controller events, send a notification
	    // to the waiting thread in waitForState method.
	    if (ce instanceof ControllerEvent) {
		synchronized (getStateLock()) {
		    getStateLock().notifyAll();
		}
	    }
	}
    }


    /****************************************************************
     * Sample Usage for AVTransmit2 class
     ****************************************************************/

/*    public  void run()
		{
			// We need three parameters to do the transmission
			// For example,
			//   java AVTransmit2 file:/C:/media/test.mov  129.130.131.132 42050

			if (args.length < 3)
				{
					//prUsage();
					args = new String[3];
					//args[0] = "file:c:/tinku/clock.avi";
					args[0] = "dsound://";
					args[1] = "localhost";
					args[2] = "5000";
					// provide your ip address, use 255 at the end for multicast
				}

			Format fmt = null;
			int i = 0;
			System.out.println("Stage 1");
			// Create a audio transmit object with the specified params.
			AVTransmit2 at = new AVTransmit2(new MediaLocator(args[i]),
							 args[i+1], args[i+2], fmt);
			System.out.println("Stage 2");
			// Start the transmission
			String result = at.start();

			System.out.println("Stage 3");

			// result will be non-null if there was an error. The return
			// value is a String describing the possible error. Print it.
			if (result != null)
				{
					System.err.println("Error : " + result);
					System.exit(0);
				}

			System.err.println("Start transmission for 6 minutes...");

			// Transmit for 60 seconds and then close the processor
			// This is a safeguard when using a capture data source
			// so that the capture device will be properly released
			// before quitting.
			// The right thing to do would be to have a GUI with a
			// "Stop" button that would call stop on AVTransmit2
			try
				{
					Thread.currentThread().sleep(600000);
				}
			catch (InterruptedException ie)
				{
				}
			System.out.println("Stage 4");

			// Stop the transmission
			at.stop();

			System.err.println("...transmission ended.");

			System.exit(0);
		}*/


    static void prUsage()
		{
			System.err.println("Usage: AVTransmit2 <sourceURL> <destIP> <destPortBase>");
			System.err.println("     <sourceURL>: input URL or file name");
			System.err.println("     <destIP>: multicast, broadcast or unicast IP address for the transmission");
			System.err.println("     <destPortBase>: network port numbers for the transmission.");
			System.err.println("                     The first track will use the destPortBase.");
			System.err.println("                     The next track will use destPortBase + 2 and so on.\n");
			System.exit(0);
	    }
	}

/**
 * AVReceive2 to receive RTP transmission using the new RTP API.
 */
class AVReceive2 implements ReceiveStreamListener, SessionListener,
	ControllerListener
{
    String sessions[] = null;
    RTPManager mgrs[] = null;
    Vector playerWindows = null;
    boolean dataReceived = false;
    Object dataSync = new Object();
    public AVReceive2(String sessions[])
	{
	this.sessions = sessions;
    }
    protected boolean initialize()
	{
        try
		{
		System.out.println(2);
	    InetAddress ipAddr;
	    SessionAddress localAddr = new SessionAddress();
	    SessionAddress destAddr;
		System.out.println(3);
	    mgrs = new RTPManager[sessions.length];
	    playerWindows = new Vector();
		System.out.println(4);
	    SessionLabel session;
	    // Open the RTP sessions.
	    for (int i = 0; i < sessions.length; i++)
		{
		 	// Parse the session addresses.
			try
			{
		    session = new SessionLabel(sessions[i]);
			}
			catch (IllegalArgumentException e)
			{
		    System.err.println("Failed to parse the session address given: " + sessions[i]);
		    return false;
			}
			System.err.println("  - Open RTP session for: addr: " + session.addr + " port: " + session.port + " ttl: " + session.ttl);
			mgrs[i] = (RTPManager) RTPManager.newInstance();
			mgrs[i].addSessionListener(this);
			mgrs[i].addReceiveStreamListener(this);
			System.out.println(session.addr);
			String st = session.addr;
			ipAddr = InetAddress.getByName("localhost");//here is the error
			System.out.println(6);
			if( ipAddr.isMulticastAddress())
			{
				// local and remote address pairs are identical:
				localAddr= new SessionAddress( ipAddr,
						   session.port,
						   session.ttl);
				destAddr = new SessionAddress( ipAddr,
						   session.port,
						   session.ttl);
			}
			else
			{
		    localAddr= new SessionAddress( InetAddress.getLocalHost(),
			  		           session.port);
			System.out.println(6);
                    destAddr = new SessionAddress( ipAddr, session.port);
			}
			System.out.println(7);
			mgrs[i].initialize( localAddr);
			// You can try out some other buffer size to see
			// if you can get better smoothness.
			BufferControl bc = (BufferControl)mgrs[i].getControl("javax.media.control.BufferControl");
			if (bc != null)
		    bc.setBufferLength(350);
		    System.out.println(8);
    		mgrs[i].addTarget(destAddr);
	    }//end of for
        } //end of try
		catch (Exception e)
		{
           e.printStackTrace();
			System.err.println("Cannot create the RTP Session: " + e.getMessage());
            return false;
        }
		// Wait for data to arrive before moving on.
		long then = System.currentTimeMillis();
		long waitingPeriod = 30000;  // wait for a maximum of 30 secs.
		try
		{
		synchronized (dataSync)
		{
			while (!dataReceived &&
			System.currentTimeMillis() - then < waitingPeriod)
			{
				if (!dataReceived)
				System.err.println("  - Waiting for RTP data to arrive...");
				dataSync.wait(1000);
			}
	    }
		}//end of try
		catch (Exception e) {}
		if (!dataReceived)
		{
			System.err.println("No RTP data was received.");
			close();
			return false;
		}
        return true;
    }//end of function intitialize
    public boolean isDone()
	{
	return playerWindows.size() == 0;
    }
    /**
     * Close the players and the session managers.
     */
    protected void close()
	{
		for (int i = 0; i < playerWindows.size(); i++)
		{
	    try
		{
		((PlayerWindow)playerWindows.elementAt(i)).close();
	    }
		catch (Exception e) {}
		}//end of for
		playerWindows.removeAllElements();
		// close the RTP session.
		for (int i = 0; i < mgrs.length; i++)
		{
			if (mgrs[i] != null)
			{
				mgrs[i].removeTargets( "Closing session from AVReceive2");
				mgrs[i].dispose();
				mgrs[i] = null;
			}
		}//end of for
    }//end of close

	PlayerWindow find(Player p)
	{
		for (int i = 0; i < playerWindows.size(); i++)
		{
			PlayerWindow pw = (PlayerWindow)playerWindows.elementAt(i);
			if (pw.player == p)
			return pw;
		}
	return null;
    }

    PlayerWindow find(ReceiveStream strm)
	{
		for (int i = 0; i < playerWindows.size(); i++)
		{
			PlayerWindow pw = (PlayerWindow)playerWindows.elementAt(i);
			if (pw.stream == strm)
			return pw;
		}
		return null;
    }

    /**
     * SessionListener.
     */
    public synchronized void update(SessionEvent evt)
	{
		if (evt instanceof NewParticipantEvent)
		{
			Participant p = ((NewParticipantEvent)evt).getParticipant();
			System.err.println("  - A new participant had just joined: " + p.getCNAME());
		}
    }

    /**
     * ReceiveStreamListener
     */
    public synchronized void update( ReceiveStreamEvent evt)
	{
		RTPManager mgr = (RTPManager)evt.getSource();
		Participant participant = evt.getParticipant();	// could be null.
		ReceiveStream stream = evt.getReceiveStream();  // could be null.
		if (evt instanceof RemotePayloadChangeEvent)
		{
			System.err.println("  - Received an RTP PayloadChangeEvent.");
			System.err.println("Sorry, cannot handle payload change.");
			System.exit(0);
		}
    	else
			if (evt instanceof NewReceiveStreamEvent)
			{
				try
				{
				stream = ((NewReceiveStreamEvent)evt).getReceiveStream();
				DataSource ds = stream.getDataSource();
				// Find out the formats.
				RTPControl ctl = (RTPControl)ds.getControl("javax.media.rtp.RTPControl");
				if (ctl != null)
				{
					System.err.println("  - Recevied new RTP stream: " + ctl.getFormat());
				}
				else
					System.err.println("  - Recevied new RTP stream");
				if (participant == null)
			    System.err.println("      The sender of this stream had yet to be identified.");
				else
					{
						 System.err.println("      The stream comes from: " + participant.getCNAME());
					}
				// create a player by passing datasource to the Media Manager
				Player p = javax.media.Manager.createPlayer(ds);
				//gui.ScreenCam sc = new gui.ScreenCam();
				//              Player p = sc.getPlayer();
                if (p == null)
					return;
				p.addControllerListener(this);
				p.realize();
				PlayerWindow pw = new PlayerWindow(p, stream);
				playerWindows.addElement(pw);
				// Notify intialize() that a new stream had arrived.
				synchronized (dataSync)
				{
					dataReceived = true;
					dataSync.notifyAll();
				}
			    }//end of try
				catch (Exception e)
				{
					System.err.println("NewReceiveStreamEvent exception " + e.getMessage());
					return;
				}
      		}//end of if
			else
				if (evt instanceof StreamMappedEvent)
				{
					if (stream != null && stream.getDataSource() != null)
					{
						DataSource ds = stream.getDataSource();
						// Find out the formats.
						RTPControl ctl = (RTPControl)ds.getControl("javax.media.rtp.RTPControl");
						System.err.println("  - The previously unidentified stream ");
						if (ctl != null)
						System.err.println("      " + ctl.getFormat());
						System.err.println("      had now been identified as sent by: " + participant.getCNAME());
					}
				}
			else
				if (evt instanceof ByeEvent)
				{
				    System.err.println("  - Got \"bye\" from: " + participant.getCNAME());
					PlayerWindow pw = find(stream);
				    if (pw != null)
					{
						pw.close();
						playerWindows.removeElement(pw);
					}
				}
	}//end of function update
    /**
     * ControllerListener for the Players.
     */
    public synchronized void controllerUpdate(ControllerEvent ce)
	{
		Player p = (Player)ce.getSourceController();
		if (p == null)
		    return;
		// Get this when the internal players are realized.
		if (ce instanceof RealizeCompleteEvent)
		{
			PlayerWindow pw = find(p);
			if (pw == null)
			{
				// Some strange happened.
				System.err.println("Internal error!");
				System.exit(-1);
			}
			pw.initialize();
		    pw.setVisible(true);
		    p.start();
		}

		if (ce instanceof ControllerErrorEvent)
		{
			p.removeControllerListener(this);
			PlayerWindow pw = find(p);
			if (pw != null)
			{
				pw.close();
				playerWindows.removeElement(pw);
			}
		 System.err.println("AVReceive2 internal error: " + ce);
		}
    }//end of function controller update
    /**
     * A utility class to parse the session addresses.
     */
class SessionLabel
{
	public String addr = null;
	public int port;
	public int ttl = 1;
	SessionLabel(String session) throws IllegalArgumentException
	{
	    int off;
	    String portStr = null, ttlStr = null;
	    if (session != null && session.length() > 0)
		{
			while (session.length() > 1 && session.charAt(0) == '/')
		    session = session.substring(1);
			// Now see if there's a addr specified.
			off = session.indexOf('/');
			if (off == -1)
			{
				if (!session.equals(""))
				addr = session;
			}
			else
			{
				addr = session.substring(0, off);
				session = session.substring(off + 1);
				// Now see if there's a port specified
				off = session.indexOf('/');
				if (off == -1)
				{
					if (!session.equals(""))
					portStr = session;
				}
				else
				{
					portStr = session.substring(0, off);
					session = session.substring(off + 1);
					// Now see if there's a ttl specified
					off = session.indexOf('/');
					if (off == -1)
					{
						if (!session.equals(""))
						ttlStr = session;
					}
					else
					{
						ttlStr = session.substring(0, off);
					}
			    }//end of else
			}//end of else
	    }//end of if
	    if (addr == null)
		throw new IllegalArgumentException();
	    if (portStr != null)
		{
			try
			{
			Integer integer = Integer.valueOf(portStr);
			if (integer != null)
			port = integer.intValue();
			}
			catch (Throwable t)
			{
			throw new IllegalArgumentException();
			}
	    }
		else
			throw new IllegalArgumentException();
	    if (ttlStr != null)
		{
			try
			{
			Integer integer = Integer.valueOf(ttlStr);
			if (integer != null)
			ttl = integer.intValue();
			}
			catch (Throwable t)
			{
			throw new IllegalArgumentException();
			}
	    }//end of if
	}//end of constructor
}//end of class
/**
* GUI classes for the Player.
*/
class PlayerWindow extends Frame
{
	Player player;
	ReceiveStream stream;
	PlayerWindow(Player p, ReceiveStream strm)
	{
	    player = p;
	    stream = strm;
	}
	public void initialize()
	{
	    add(new PlayerPanel(player));
	}
	public void close()
	{
	    player.close();
	    setVisible(false);
	    dispose();
	}
	public void addNotify()
	{
	    super.addNotify();
	    pack();
	}
}//end of PlayerWindow
/**
 * GUI classes for the Player.
 */
class PlayerPanel extends Panel
{
	Component vc, cc;
	PlayerPanel(Player p)
	{
	    setLayout(new BorderLayout());
	    if ((vc = p.getVisualComponent()) != null)
		add("Center", vc);
	    if ((cc = p.getControlPanelComponent()) != null)
		add("South", cc);
	}
	public Dimension getPreferredSize()
	{
	    int w = 0, h = 0;
	    if (vc != null)
		{
			Dimension size = vc.getPreferredSize();
			w = size.width;
			h = size.height;
	    }
	    if (cc != null)
		{
			Dimension size = cc.getPreferredSize();
			if (w == 0)
		    w = size.width;
			h += size.height;
	    }
	    if (w < 160)
		w = 160;
	    return new Dimension(w, h);
	}//end of Dimension
}//end of class PlayerPanel

//main program
    public static void controller()
	{
		String argv[] = null;
		if (argv.length == 0)
        {
			//prUsage();
            String arg[] = new String[1];
            arg[0]=" localhost/5000";//192.168.254.255/5000";
            // provide your ip address, use 255 at the end for multicast
            argv = arg;
        }
		AVReceive2 avReceive = new AVReceive2(argv);
		System.out.println(1);
		if (!avReceive.initialize())
		{
			System.err.println("Failed to initialize the sessions.");
			System.exit(-1);
		}
		// Check to see if AVReceive2 is done.
		try
		{
		while (!avReceive.isDone())
		Thread.sleep(1000);
		}
		catch (Exception e) {}
		System.err.println("Exiting AVReceive2");
    }
    static void prUsage()
	{
		System.err.println("Usage: AVReceive2 <session> <session> ...");
		System.err.println("     <session>: <address>/<port>/<ttl>");
		System.exit(0);
    }

	}// end of main program




