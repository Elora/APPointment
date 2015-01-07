package kb50.appointment;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class ApiConnector {

	
	HttpEntity httpEntity = null;
	
	public JSONArray getTable(String url) {
		
		try{
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			httpEntity = httpResponse.getEntity();
		}
		catch(ClientProtocolException e){
			e.printStackTrace();
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	
	JSONArray jsonArray = null;
	
	if(httpEntity != null){
		
		try{
			String entityResponse = EntityUtils.toString(httpEntity);
			Log.e("Entity response : ",entityResponse);
			
			jsonArray = new JSONArray(entityResponse);
			
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	return jsonArray;
	}
	
}

