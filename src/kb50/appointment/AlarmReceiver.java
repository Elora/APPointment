package kb50.appointment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	public MediaPlayer mp;
	private String name;
	private String date;
	private String time;


	public void onReceive(Context context, Intent intent) {

		mp = MediaPlayer.create(context, R.raw.alarm);
		mp.start();
		
		name = intent.getStringExtra("appointment name");
		date = intent.getStringExtra("date");
		time = intent.getStringExtra("time");

		Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_SHORT).show();
        
		 Intent i = new Intent();
	        i.setClass(context, kb50.appointment.AlarmDialogActivity.class);
	        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        i.putExtra("name", name);
	        i.putExtra("date", date);
	        i.putExtra("time", time);
	        context.startActivity(i);
        
	}
	

		
		
}