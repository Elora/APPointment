package kb50.appointment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kb50.appointment.Controller.Select;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends Activity {

	private EditText email;
	private EditText password;

	private int userId;

	SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
    }
    
    public void onClickLogin(View v){    	
    	String user = email.getText().toString();
    	String pass = password.getText().toString(); 
    	
    	if(checkCredentials(user, pass) == true){
    		startActivity(new Intent(LogIn.this, TabLayout.class));
    	}else{
			Context context = getApplicationContext();
			CharSequence text = "Wrong email or password";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);

			toast.show();
		}
	}

	public void onClickReg(View v) {
		startActivity(new Intent(LogIn.this, RegisterActivity.class));
	}

	private List<User> getUsers() {

		List<User> users = new ArrayList<User>();

		try {
			for (Object o : new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/GetUsers.php").execute(
					new ApiConnector()).get()) {

				users.add((User) o);

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}
    
    private boolean checkCredentials(String usern, String password){
    	boolean passCheck = false;
    	   try {
   			List<Object> users = new Controller().new Select("http://eduweb.hhs.nl/~13061798/GetUsers.php").execute(new ApiConnector()).get();
   			for(Object user : users){
   				User u = (User)user;
   				if(usern.equals(u.getEmail()) && password.equals(u.getPwd())){
   					passCheck = true;
   					break;
   		    	}else{
   		    		passCheck = false;
   		    	}
   				
   			}
   		} catch (InterruptedException e) {
   			e.printStackTrace();
   			return false;
   		} catch (ExecutionException e) {
   			e.printStackTrace();
   			return false;
   		}
    	
    	return passCheck;
    }
}
