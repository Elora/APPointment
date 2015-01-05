package kb50.appointment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private final String TEMP_USER = "Tony";
	private EditText username;
	private EditText phoneNum;
	private EditText mail;
	private EditText password1;
	private EditText password2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		username = (EditText) findViewById(R.id.reg_user_field);
		phoneNum = (EditText) findViewById(R.id.reg_phone_field);
		mail = (EditText) findViewById(R.id.reg_mail_field);
		password1 = (EditText) findViewById(R.id.reg_pass1_field);
		password2 = (EditText) findViewById(R.id.reg_pass2_field);
	}
	
	public void onClickBtnCancel(View v){
		startActivity(new Intent(RegisterActivity.this, LogIn.class));
	}
	
	public void onClickBtnSubmit(View v){
		String user = username.getText().toString();
		String phone = phoneNum.getText().toString();
		String email = mail.getText().toString();
		String pass1 = password1.getText().toString();
		String pass2 = password2.getText().toString();
		
		if(userAvailable(user) == true && matchingPassword(pass1, pass2) == true){
			Context context = getApplicationContext();
			CharSequence text = "Registration complete";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();			
		}else{
			Context context = getApplicationContext();
			CharSequence text = "Registration failed";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();			
		}
		
	}
	
	private boolean matchingPassword(String pass1, String pass2){
		if(pass1.equals(pass2)){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean userAvailable(String user){
		if(user.equalsIgnoreCase(TEMP_USER)){
			return false;
		}else{
			return true;
		}
	}
}
