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

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		Context ctx = this;
		
		final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
	    alert.setTitle("Alarm");
	    alert.setMessage("Appointment");
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

