package kb50.appointment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	public MediaPlayer mp;


	public void onReceive(Context context, Intent intent) {

		mp = MediaPlayer.create(context, R.raw.alarm);
		mp.start();

		Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_SHORT).show();
        
		 Intent i = new Intent();
	        i.setClass(context, kb50.appointment.AlarmDialogActivity.class);
	        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(i);
        
	}
	

		
		
}