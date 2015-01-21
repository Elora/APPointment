package kb50.appointment;

import java.util.Timer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;

public class Notifcation extends BroadcastReceiver {

	private boolean isServiceRunning = false;
	private Intent serviceIntent;
	@Override
	public void onReceive(Context context, Intent intent) {

		// now start the service, if not already running
		try {
			if (!isServiceRunning) {
				

				serviceIntent = new Intent(context, TimerService.class);
       		 
        
        	    
        		context.startService(serviceIntent);
				
				isServiceRunning = true;
			} else {
				

				Toast.makeText(context, "service already running!!",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			isServiceRunning = false;
		}

	}

}
