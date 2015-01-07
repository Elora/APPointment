package kb50.appointment;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends Activity {

	private EditText username;
	private EditText password;
	private final String TEMP_USER = "a";
	private final String TEMP_PASS = "a";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        
        username = (EditText) findViewById(R.id.usernameField);
        password = (EditText) findViewById(R.id.passwordField);
        
     
        
    }
    
    public void onClickLogin(View v){    	
    	String user = username.getText().toString();
    	String pass = password.getText().toString(); 
    	
    	if(checkCredentials(user, pass) == true){
    		startActivity(new Intent(LogIn.this, TabLayout.class));
    	}else{
			Context context = getApplicationContext();
			CharSequence text = "Wrong username or password";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
    	}
    }
    
    public void onClickReg(View v){
    	startActivity(new Intent(LogIn.this, RegisterActivity.class));
    }
    
    private boolean checkCredentials(String usern, String password){
    	// TODO replace with DB check
    	boolean passCheck = false;
    	   try {
   			List<Object> users = new Controller("http://eduweb.hhs.nl/~13061798/GetUsers.php").get();
   			for(Object user : users){
   				User u = (User)user;
   				if(usern.equals(u.getEmail()) && password.equals(u.getPwd())){
   					passCheck = true;
   		    	}else{
   		    		passCheck = false;
   		    	}
   				
   			}
   		} catch (InterruptedException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   			return false;
   		} catch (ExecutionException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   			return false;
   		}
    	
    	return passCheck;
    }
}
