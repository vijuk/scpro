package recorder;

import java.io.*;
import java.util.*;
import java.awt.Dimension;
import javax.media.*;
import javax.media.control.*;
import javax.media.protocol.*;
import javax.media.datasink.*;
import javax.media.format.VideoFormat;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import com.sun.image.codec.jpeg.*;

public class Recorder implements ControllerListener, DataSinkListener, Runnable {
    private static java.util.ResourceBundle resscreencam = java.util.ResourceBundle.getBundle("screencam"); //$NON-NLS-1$
    static int maxFrameCount = Integer.parseInt(resscreencam.getString("MaxFrameCount")); //$NON-NLS-1$
    DataSource ids = null;
    
    public DataSource getDataSource() {
        return ids;
    }
    
    Object waitSync = new Object();
    boolean stateTransitionOK = true;
    Object waitFileSync = new Object();
    boolean fileDone = false;
    boolean fileSuccess = true;
    
    ///////////////////////////////////////////////
    //
    // Inner classes.
    ///////////////////////////////////////////////
    
    
    
    
    
    public volatile boolean m_Recording = false;
    protected java.lang.Thread thread = null;
    private final static int RECORD_THREAD_PRIORITY = 8;
    private String m_OutputURL;
    private int m_Width;
    private int m_Height;
    private int m_FrameRate;
    /**
     * Insert the method's description here.
     * Creation date: (11/25/00 7:41:57 PM)
     */
    public Recorder() {
        thread = new Thread(this, "Thread_Recorder");
        thread.setPriority(RECORD_THREAD_PRIORITY);
    }
    /**
     * Controller Listener.
     */
    synchronized public void controllerUpdate(ControllerEvent evt) {
        if (evt instanceof ConfigureCompleteEvent ||
        evt instanceof RealizeCompleteEvent ||
        evt instanceof PrefetchCompleteEvent) {
            synchronized (waitSync) {
                stateTransitionOK = true;
                waitSync.notifyAll();
            }
        } else if (evt instanceof ResourceUnavailableEvent) {
            synchronized (waitSync) {
                stateTransitionOK = false;
                waitSync.notifyAll();
            }
        } else if (evt instanceof EndOfMediaEvent) {
            try {
                evt.getSourceController().stop();
                evt.getSourceController().close();
            } catch (Exception e) {
                System.out.println("Exception :" + e); // Analyse the situation when it happens - viju
            }
            
        }
    }
    /**
     * Create the DataSink.
     */
    DataSink createDataSink(Processor p, MediaLocator outML) {
        
        DataSource ds;
        
        if ((ds = p.getDataOutput()) == null) {
            lib.Util.println("Something is really wrong: the processor does not have an output DataSource");//$NON-NLS-1$
            return null;
        }
        
        DataSink dsink;
        
        try {
            lib.Util.println("- create DataSink for: " + outML);//$NON-NLS-1$
            dsink = Manager.createDataSink(ds, outML);
            dsink.open();
        } catch (Exception e) {
            lib.Util.println("Cannot create the DataSink: " + e);//$NON-NLS-1$
            return null;
        }
        
        return dsink;
    }
    /**
     * Create a media locator from the given string.
     */
    static MediaLocator createMediaLocator(String url) {
        
        MediaLocator ml;
        
        if (url.indexOf(":") > 0 && (ml = new MediaLocator(url)) != null)//$NON-NLS-1$
            return ml;
        
        if (url.startsWith(File.separator)) {
            if ((ml = new MediaLocator("file:" + url)) != null)//$NON-NLS-1$
                return ml;
        } else {
            String file = "file:" + System.getProperty("user.dir") + File.separator + url;//$NON-NLS-2$//$NON-NLS-1$
            if ((ml = new MediaLocator(file)) != null)
                return ml;
        }
        
        return null;
    }
    /**
     * Event handler for the file writer.
     */
    public void dataSinkUpdate(DataSinkEvent evt) {
        
        if (evt instanceof EndOfStreamEvent) {
            synchronized (waitFileSync) {
                fileDone = true;
                waitFileSync.notifyAll();
            }
        } else if (evt instanceof DataSinkErrorEvent) {
            synchronized (waitFileSync) {
                fileDone = true;
                fileSuccess = false;
                waitFileSync.notifyAll();
            }
        }
    }
    public boolean doIt(int width, int height, int frameRate, Vector inFiles, MediaLocator outML) {
        try {
            //ids = javax.media.Manager.createDataSource(new MediaLocator("screen://0,0,160,120/10"));
            ids = new ImageDataSource(width, height, frameRate, inFiles);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("IDS = " + ids);
        /*Processor p;
         
        try {
            lib.Util.println("- create processor for the image datasource ...");//$NON-NLS-1$
            p = Manager.createProcessor(ids);
        } catch (Exception e) {
            lib.Util.println("Yikes!  Cannot create a processor from the data source.");//$NON-NLS-1$
            return false;
        }
         
        p.addControllerListener(this);
         
        // Put the Processor into configured state so we can set
        // some processing options on the processor.
        p.configure();
        if (!waitForState(p, p.Configured)) {
            lib.Util.println("Failed to configure the processor.");//$NON-NLS-1$
            return false;
        }
         
        // Set the output content descriptor to QuickTime.
        p.setContentDescriptor(new ContentDescriptor(FileTypeDescriptor.QUICKTIME));
         
        // Query for the processor for supported formats.
        // Then set it on the processor.
        TrackControl tcs[] = p.getTrackControls();
        Format f[] = tcs[0].getSupportedFormats();
        if (f == null || f.length <= 0) {
            lib.Util.println("The mux does not support the input format: " + tcs[0].getFormat());//$NON-NLS-1$
            return false;
        }
         
        tcs[0].setFormat(f[0]);
         
        lib.Util.println("Setting the track format to: " + f[0]);//$NON-NLS-1$
         
        // We are done with programming the processor.  Let's just
        // realize it.
        p.realize();
        if (!waitForState(p, p.Realized)) {
            lib.Util.println("Failed to realize the processor.");//$NON-NLS-1$
            return false;
        }
         
         
        // Now, we'll need to create a DataSink.
        DataSink dsink;
        if ((dsink = createDataSink(p, outML)) == null) {
            lib.Util.println("Failed to create a DataSink for the given output MediaLocator: " + outML);//$NON-NLS-1$
            return false;
        }
         
        dsink.addDataSinkListener(this);
        fileDone = false;
         
        lib.Util.println("start processing...");//$NON-NLS-1$
         
        // OK, we can now start the actual transcoding.
        try {
            p.start();
            dsink.start();
        } catch (IOException e) {
            lib.Util.println("IO error during processing");//$NON-NLS-1$
            return false;
        }
         
        // Wait for EndOfStream event.
        waitForFileDone();
         
        // Cleanup.
        try {
            dsink.close();
        } catch (Exception e) {}
        p.removeControllerListener(this);
         
        lib.Util.println("...done processing.");//$NON-NLS-1$
         */
        
        return true;
        
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (11/25/00 7:44:59 PM)
     * @return int
     */
    public final static int getRecordThreadPriority() {
        return RECORD_THREAD_PRIORITY;
    }
    /**
     * Insert the method's description here.
     * Creation date: (11/25/00 7:44:59 PM)
     * @return Thread
     */
    public Thread getThread() {
        return thread;
    }
    /**
     * Insert the method's description here.
     * Creation date: (11/12/00 8:55:42 PM)
     * @return boolean
     */
    public boolean isRecording() {
        return m_Recording;
    }
    public static void main(String args[]) {
        Recorder recorder = new Recorder();
        recorder.setParameters("movie.mov", 600,800, 1);//$NON-NLS-1$
        recorder.start();
    }
    static void prUsage() {
        System.exit(-1);
    }
    public void run() {
        if (!m_OutputURL.endsWith(".mov") && !m_OutputURL.endsWith(".MOV")) {//$NON-NLS-2$//$NON-NLS-1$
            lib.Util.println("The output file extension should end with a .mov extension");//$NON-NLS-1$
        }
        
        m_Recording = true;
        java.util.Date startDate = new java.util.Date();
        /*
        if (args.length == 0)
            prUsage();
         
        // Parse the arguments.
        int i = 0;
        int width = -1, height = -1, frameRate = 1;
        Vector inputFiles = new Vector();
        String outputURL = null;
         
        while (i < args.length) {
         
            if (args[i].equals("-w")) {
        i++;
        if (i >= args.length)
            prUsage();
        width = new Integer(args[i]).intValue();
            } else if (args[i].equals("-h")) {
        i++;
        if (i >= args.length)
            prUsage();
        height = new Integer(args[i]).intValue();
            } else if (args[i].equals("-f")) {
        i++;
        if (i >= args.length)
            prUsage();
        frameRate = new Integer(args[i]).intValue();
            } else if (args[i].equals("-o")) {
        i++;
        if (i >= args.length)
            prUsage();
        outputURL = args[i];
            } else {
        inputFiles.addElement(args[i]);
            }
            i++;
        }
         
        if (outputURL == null || inputFiles.size() == 0)
            prUsage();
         
        // Check for output file extension.
        if (!outputURL.endsWith(".mov") && !outputURL.endsWith(".MOV")) {
            lib.Util.println("The output file extension should end with a .mov extension");
            prUsage();
        }
         
        if (width < 0 || height < 0) {
            lib.Util.println("Please specify the correct image size.");
            prUsage();
        }
         
        // Check the frame rate.
        if (frameRate < 1)
            frameRate = 1;
         */
        
        /*outputURL = "movie.mov";
        width = 400;
        height = 300;
        frameRate = 1;*/
        
        Vector inputFiles = new Vector();
        
        // Generate the output media locators.
        MediaLocator oml;
        if ((oml = createMediaLocator(m_OutputURL)) == null) {
            lib.Util.println("Cannot build media locator from: " + m_OutputURL);//$NON-NLS-1$
            System.exit(0);
        }
        doIt(m_Width, m_Height, m_FrameRate, inputFiles, oml);
        lib.Util.println(startDate);
        lib.Util.println(new java.util.Date());
    }
    public static void saveImage(BufferedImage image, String fileName) throws Exception	{		FileOutputStream fileStream = new
    FileOutputStream(fileName);
    JPEGEncodeParam encodeParam = JPEGCodec.getDefaultJPEGEncodeParam(image);
    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fileStream);
    encoder.encode(image,encodeParam);
    }
    public void setParameters(String outputURL, int width, int height, int frameRate) {
        if (!outputURL.endsWith(".mov") && !outputURL.endsWith(".MOV")) {//$NON-NLS-2$//$NON-NLS-1$
            lib.Util.println("The output file extension should end with a .mov extension");//$NON-NLS-1$
        }
        
        m_OutputURL = outputURL;
        m_Width= width;
        m_Height = height;
        m_FrameRate = frameRate;
    }
    /**
     * Insert the method's description here.
     * Creation date: (11/25/00 8:03:58 PM)
     */
    public void start() {
        thread.start();
    }
    public void stopRecording() {
        m_Recording = false;
        try {
            ids.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Block until file writing is done.
     */
    boolean waitForFileDone() {
        synchronized (waitFileSync) {
            try {
                while (!fileDone)
                    waitFileSync.wait();
            } catch (Exception e) {}
        }
        return fileSuccess;
    }
    /**
     * Block until the processor has transitioned to the given state.
     * Return false if the transition failed.
     */
    boolean waitForState(Processor p, int state) {
        synchronized (waitSync) {
            try {
                while (p.getState() < state && stateTransitionOK)
                    waitSync.wait();
            } catch (Exception e) {}
        }
        return stateTransitionOK;
    }
}
