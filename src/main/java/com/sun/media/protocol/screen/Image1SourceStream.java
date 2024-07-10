package com.sun.media.protocol.screen;

/*
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
*/

import com.sun.image.codec.jpeg.*;
import java.util.*;
import java.io.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.media.*;
import javax.media.format.*;
import javax.media.protocol.*;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * The source stream to go along with ImageDataSource.
 */
public class Image1SourceStream implements PushBufferStream, Runnable {

    protected ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW);
    protected int maxDataLength;
    protected int [] data;
    protected Dimension size;
    protected VideoFormat format;
    protected boolean started;
    protected Thread thread;
    protected float frameRate = 1f;
    protected BufferTransferHandler transferHandler;
    protected Control [] controls = new Control[0];
    protected int x, y, width, height;

    protected Robot robot = null;
    int seqNo = 0;
 
    
    /*
    public boolean m_Recording = false;
    int x,y,width,height;
    int frameRate=1;
    Thread thread;
    
    VideoFormat format;
    int nextImage = 0; // index of the next image to be read.
    boolean ended = false; */
    
    public Image1SourceStream(int width, int height, int frameRate, java.util.Vector images) {
        this.width = width;
        this.height = height;
        format = new VideoFormat(VideoFormat.JPEG, new Dimension(width, height), Format.NOT_SPECIFIED, Format.byteArray, (float) frameRate);
    }
    
    /**
     * We should never need to block assuming data are read from files.
     */
    public boolean willReadBlock() {
        return false;
    }
    
    /**
     * This is called from the Processor to read a frame worth
     * of video data.
     */
    /*
    public void readJPEG(Buffer buf) throws IOException {
        
        // Check if we've finished all the frames.
        if (nextImage >= images.size()) {
            // We are done.  Set EndOfMedia.
            lib.Util.println("Recording Stopped."); //$NON-NLS-1$
            buf.setEOM(true);
            buf.setOffset(0);
            buf.setLength(0);
            ended = true;
            return;
        }
        String imageFile = (String) images.elementAt(nextImage);
        nextImage++;
        lib.Util.println("  - reading image file: " + imageFile); //$NON-NLS-1$
        
        // Open a random access file for the next image.
        RandomAccessFile raFile;
        raFile = new RandomAccessFile(imageFile, "r"); //$NON-NLS-1$
        
        byte data[] = null;
        
        // Check the input buffer type & size.
        
        if (buf.getData() instanceof byte[])
            data = (byte[]) buf.getData();
        
        // Check to see the given buffer is big enough for the frame.
        if (data == null || data.length < raFile.length()) {
            data = new byte[ (int) raFile.length()];
            buf.setData(data);
        }
        
        // Read the entire JPEG image from the file.
        raFile.readFully(data, 0, (int) raFile.length());
        lib.Util.println("    read " + raFile.length() + " bytes."); //$NON-NLS-2$//$NON-NLS-1$
        
        buf.setOffset(0);
        buf.setLength((int) raFile.length());
        buf.setFormat(format);
        buf.setFlags(buf.getFlags() | buf.FLAG_KEY_FRAME);
        
        // Close the random access file.
        raFile.close();
    }
    */
    
    /**
     * This is called from the Processor to read a frame worth
     * of video data.
     */
    public void read(Buffer buffer) throws java.io.IOException {
        System.out.println("Inside read...");
        synchronized (this) {
	    Object outdata = buffer.getData();
	    if (outdata == null || !(outdata.getClass() == Format.intArray) ||
		((int[])outdata).length < maxDataLength) {
		outdata = new int[maxDataLength];
		buffer.setData(outdata);
	    }
            
	    ByteArrayOutputStream stream = null;
            try {
                BufferedImage image = getScreenImage();
                //stream = getByteStream(image);
                image.getRGB(0, 0, width, height,
		      (int[])outdata, 0, width);
	    } catch (Exception e) {
                lib.Util.println("Exception occured : " + e); //$NON-NLS-1$
            }
            byte data[] = stream.toByteArray();
            buffer.setData(data);
            
            buffer.setFormat( format );
	    buffer.setTimeStamp( (long) (seqNo * (1000 / frameRate) * 1000000) );
            buffer.setSequenceNumber( seqNo );
	    seqNo++;
            
            int length = data.length;
            buffer.setOffset(0);
            buffer.setLength((int) length);
            buffer.setFlags(buffer.getFlags() | buffer.FLAG_KEY_FRAME);
            buffer.setHeader( null );	    
            System.out.println("Captured screen: " + length);
        }
/*	    BufferedImage bi = robot.createScreenCapture(
		new Rectangle(x, y, width, height));
	    bi.get(0, 0, width, height,
		      (int[])outdata, 0, width);
	    buffer.setLength(maxDataLength);
	    buffer.setFlags(Buffer.FLAG_KEY_FRAME);
	    buffer.setHeader( null );	    
	}
        
        try {
            // Check if we've finished all the frames.
            //if (frameCount >= maxFrameCount) {
            if (started) {
                System.out.println("Recording is done :" + m_Recording + "," + frameCount);
                // We are done.  Set EndOfMedia.
                lib.Util.println("Done reading all images."); //$NON-NLS-1$
                buf.setEOM(true);
                buf.setOffset(0);
                buf.setLength(0);
                ended = true;
                return;
            }
            frameCount++;
            lib.Util.println(" - capturing frame: " + frameCount); //$NON-NLS-1$
            
            // Close the random access file.
            // raFile.close();
        } catch (Exception e) {
            lib.Util.println("Exception : " + e); //$NON-NLS-1$
        }
        
        
        try {
            Thread.sleep(100); // Added to improve mouse response time: viju
        }
        catch (Exception e) {
            lib.Util.println("Exception : " + e); //$NON-NLS-1$
        } */
        System.out.println("exiting read...");
    }
    
    /**
     * Return the format of each video frame.  That will be JPEG.
     */
    public Format getFormat() {
        return format;
    }
    public ContentDescriptor getContentDescriptor() {
        return cd;
    }
    public long getContentLength() {
        return LENGTH_UNKNOWN;
    }
    public boolean endOfStream() {
        return false;
    }

    // Controls
    
    public Object [] getControls() {
	return controls;
    }

    public Object getControl(String controlType) {
       try {
          Class  cls = Class.forName(controlType);
          Object cs[] = getControls();
          for (int i = 0; i < cs.length; i++) {
             if (cls.isInstance(cs[i]))
                return cs[i];
          }
          return null;

       } catch (Exception e) {   // no such controlType or such control
         return null;
       }
    }
    
    public ByteArrayOutputStream getByteStream(BufferedImage image) throws Exception	{
        
         ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        JPEGEncodeParam encodeParam = JPEGCodec.getDefaultJPEGEncodeParam(image);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(byteStream);

        encoder.encode(image,encodeParam);
        return byteStream;
    }
    
    public BufferedImage getScreenImage() throws Exception {
        //Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage screen = robot.createScreenCapture(new Rectangle(0,0,width,height));
        /*
         MediaTracker tracker = new MediaTracker(new Label());
        tracker.addImage(screen,0);
        try {
            tracker.waitForID(0);
        }
        catch(InterruptedException e) {
            }
         **/
        
        return screen;
    }
    
    public void run() {
        System.out.println("Inside run..." + started);
        while (started) {
	    synchronized (this) {
		while (transferHandler == null && started) {
		    try {
System.out.println("Inside run...1" + started);
        			wait(1000);
System.out.println("Inside run...2" + started);
		    } catch (InterruptedException ie) {
                        ie.printStackTrace();
		    }
		} // while
	    }

	    if (started && transferHandler != null) {
		transferHandler.transferData(this);
		try {
System.out.println("Inside run...3" + started);
		    Thread.currentThread().sleep( 10 );
		} catch (InterruptedException ise) {
                    ise.printStackTrace();
		}
	    }
	} // while (started)
        System.out.println("Inside run...4" + started);

    }
    public void start(boolean started) {
	synchronized ( this ) {
	    this.started = started;
	    if (started && !thread.isAlive()) {
		thread = new Thread(this);
                System.out.println("Starting thread...");
		thread.start();
	    }
	    notifyAll();
	}
    }

    public Image1SourceStream(MediaLocator locator) {
	try {
	    parseLocator(locator);
	} catch (Exception e) {
	    System.err.println(e);
	}
	//size = Toolkit.getDefaultToolkit().getScreenSize();
	try {
	    robot = new Robot();
	} catch (AWTException awe) {
	    throw new RuntimeException("");
	}
	maxDataLength = size.width * size.height * 3;
        //format = new VideoFormat(VideoFormat.JPEG, new Dimension(width, height), Format.NOT_SPECIFIED, Format.byteArray, (float) frameRate);
        format = new RGBFormat(size, maxDataLength,
				  Format.intArray,
				  frameRate,
				  32,
				  0xFF0000, 0xFF00, 0xFF,
				  1, size.width,
				  VideoFormat.FALSE,
				  Format.NOT_SPECIFIED);
	
        /*Format = new Format(size, maxDataLength,
				  Format.intArray,
				  frameRate,
				  32,
				  0xFF0000, 0xFF00, 0xFF,
				  1, size.width,
				  VideoFormat.FALSE,
				  Format.NOT_SPECIFIED);
	*/
        
	// generate the data
	//data = new int[maxDataLength];
	thread = new Thread(this, "Screen Grabber");
    }
 protected void parseLocator(MediaLocator locator) {
	String rem = locator.getRemainder();
	// Strip off starting slashes
	while (rem.startsWith("/") && rem.length() > 1)
	    rem = rem.substring(1);
	StringTokenizer st = new StringTokenizer(rem, "/");
	if (st.hasMoreTokens()) {
	    // Parse the position
	    String position = st.nextToken();
	    StringTokenizer nums = new StringTokenizer(position, ",");
	    String stX = nums.nextToken();
	    String stY = nums.nextToken();
	    String stW = nums.nextToken();
	    String stH = nums.nextToken();
	    x = Integer.parseInt(stX);
	    y = Integer.parseInt(stY);
	    width = Integer.parseInt(stW);
	    height = Integer.parseInt(stH);
	}
	if (st.hasMoreTokens()) {
	    // Parse the frame rate
	    String stFPS = st.nextToken();
	    frameRate = (Double.valueOf(stFPS)).floatValue();
	}
    }
 
 public void setTransferHandler(javax.media.protocol.BufferTransferHandler bufferTransferHandler) {
     	synchronized (this) {
	    this.transferHandler = transferHandler;
	    notifyAll();
	}
 }
 
}