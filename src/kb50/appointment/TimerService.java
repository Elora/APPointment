package kb50.appointment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class TimerService extends Service {

	private static final long TIMER_INTERVAL = 10 * 1000; // 10 secs

	// use an android handler object to handle the thread(s)
	private Handler theHandler = new Handler();

	// standard java timer class
	private Timer theTimer = null;

	public IBinder mBinder = new LocalBinder();

	// need to write the IBinder method, even if not doing anything with it
	// (could return null if local service to app)
	@Override
	public IBinder onBind(Intent intent) {
		// return null; //return null if not binding
		return mBinder; // as separate app will bind later, return an IBinder
						// instance.
	}

	public class LocalBinder extends Binder {
		public TimerService getService() {
			return TimerService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// create new timer - check first if timer exists, if so cancel it,
		if (theTimer != null) {
			theTimer.cancel();
		} else {
			theTimer = new Timer();
		}

		// now schedule task using standard timer method
		theTimer.scheduleAtFixedRate(new TimerDisplayTask(), 0, TIMER_INTERVAL);
	}

	// need to cancel the timer or it keeps running even if the service is
	// stopped!
	public void onDestroy() {
		theTimer.cancel();
		Toast.makeText(this, "Timer Service Stopped...", Toast.LENGTH_SHORT)
				.show();
		super.onDestroy();
	}

	// nested inner class to handle timer events
	class TimerDisplayTask extends TimerTask {

		@Override
		public void run() {
			// run on a separate thread
			theHandler.post(new Runnable() {

				final SharedPreferences mSharedPreference = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());

				final int id = mSharedPreference.getInt("id", 0);

				@Override
				public void run() {
					// display toast
					try {
						List<Object> incomingAppointments = new Controller().new Select(
								"http://eduweb.hhs.nl/~13061798/IncomingAppointment.php?id="
										+ id).execute(new ApiConnector()).get();

						if (!incomingAppointments.isEmpty()) {

							for (Object o : incomingAppointments) {
								Appointment a = (Appointment) o;
								if(!a.getReceived()){
									
									Intent i = new Intent(TimerService.this,
											AcceptAppDialog.class);

									i.putExtra("appName", a.getName());
									i.putExtra("appDate", a.getDate());
									i.putExtra("appDescription", a.getDescription());
									i.putExtra("userId", id+"");
									i.putExtra("appId", a.getId()+"");
									
									PendingIntent pi = PendingIntent.getActivity(
										getBaseContext(), 1, i,
											PendingIntent.FLAG_UPDATE_CURRENT);
									
									NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
									Notification notify = new Notification(
											R.drawable.ic_launcher,
											"New appointment invitation!", System
													.currentTimeMillis());

								    
								
									notify.setLatestEventInfo(getBaseContext(),
											a.getName(), "Date:" + a.getDate(), pi);
									
									new Controller().new Select("http://eduweb.hhs.nl/~13061798/SetReceived.php?userid="+id+"&appid="+a.getId()).execute(new ApiConnector());
									nm.notify(1, notify);
									
									
									
								}
							

							}

						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		}

	}
}
