package kb50.appointment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//Source: Groep 3 

public class AddImageActivity extends Activity{

	
	Uri currImageURI;
	String image_name;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addimg);
		
		//select button
		Button select_btn = (Button) this.findViewById(R.id.select);
		select_btn.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View view) {
				// To open up a gallery browser
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
			}});
        
		//upload button
		Button upload_btn = (Button) this.findViewById(R.id.upload);
		upload_btn.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View view) {
				// To upload to webserver
				dialog = ProgressDialog.show(getApplicationContext(), "", "Uploading file...", true);
                new Thread(new Runnable() {
                       public void run() {                    
                        int response= uploadFile(getRealPathFromURI(currImageURI));
                        System.out.println("RES : " + response);                         
                       }
                     }).start();
				}
			});
		}
	
	// To handle when an image is selected from the browser
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	        if (requestCode == 1) {
	        // currImageURI is the global variable I’m using to hold the content:
	            currImageURI = data.getData();
	            System.out.println("Current image Path is ----->" + getRealPathFromURI(currImageURI));
	            TextView tv_path = (TextView) findViewById(R.id.path);
	            tv_path.setText(getRealPathFromURI(currImageURI));
	        }
	    }
	}
	 
	//Convert the image URI to the direct file system path of the image file
	public String getRealPathFromURI(Uri contentUri) {
	    String [] proj={MediaStore.Images.Media.DATA};
	    @SuppressWarnings("deprecation")
		android.database.Cursor cursor = managedQuery( contentUri,
	    proj,     // Which columns to return
	    null,     // WHERE clause; which rows to return (all rows)
	    null,     // WHERE clause selection arguments (none)
	    null);     // Order-by clause (ascending by name)
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public int uploadFile(String sourceFileUri) {
        String upLoadServerUri = "http://hhskb50.cf/upload_media_test.php";
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(sourceFileUri); 
        if (!sourceFile.isFile()) {
         Log.e("uploadFile", "Source File Does not exist");
         return 0;
        }
            try { // open a URL connection to the Servlet
             FileInputStream fileInputStream = new FileInputStream(sourceFile);
             URL url = new URL(upLoadServerUri);
             conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
             conn.setDoInput(true); // Allow Inputs
             conn.setDoOutput(true); // Allow Outputs
             conn.setUseCaches(false); // Don't use a Cached Copy
             conn.setRequestMethod("POST");
             conn.setRequestProperty("Connection", "Keep-Alive");
             conn.setRequestProperty("ENCTYPE", "multipart/form-data");
             conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
             conn.setRequestProperty("uploaded_file", fileName); 
             dos = new DataOutputStream(conn.getOutputStream());
   
             dos.writeBytes(twoHyphens + boundary + lineEnd); 
             dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
             dos.writeBytes(lineEnd);
   
             bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
   
             bufferSize = Math.min(bytesAvailable, maxBufferSize);
             buffer = new byte[bufferSize];
   
             // read file and write it into form...
             bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
               
             while (bytesRead > 0) {
               dos.write(buffer, 0, bufferSize);
               bytesAvailable = fileInputStream.available();
               bufferSize = Math.min(bytesAvailable, maxBufferSize);
               bytesRead = fileInputStream.read(buffer, 0, bufferSize);               
              }
   
             // send multipart form data necesssary after file data...
             dos.writeBytes(lineEnd);
             dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
   
             // Responses from the server (code and message)
             serverResponseCode = conn.getResponseCode();
             String serverResponseMessage = conn.getResponseMessage();
              
             Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
             if(serverResponseCode == 200){
                 runOnUiThread(new Runnable() {
                      public void run() {
                          Toast.makeText(getApplicationContext(), "File Upload Complete.", Toast.LENGTH_SHORT).show();
                      }
                  });                
             }    
             
             //close the streams //
             fileInputStream.close();
             dos.flush();
             dos.close();
              
        } catch (MalformedURLException ex) {  
            dialog.dismiss();  
            ex.printStackTrace();
            Toast.makeText(this, "MalformedURLException", Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
        } catch (Exception e) {
            dialog.dismiss();  
            e.printStackTrace();
            Toast.makeText(this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);  
        }
        dialog.dismiss();       
        return serverResponseCode;  
       } 

	
	
}
