 
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
 
        //private MjpegView mv;
		private int zoomValue = 0;
       
 
        public void onCreate(Bundle icicle) {
 
        super.onCreate(icicle);
        //sample public ca
        //String URL = "http://plazacam.studentaffairs.duke.edu/mjpg/video.mjpg";
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
 
        //                   WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_streaming);
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
 
        //setContentView(mv);        
 
//        mv.setSource(MjpegInputStream.read(URL));
 //
   //     mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
 
     //   mv.showFps(true);
 
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
                	//GetRequest("http://192.168.0.90/axis-cgi/com/ptz.cgi?camera=1&rzoom=" + zoomValue);
                	GetRequest("http://192.168.0.90/?lightoff");
       
                }
            });
            btnZin.setOnClickListener(new OnClickListener(){
                public void onClick(View arg)
                {
                	TextView txt = (TextView)findViewById(R.id.TextView);
                	txt.setText("Zoom in");
                	zoomValue++;
                	//GetRequest("http://192.168.0.90/axis-cgi/com/ptz.cgi?camera=1&rzoom=" + zoomValue);
                	GetRequest("http://192.168.0.90/?lighton");
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
    					// execute(); executes a request using the default context.
    					// Then we assign the execution result to HttpResponse
    					HttpResponse httpResponse = httpClient.execute(httpGet);
    					// getContent() ; creates a new InputStream object of the entity.
    					// Now we need a readable source to read the byte stream that comes as the httpResponse
    					InputStream inputStream = httpResponse.getEntity().getContent();

    					// We have a byte stream. Next step is to convert it to a Character stream
    					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

    					// Then we have to wraps the existing reader (InputStreamReader) and buffer the input
    					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

    					// InputStreamReader contains a buffer of bytes read from the source stream and converts these into characters as needed.
    					//The buffer size is 8K
    					//Therefore we need a mechanism to append the separately coming chunks in to one String element
    					// We have to use a class that can handle modifiable sequence of characters for use in creating String
    					//StringBuilder stringBuilder = new StringBuilder();

    					//String bufferedStrChunk = null;

    					// There may be so many buffered chunks. We have to go through each and every chunk of characters
    					//and assign a each chunk to bufferedStrChunk String variable
    					//and append that value one by one to the stringBuilder
    					//while((bufferedStrChunk = bufferedReader.readLine()) != null){
    						//stringBuilder.append(bufferedStrChunk);
    					//}

    					// Now we have the whole response as a String value.
    					//We return that value then the onPostExecute() can handle the content

    					// If the Username and Password match, it will return "working" as response
    					// If the Username or Password wrong, it will return "invalid" as response					
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
                // onPostExecute displays the results of the AsyncTask.
        	HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
    		// Parameter we pass in the execute() method is relate to the first generic type of the AsyncTask
    		// We are passing the connectWithHttpGet() method arguments to that
    		httpGetAsyncTask.execute(url); 
               
        	
        }
        
        
        

 
        // public void onPause() {
        //       super.onPause();
        //     mv.stopPlayback();
        //       }
        
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.streaming, menu);
            return true;
        } 
 
}

