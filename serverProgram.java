import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* This is a <b> Server Class </b>
* It creates a serversocket and waits for clients request in an infinite loop
* when a client request a connection it opens up a new thread with the client
* socket as argument
*/
public class serverProgram
{
	public static void main(String arg[])
	{
		ServerSocket server = null;
		boolean isListening = true;
		int i = 1;
		try
		{
			server = new ServerSocket( 3000);
			System.out.println("Connected to port 3000 of local host..");
			while(isListening)
				new ServerWindow(server.accept()).start();
			server.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

/**
* This is <b>serverThread class</b>
* It's constructor receives a socket from ServerProgram and assigns it to the 
* socket of this class
* It extends the Thread class and implements the run() method
*/
class  serverThread extends Thread
{
	private Socket socket= null;
	/**
	* The constructor names the thread and assigns this.socket = socket
	*/
	public serverThread(Socket socket)
	{
		super("serverThread");
		this.socket= socket;
	}
	/**
	* The run method 
	*/
	public void run()
	{
		try
		{
		System.out.println("Received connection from client..:");
		System.out.println(socket);
		PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String sInputLine, sOutputLine;
		sInputLine = in.readLine();
		System.out.println(sInputLine);
		while((sInputLine  != null))
		{
        
				sOutputLine =processInput(sInputLine);
				out.println(sOutputLine);
				System.out.println("Server Says  :"+sOutputLine);
				if(sOutputLine.equals("Bye"))
					break;
				sInputLine = null;
		}
		out.close();
		in.close();
		socket.close();
		}
		catch(Exception e)
		{
		}
		
	}

	/**
	* Method - processInput()
	* This method processes the input from the client and receives a message from 
	* the server in reply.
	* @return String
	*/
	public String processInput(String sInput)
	{
		String sReply = null;
		BufferedReader bReply = null;
		try
		{
		bReply = new BufferedReader(new InputStreamReader (System.in));
		System.out.println("Client Says  :"+sInput);
		sReply = bReply.readLine();
		}
		catch(Exception e)
		{
		}
		return(sReply);
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
		type = new TextArea(20,40);
		tType = new TextField(44);
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
			sInputLine = in.readLine();
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
			type.append("Client says :" + sInput +"\n");
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