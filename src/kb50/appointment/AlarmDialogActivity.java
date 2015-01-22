package kb50.appointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AlarmDialogActivity extends Activity {

	private String name;
	private String date;
	private String time;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		Context ctx = this;
		
		Intent i = getIntent();
		name = i.getStringExtra("name");
		date = i.getStringExtra("date");
		time = i.getStringExtra("time");
		
		
		final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
	    alert.setTitle("Appointment!");
	    alert.setMessage(name + "\n" + date + "\n" + time);
	    alert.setCancelable(false);
	    alert.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog,int whichButton)
			{
			dialog.cancel();
			finish();
	
			}});
	      alert.create();
	      alert.show();
	}
	}

