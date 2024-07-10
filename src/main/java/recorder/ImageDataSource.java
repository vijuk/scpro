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
/**
	 * A DataSource to read from a list of JPEG image files and
	 * turn that into a stream of JMF buffers.
	 * The DataSource is not seekable or positionable.
	 */
	public class ImageDataSource extends PullBufferDataSource {
		ImageSourceStream streams[];
		public ImageDataSource(int width, int height, int frameRate, Vector images) {
			streams = new ImageSourceStream[1];
			streams[0] = new ImageSourceStream(width, height, frameRate, images);
		}
		public void setLocator(MediaLocator source) {
		}
		public MediaLocator getLocator() {
			return null;
		}

		/**
		 * Content type is of RAW since we are sending buffers of video
		 * frames without a container format.
		 */
		public String getContentType() {
			return ContentDescriptor.RAW;
		}
		public void connect() {
		}
		public void disconnect() {
		}
		public void start() {
                    ((ImageSourceStream)streams[0]).start(true);
		}
		public void stop() {
                    ((ImageSourceStream)streams[0]).start(false);
		}

		/**
		 * Return the ImageSourceStreams.
		 */
		public PullBufferStream[] getStreams() {
			return streams;
		}

		/**
		 * We could have derived the duration from the number of
		 * frames and frame rate.  But for the purpose of this program,
		 * it's not necessary.
		 */
		public Time getDuration() {
			return DURATION_UNKNOWN;
		}
		public Object[] getControls() {
			return new Object[0];
		}
		public Object getControl(String type) {
			return null;
		}
	}
