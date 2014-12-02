 
package com.streaming;
 
 
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.streaming.MjpegView.MjpegInputStream;
import com.streaming.MjpegView.MjpegView;
 
public class Streaming extends Activity {
 
        private MjpegView mv = null;
		private int zoomValue = 0;
		String URL = "http://plazacam.studentaffairs.duke.edu/mjpg/video.mjpg";
		
	    private int width = 640;
	    private int height = 480;
		
     public void onCreate(Bundle icicle) {
 
        super.onCreate(icicle);
        
        SharedPreferences preferences = getSharedPreferences("SAVED_VALUES", MODE_PRIVATE);
        width = preferences.getInt("width", width);
        height = preferences.getInt("height", height);
             
        setContentView(R.layout.activity_streaming);
        //mv = (MjpegView) findViewById(R.id.mv);  
        //if(mv != null){
        	//mv.setResolution(width, height);
        //}
        TextView txtIsConnected = (TextView)findViewById(R.id.txtIsConnected);
        
        if(isConnected())
        {
        	txtIsConnected.setText("Connect");
        	addButtonClickListner();
        }
        else
        {
        	txtIsConnected.setText("Not Connect");
        }
        
        
        //mv = new MjpegView(this);
        //mv.setSource(MjpegInputStream.read(URL));
       // mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
 
 
        }
        
        public void addButtonClickListner()
        {
        	//requests
        	//http://192.168.0.90/axis-cgi/com/ptz.cgi?camera=1&move=left
        	//http://192.168.0.90/axis-cgi/com/ptz.cgi?camera=1&rzoom=
        	//http://192.168.0.90/mjpg/video.mjpg
        	//rtsptextrtsp://dummyurl/mpeg4/media.amp
            Button btnUP = (Button)findViewById(R.id.button1);
            Button btnDown = (Button)findViewById(R.id.button2);
            Button btnRight = (Button)findViewById(R.id.button3);
            Button btnLeft = (Button)findViewById(R.id.button4);
            Button btnZout = (Button)findViewById(R.id.button5);
            Button btnZin = (Button)findViewById(R.id.button6);
            
            btnUP.setOnClickListener(new OnClickListener(){
                public void onClick(View arg)
                {
                	TextView txt = (TextView)findViewById(R.id.TextView);
                	txt.setText("Up");
                	GetRequest("http://192.168.0.90/axis-cgi/com/ptz.cgi?camera=1&move=up");
                    //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://plazacam.studentaffairs.duke.edu/view/viewer_index.shtml?id=90261"));
                    //startActivity(intent);
                }
            });
            btnDown.setOnClickListener(new OnClickListener(){
                public void onClick(View arg)
                {
                	TextView txt = (TextView)findViewById(R.id.TextView);
                	txt.setText("Down");
                	GetRequest("http://192.168.0.90/axis-cgi/com/ptz.cgi?camera=1&move=down");
                }
            });
            btnRight.setOnClickListener(new OnClickListener(){
                public void onClick(View arg)
                {
                	TextView txt = (TextView)findViewById(R.id.TextView);
                	txt.setText("Right");
                	GetRequest("http://192.168.0.90/axis-cgi/com/ptz.cgi?camera=1&move=right");
                }
            });
            btnLeft.setOnClickListener(new OnClickListener(){
                public void onClick(View arg)
                {
                	TextView txt = (TextView)findViewById(R.id.TextView);
                	txt.setText("Left");
                	GetRequest("http://192.168.0.90/axis-cgi/com/ptz.cgi?camera=1&move=left");
                }
            });
            btnZout.setOnClickListener(new OnClickListener(){
                public void onClick(View arg)
                {
                	TextView txt = (TextView)findViewById(R.id.TextView);
                	txt.setText("Zoom out");
                	zoomValue--;
                	GetRequest("http://192.168.0.90/axis-cgi/com/ptz.cgi?camera=1&rzoom=" + zoomValue);
       
                }
            });
            btnZin.setOnClickListener(new OnClickListener(){
                public void onClick(View arg)
                {
                	TextView txt = (TextView)findViewById(R.id.TextView);
                	txt.setText("Zoom in");
                	zoomValue++;
                	GetRequest("http://192.168.0.90/axis-cgi/com/ptz.cgi?camera=1&rzoom=" + zoomValue);
                }
            });
            
        } 
        
        public boolean isConnected(){
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) 
                    return true;
                else
                    return false;   
        }
                
        public void GetRequest(String url)
        {
        	class HttpGetAsyncTask extends AsyncTask<String, Void, Void>{
        		@Override
                protected Void doInBackground(String... urls) {
        			String paramUrl = urls[0];
        			HttpClient httpClient = new DefaultHttpClient();
    				HttpGet httpGet = new HttpGet(paramUrl);
    				try {
    					HttpResponse httpResponse = httpClient.execute(httpGet);
    					InputStream inputStream = httpResponse.getEntity().getContent();
    					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);					
    					return null;

    				} catch (ClientProtocolException cpe) {
    					System.out.println("Exceptionrates caz of httpResponse :" + cpe);
    					cpe.printStackTrace();
    				} catch (IOException ioe) {
    					System.out.println("Secondption generates caz of httpResponse :" + ioe);
    					ioe.printStackTrace();
    				}

    				return null;
    			}
        			
             }
        	HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
    		httpGetAsyncTask.execute(url); 
               
        	
        }
        
        
 
        // public void onPause() {
        //       super.onPause();
        //     mv.stopPlayback();
        //       }
        
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.streaming, menu);
            return true;
        } 
 
}

