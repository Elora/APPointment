package kb50.appointment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
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
	private int id;

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

		//Toast.makeText(this, "to come, press back pls", Toast.LENGTH_SHORT).show();
		
		
		String pass1 = password1.getText().toString();
		String pass2 = password2.getText().toString();

		User u = new User();
		u.setEmail("abc");
		u.setName(name.getText().toString());
		u.setPhone(Integer.parseInt(phoneNum.getText().toString()));
		u.setPwd(pass1);
		u.setId(id);
		u.setImageurl("http://eduweb.hhs.nl/~13061798/Profile/profilepic.jpg");

		if (matchingPassword(pass1, pass2) == true) {
			Context context = getApplicationContext();

			new Controller().new Insert(u,
					"http://eduweb.hhs.nl/~13061798/EditUser.php?id="+id)
					.execute(new ApiConnector());
			
			CharSequence text = "Edit complete";
			int duration = Toast.LENGTH_SHORT;
			

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			setResult(RESULT_OK);
			this.finish();
			
		} else if(matchingPassword(pass1, pass2) == false) {
			Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
			
			//Set focus on first password field & reset password fields
			password1.requestFocus();
			password1.setText("");
			password2.setText("");
			
		}
	}

	private boolean matchingPassword(String pass1, String pass2) {
		if (pass1.equals(pass2)) {
			return true;
		} else {
			return false;
		}
	}


	public User GetUser(){

		final SharedPreferences mSharedPreference= PreferenceManager
				.getDefaultSharedPreferences(getBaseContext()); 
		id = mSharedPreference.getInt("id", 0);
		
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
