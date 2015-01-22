package kb50.appointment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kb50.appointment.Controller.Insert;
import kb50.appointment.Controller.Select;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private EditText name;
	private EditText phoneNum;
	private EditText mail;
	private EditText password1;
	private EditText password2;
	private List<EditText> fields;
	private User u = new User();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		name = (EditText) findViewById(R.id.reg_user_field);
		phoneNum = (EditText) findViewById(R.id.reg_phone_field);
		mail = (EditText) findViewById(R.id.reg_mail_field);
		password1 = (EditText) findViewById(R.id.reg_pass1_field);
		password2 = (EditText) findViewById(R.id.reg_pass2_field);
		

		fields = new ArrayList<EditText>();
		fields.add(name);
		fields.add(phoneNum);
		fields.add(mail);
		fields.add(password1);
		fields.add(password2);
	}

	public void onClickBtnCancel(View v) {
		startActivity(new Intent(RegisterActivity.this, LogIn.class));
	}

	public void onClickBtnSubmit(View v) {
		for(EditText t : fields){
			t.setBackgroundColor(Color.WHITE);
		}
		if (fieldsEmpty() == false) {
			String pass1 = password1.getText().toString();
			String pass2 = password2.getText().toString();

		
				
			
			
		

			if (emailAvailable(mail.getText().toString()) == true
					&& matchingPassword(pass1, pass2) == true) {

				
				u.setEmail(mail.getText().toString());
				u.setName(name.getText().toString());
				u.setImageurl("http://image.com/test.jpg");
				u.setPwd(pass1);
				try{
					u.setPhone(Integer.parseInt(phoneNum.getText().toString()));
				}catch(NumberFormatException e){
					phoneNum.setBackgroundColor(Color.RED);
					Toast.makeText(this, "Enter only numbers please", Toast.LENGTH_SHORT).show();
					
				}
				
				new Controller().new Insert(u,
						"http://eduweb.hhs.nl/~13061798/CreateUser.php")
						.execute(new ApiConnector());

				Toast.makeText(this, "Registration complete", Toast.LENGTH_SHORT).show();
				this.finish();
			} else if (matchingPassword(pass1, pass2) == false) {
				Toast.makeText(this, "Passwords don't match",
						Toast.LENGTH_SHORT).show();

				// Set focus on first password field & reset password fields
				password1.requestFocus();
				password1.setText("");
				password2.setText("");

			} else if (emailAvailable(mail.getText().toString()) == false) {
				Toast.makeText(this, "Email address already in use",
						Toast.LENGTH_SHORT).show();

				// Reset email address, otherwise it will keep saying the email
				// address is in use
				u.setEmail("");
			}
		} else {
			Toast.makeText(this, "Please fill in every field!", Toast.LENGTH_SHORT)
					.show();
		}
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
			List<Object> users = new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/CheckUserExist.php?email="
							+ email).execute(new ApiConnector()).get();
			if (users.isEmpty()) {
				avail = true;

			}
		} catch (InterruptedException e) {
			avail = false;
			e.printStackTrace();
		} catch (ExecutionException e) {
			avail = false;
			e.printStackTrace();
		}
		return avail;
	}

	private boolean fieldsEmpty() {
		String uName = name.getText().toString();
		String pNum = phoneNum.getText().toString();
		String mAddress = mail.getText().toString();
		String p1 = password1.getText().toString();
		String p2 = password2.getText().toString();
		
		if (uName.isEmpty() || pNum.isEmpty() || mAddress.isEmpty() || p1.isEmpty() || p2.isEmpty()) {
			for(EditText t : fields){
				if(t.getText().toString().isEmpty()){
					t.setBackgroundColor(Color.RED);
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
