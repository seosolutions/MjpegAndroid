 
package com.streaming.MjpegView;
 
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
 
 
 
public class MjpegInputStream extends DataInputStream {
 
    private final byte[] SOI_MARKER = { (byte) 0xFF, (byte) 0xD8 };
    private final byte[] EOF_MARKER = { (byte) 0xFF, (byte) 0xD9 };
    private final String CONTENT_LENGTH = "Content-Length";
    private final static int HEADER_MAX_LENGTH = 100;
    private final static int FRAME_MAX_LENGTH = 200000; //+ HEADER_MAX_LENGTH;
    private int mContentLength = -1;
    byte[] header =null;
    byte[] frameData =null;
    int headerLen = -1;
    int headerLenPrev = -1;
    
    public native void freeCameraMemory();
    
    public static MjpegInputStream read(String surl) {
 
    	try {
    		URL url = new URL(surl);
    		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    		return new MjpegInputStream(urlConnection.getInputStream());
    	}catch(Exception e){}
    	
        return null;
    }
   
    public MjpegInputStream(InputStream in) {
 
        super(new BufferedInputStream(in, FRAME_MAX_LENGTH));
 
    }
        
    private int getEndOfSeqeunce(DataInputStream in, byte[] sequence)
 
        throws IOException
 
    {
 
        int seqIndex = 0;
 
        byte c;
 
        for(int i=0; i < FRAME_MAX_LENGTH; i++) {
 
            c = (byte) in.readUnsignedByte();
 
            if(c == sequence[seqIndex]) {
 
                seqIndex++;
 
                if(seqIndex == sequence.length) return i + 1;
 
            } else seqIndex = 0;
 
        }
 
        return -1;
 
    }
      
    private int getStartOfSequence(DataInputStream in, byte[] sequence)
 
        throws IOException
 
    {
 
        int end = getEndOfSeqeunce(in, sequence);
 
        return (end < 0) ? (-1) : (end - sequence.length);
 
    }
 
    private int parseContentLength(byte[] headerBytes)
 
        throws IOException, NumberFormatException
 
    {
 
        ByteArrayInputStream headerIn = new ByteArrayInputStream(headerBytes);
 
        Properties props = new Properties();
 
        props.load(headerIn);
 
        return Integer.parseInt(props.getProperty(CONTENT_LENGTH));
 
    }  
 
    public Bitmap readMjpegFrame() throws IOException {
 
        mark(FRAME_MAX_LENGTH);
        headerLen = getStartOfSequence(this, SOI_MARKER);
        reset();
        header = new byte[headerLen];
        headerLenPrev = headerLen;
        readFully(header);
        try {
 
            mContentLength = parseContentLength(header);
 
        } catch (NumberFormatException nfe) {
 
            mContentLength = getEndOfSeqeunce(this, EOF_MARKER);
 
        }
 
        reset();
        frameData = new byte[mContentLength];
        skipBytes(headerLen);
        readFully(frameData);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(frameData));
    }
 
}
 
 
