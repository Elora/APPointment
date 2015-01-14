package kb50.appointment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfielActivity extends Activity {

	private final String TEMP_USER = "Tony";
	private EditText name;
	private EditText phoneNum;
	private EditText mail;
	private EditText password1;
	private EditText password2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);
		
		//final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext()); 
		//int id = mSharedPreference.getInt("id", 0);
		
		
		User user = GetUser();
		
		name = (EditText) findViewById(R.id.reg_user_fieldEPA);
		phoneNum = (EditText) findViewById(R.id.reg_phone_fieldEPA);
		mail = (EditText) findViewById(R.id.reg_mail_fieldEPA);
		password1 = (EditText) findViewById(R.id.reg_pass1_fieldEPA);
		password2 = (EditText) findViewById(R.id.reg_pass2_fieldEPA);
		
		
		String phone = Integer.toString(user.getPhone());
		
		name.setText(user.getName(), TextView.BufferType.EDITABLE);
		phoneNum.setText(phone, TextView.BufferType.EDITABLE);
		mail.setText(user.getEmail(), TextView.BufferType.EDITABLE);
		password1.setText(user.getPwd(), TextView.BufferType.EDITABLE);
		password2.setText(user.getPwd(), TextView.BufferType.EDITABLE);
		
		
	}

	public void onClickBtnCancel(View v) {
		finish();
	}

	public void onClickBtnSubmit(View v) {

		Toast.makeText(this, "to come, press back pls", Toast.LENGTH_SHORT).show();
		
		/*String pass1 = password1.getText().toString();
		String pass2 = password2.getText().toString();

		User u = new User();
		u.setEmail(mail.getText().toString());
		u.setName(name.getText().toString());
		u.setPhone(Integer.parseInt(phoneNum.getText().toString()));
		u.setPwd(pass1);
		u.setImageurl("http://image.com/test.jpg");

		if (emailAvailable(u.getEmail()) == true
				&& matchingPassword(pass1, pass2) == true) {
			Context context = getApplicationContext();

			new Controller().new Insert(u,
					"http://eduweb.hhs.nl/~13061798/CreateUser.php")
					.execute(new ApiConnector());
			
			CharSequence text = "Registration complete";
			int duration = Toast.LENGTH_SHORT;
			

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			this.finish();
		} else if(matchingPassword(pass1, pass2) == false) {
			Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
			
			//Set focus on first password field & reset password fields
			password1.requestFocus();
			password1.setText("");
			password2.setText("");
			
		}else if(emailAvailable(u.getEmail()) == false){
			Toast.makeText(this, "Email address already in use", Toast.LENGTH_SHORT).show();
			
			//Reset email address, otherwise it will keep saying the email address is in use
			u.setEmail("");
		}
*/
	}

	private boolean matchingPassword(String pass1, String pass2) {
		if (pass1.equals(pass2)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean emailAvailable(String email) {
		boolean avail = false; 	   
		
		try {
			List<Object> users = new Controller().new Select("http://eduweb.hhs.nl/~13061798/GetUsers.php").execute(new ApiConnector()).get();
			for(Object user : users){
				User u = (User)user;
				if(email.equalsIgnoreCase(u.getEmail())){
					avail = false;
					break;
				}else{
					avail = true;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return avail;
	}

	public User GetUser(){

		final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext()); 
		int id = mSharedPreference.getInt("id", 0);
		
		List<User> users = new ArrayList<User>();
		users = GetProfiels();
		
		User user = new User();
		for(int i = 0; i<users.size(); i++){
			if (users.get(i).getId() == (id)){
				user=users.get(i);
			}
		}
		return user;
			
	}
	
	
	public List<User> GetProfiels(){
		
		List<User> users = new ArrayList<User>();
		try {
			for (Object o : new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/GetUsers.php"
							).execute(
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


}
