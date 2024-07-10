import javax.media.*;
import javax.media.Manager;
import javax.media.format.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import javax.media.protocol.*;
import javax.media.control.*;
import javax.media.ProcessorModel.*;
import javax.media.datasink.*;

public class  captureAudio
{
	public static void main(String arg[])
	{
		audioDevice au = new  audioDevice();
		au.capture();
		au.createProcessor();
                while (true)
                {
                    try
                    {
                    Thread.currentThread().sleep(100);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
	}
}

//***************************Audio Device Detection Class 

class audioDevice  implements ControllerListener, DataSinkListener
{
	AudioFormat format ;
	Vector devices;
	CaptureDeviceInfo di = null;
	Processor processor;
//--------------------------------Audio Device Detection------------------------------
	public void capture()
	{
		//create  a required format to get the  corresponding capture device.
		 AudioFormat format= new AudioFormat(AudioFormat.LINEAR, 
                                             8000, 
                                             8, 
                                             2); 
 
                //System.out.println(CaptureDeviceManager.getDevice("javasound://44100"));
                devices= CaptureDeviceManager.getDeviceList( format);
                System.out.println(devices.size());
                
//vector array to store the list of the   supported devices.
		//import   java.util;
		//CaptureDeviceInfo is a class which stores all the information of the device.

		if (devices.size()>0)
		{
			System.out.println("Device found in the system.....");
			di = (CaptureDeviceInfo) devices.elementAt(0);
                        System.out.println(di);
		}
		else
		{
		    System.out.println("No device found in the system....");
		    System.exit(-1);
		}
	}
//------------------------------------------------------------------------------------


//--------------------------------createProcessor------------------------------------
	public   void createProcessor()
	{
		try
		{	//Manager.createProcessor(MediaLocator)
			processor=Manager.createProcessor(di.getLocator());
                        processor.addControllerListener(this);
		}
		catch(IOException e)
		{
			System.out.println("IOException in creating processor...");
			System.exit(-1);
		}
		catch(Exception e)
		{
			System.out.println(e);
			System.exit(-1);
		}
		//we have succesfully created a processor .Now realize it and  block it
		//until it is configured...
		System.out.println("Processor creation successfull..");

                
		processor.configure();
 
                 try
                {
                    System.out.println("Waiting for configuration");
                Thread.currentThread().sleep(100);
                 } catch (Exception e)
                {
                    e.printStackTrace();
                }
                                
                                
		System.out.println("d");

		//block until the processor is configured


		try
		{
                    	//import javax.media.control.*;
			TrackControl track[] = processor.getTrackControls();
			System.out.println("processor configuration successful...");
		}
		catch(Exception e)
		{
                        e.printStackTrace();
			System.out.println("exeptions ... here ...");
		}
        processor.setContentDescriptor( 
            new ContentDescriptor( ContentDescriptor.RAW));
         
        TrackControl track[] = processor.getTrackControls(); 
        
        boolean encodingOk = false;
        
        // Go through the tracks and try to program one of them to
        // output gsm data. 
        
         for (int i = 0; i < track.length; i++) { 
             if (!encodingOk && track[i] instanceof FormatControl) {  
 
                 if (((FormatControl)track[i]).
                     setFormat( new AudioFormat(AudioFormat.GSM_RTP, 
                                                8000, 
                                                8, 
                                                1)) == null) {
 
                    track[i].setEnabled(false); 
                 }
                 else {
                     encodingOk = true; 
                 }
             } else { 
                 // we could not set this track to gsm, so disable it 
                 track[i].setEnabled(false); 
             } 
         }
         
         // At this point, we have determined where we can send out 
         // gsm data or not. 
         // realize the processor 
         if (encodingOk) { 
             processor.realize(); 
             
             try
                {
                    System.out.println("Waiting for realization");
                Thread.currentThread().sleep(1000);
                 } catch (Exception e)
                {
                    e.printStackTrace();
                }
             
             // block until realized. 
             // get the output datasource of the processor and exit 
             // if we fail 
             DataSource ds = null;
             
             try { 
                 ds = processor.getDataOutput(); 
                 
             } catch (NotRealizedError e) { 
                 System.exit(-1);
             }
 
             // hand this datasource to manager for creating an RTP 
             // datasink our RTP datasimnk will multicast the audio 
             try {
                 /*
                 String url= "rtp://127.0.0.1:49150/audio/1";
                 MediaLocator m = new MediaLocator(url);
                 DataSink d = Manager.createDataSink(ds, m);
                 d.addDataSinkListener(this);
                 d.open();
                 System.out.println("Sink Starting");
                 d.start();
                */
                 System.out.println("Sink Started");
                 Player player = null;
             	    try {
System.out.println("creating player");
                                player = Manager.createPlayer(ds);
                                player.addControllerListener(this);
                                player.realize();
                                
                System.out.println("Player = " + player);

	    } catch (NoPlayerException e) {
		System.out.println(e);
		//System.out.println("Could not create player for " + url);
	    }

	    // Add ourselves as a listener for a player's events
            System.out.println("Starting Player");

            player.start();
            
            //ds.connect();
            //ds.start();
         
           //   processor.getDataOutput().start();
            processor.start();

                 System.out.println("Player Started");
                 
                 
             } catch (Exception e) {
                 e.printStackTrace();
                 System.exit(-1);
             }     
         }   
 
		
                System.out.println("djf");
 	}
        
        public void controllerUpdate(javax.media.ControllerEvent controllerEvent) {
            System.out.println(controllerEvent.toString());
        }
        
        public void dataSinkUpdate(javax.media.datasink.DataSinkEvent dataSinkEvent) {
            System.out.println("DL : " + dataSinkEvent.toString());
        }
        
}

