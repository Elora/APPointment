package kb50.appointment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends Activity {

	private EditText username;
	private EditText password;
	private final String TEMP_USER = "Tony";
	private final String TEMP_PASS = "isAwesome";
	
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
    
    private boolean checkCredentials(String user, String password){
    	// TODO replace with DB check
    	if(user.equals(TEMP_USER) && password.equals(TEMP_PASS)){
    		return true;
    	}else{
    		return false;
    	}
    }
}
