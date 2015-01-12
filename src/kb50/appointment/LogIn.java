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

	private EditText username;
	private EditText password;
	
	private int userId;

	SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		 prefs = PreferenceManager.getDefaultSharedPreferences(this);

		username = (EditText) findViewById(R.id.usernameField);
		password = (EditText) findViewById(R.id.passwordField);
		
	}

	@Override
	protected void onResume() {
		final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext()); 
		
			if (mSharedPreference.contains("username")) {
			if (mSharedPreference.contains("password")) {
				Intent i = new Intent(this, TabLayout.class);
				startActivity(i);

			}
		}
		super.onResume();
	}

	public void onClickLogin(View v) {
		String user = username.getText().toString();
		String pass = password.getText().toString();

		if (checkCredentials(user, pass) == true) {
			Editor editor = prefs.edit();

			editor.putInt("id", userId);
			editor.putString("username", user);
			editor.putString("password", pass);
			editor.commit();
			startActivity(new Intent(LogIn.this, TabLayout.class));
		} else {
			Context context = getApplicationContext();
			CharSequence text = "Wrong username or password";
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

	private boolean checkCredentials(String usern, String password) {
		// TODO replace with DB check
		boolean passCheck = false;

		for (User user : getUsers()) {

			if (usern.equals(user.getEmail()) && password.equals(user.getPwd())) {
				userId = user.getId();
				passCheck = true;

			} else {
				passCheck = false;
			}

		}

		return passCheck;
	}
}
