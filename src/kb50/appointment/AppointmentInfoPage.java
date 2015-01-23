package kb50.appointment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kb50.appointment.Controller.Select;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AppointmentInfoPage extends Activity {
	private GoogleMap map;
	private final float ZOOM = 14;

	private double lat;
	private double lng;

	private Geocoder gc;
	private Resources res;

	private String date;
	private String name;
	private String id;
	private String description;
	private String location;

	private List<User> ownerAndGuests;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int idtemp = intent.getIntExtra("appointment_id", 0);
		id = Integer.toString(idtemp);
		name = intent.getStringExtra("appointment_name");
		description = intent.getStringExtra("appointment_description");
		date = intent.getStringExtra("appointment_date");

		ownerAndGuests = getUsersSelectedAppointment(id);

		location = intent.getStringExtra("appointment_location");

		setContentView(R.layout.appointment_info_page_layout);

		TextView appointmentName = (TextView) findViewById(R.id.appointment_name);
		TextView appointmentDescr = (TextView) findViewById(R.id.appointment_desc);
		TextView appointmentDate = (TextView) findViewById(R.id.appointment_date);
		TextView appointmentTime = (TextView) findViewById(R.id.appointment_time);
		TextView appointmentLocation = (TextView) findViewById(R.id.appointment_location);

		String[] dateTime = date.split("\\s+");
		String date = dateTime[0];
		String time = dateTime[1];
		
		appointmentName.setText(name);
		appointmentDescr.setText(description);
		appointmentDate.setText(date);
		appointmentTime.setText(time);

		
			appointmentLocation.setText(location);
		
		try {
			final SharedPreferences mSharedPreference = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			int id = mSharedPreference.getInt("id", 0);

			List<Object> appointments = new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/GetAppointments.php?id="
							+ id).execute(new ApiConnector()).get();

			for (Object o : appointments) {
				Appointment a = (Appointment) o;
				int appID = Integer.parseInt(this.id);

				if (a.getId() == appID) {
					this.location = a.getLocation();
					break;
				}
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (!ownerAndGuests.isEmpty()) {
			String guestString = "Guests:" + "\n";
			TextView guests = (TextView) findViewById(R.id.GuestNames);
			for (User u : ownerAndGuests) {

				guestString = guestString + "\n" + u.getName();

			}
			guests.setText(guestString);

		}

		try {
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			map.setMyLocationEnabled(true);
			getLocation();
			onMapReady(map);
		} catch (Exception e) {
			Context context = getApplicationContext();
			CharSequence text = "Exception!!!!!!! :(";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

		res = getResources();
	}

	private void gotoLocation(double lat, double lng, float zoom) {
		LatLng ll = new LatLng(lat, lng);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
		map.moveCamera(update);

	}

	private void getLocation() throws IOException {

		gc = new Geocoder(this);

		try {
			if (Geocoder.isPresent()) {
				List<Address> list = null;

				boolean worked = false;
				int count = 0, maxretry = 5;
				while (!worked && count < maxretry) {
					try {
						list = gc.getFromLocationName(location, 1);
						worked = true;
					} catch (Exception te) {
						System.err.println(te
								+ " Exception occurred, will retry max "
								+ maxretry + " times");
						count++;
					}
				}
				if (list != null) {
					Address addresses = list.get(0);

					String locality = addresses.getLocality();

					Toast.makeText(this, locality, Toast.LENGTH_SHORT).show();

					lng = addresses.getLongitude();
					lat = addresses.getLatitude();

					gotoLocation(lat, lng, ZOOM);
				}
			} else {
				System.err
						.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! GEOCODER SERVICE IS NOT AVAILABLE");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onMapReady(GoogleMap map) {
		Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(
				lat, lng)));

		marker.setIcon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_alter:
			Intent i = new Intent(this, EditAppointment.class);
			i.putExtra("id", this.id);
			startActivityForResult(i, 2015);

			// startActivity(new Intent(AppointmentInfoPage.this,
			// EditAppointment.class));
			break;

		case R.id.button_delete:
			deleteAppoinment();
			break;
		case R.id.button_sendMessage:
			sendMessage();
			break;
		case R.id.AddGuestBtn:
			addGuest();
			break;
		case R.id.button_route:
			String uri = "https://maps.google.com/maps?f=d&daddr=" + lat + ","
					+ lng;
			Intent route = new Intent(android.content.Intent.ACTION_VIEW,
			// Uri.parse(("geo:" + lat + ", " + lng)));
					Uri.parse(uri));
			startActivity(route);

			break;

		case R.id.button_location:
			sendLocation();
			break;
		}

	}

	private void sendLocation() {

		String message = "http://maps.google.com/maps?q=" + lat + "," + lng;
		
		/*
		 * phone numbers from database
		try {
			SmsManager smsManager = SmsManager.getDefault();
			
			for (int i = 0; i < ownerAndGuests.size(); i++) {
				String temp = Integer.toString(ownerAndGuests.get(i).getPhone());
				smsManager.sendTextMessage(temp, null, message, null, null);
			}		
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS failed, please try again.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		*/
		
		Toast.makeText(getApplicationContext(),"Location sent: " + message, Toast.LENGTH_LONG).show();
	}

	private void deleteAppoinment() {

		DeleteFragment dialogFragment = DeleteFragment.newInstance(res
				.getString(R.string.Deletemessage));
		dialogFragment.show(getFragmentManager(), "dialog");

	}

	public void doPositiveClick() {
		new Controller().new Select(
				"http://eduweb.hhs.nl/~13061798/DeleteAppointment.php?id=" + id)
				.execute(new ApiConnector());
		Toast toast = Toast.makeText(this, "Appointment deleted!",
				Toast.LENGTH_SHORT);
		toast.show();
		startActivity(new Intent(this, TabLayout.class));
		this.finish();
	}

	public void doNegativeClick() {
	}

	public List<User> getUsersSelectedAppointment(String id) {

		List<User> users = new ArrayList<User>();

		try {
			for (Object o : new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/GetAppointmentUsers.php?id="
							+ id).execute(new ApiConnector()).get()) {

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

	private void sendMessage() {
		SendFragment dialogFragment2 = SendFragment.newInstance();
		dialogFragment2.show(getFragmentManager(), "dialog");
	}

	public void doPositiveClickSendMessage(int mSelectedItem) {
		String[] array = res.getStringArray(R.array.Default_messages);
		String item = array[mSelectedItem];

		// Toast.makeText(this, item + " get number ",
		// Toast.LENGTH_SHORT).show();
		// TODO get numbers

		SmsManager sm = SmsManager.getDefault();

		for (int i = 0; i < ownerAndGuests.size(); i++) {
			String temp = Integer.toString(ownerAndGuests.get(i).getPhone());
			sm.sendTextMessage(temp, null, item, null, null);
		}

	}

	public void doNegativeClickSendMessage() {
	}

	public void doNegativeClickAddGuestsMessage() {
	}

	public void doPositiveClickAddGuestsMessage(List<User> selecteditems) {
	}

	private void addGuest() {
		AddGuestFragment dialogFragment3 = AddGuestFragment.newInstance();
		dialogFragment3.show(getFragmentManager(), id);
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 2015) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
            	
    			//startActivity(new Intent(this, AppointmentInfoPage.class));
    			//this.finish();
            	refreshFields();
            	
                // Do something with the contact here (bigger example below)
            }
        }
        
    }
	
	
	private void refreshFields(){
    	String appid = id;
    	
		final SharedPreferences mSharedPreference = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		int idpref = mSharedPreference.getInt("id", 0);

    	
    	List<Appointment> appointments = new ArrayList<Appointment>();
		try {
			for (Object o : new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/GetAppointments.php?id="
							+ idpref).execute(new ApiConnector()).get()) {

				appointments.add((Appointment) o);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Boolean found = false;
		for (int j=0; j<appointments.size(); j++){
			if (appointments.get(j).getId() == Integer.parseInt(appid)){
				found = true;
				
				
				
				name = appointments.get(j).getName();
				description = appointments.get(j).getDescription();
				date = appointments.get(j).getDate();

				ownerAndGuests = getUsersSelectedAppointment(id);

				location = appointments.get(j).getLocation();


				TextView appointmentName = (TextView) findViewById(R.id.appointment_name);
				TextView appointmentDescr = (TextView) findViewById(R.id.appointment_desc);
				TextView appointmentDate = (TextView) findViewById(R.id.appointment_date);
				TextView appointmentLocation = (TextView) findViewById(R.id.appointment_location);

				
				appointmentName.setText(name);
				appointmentDescr.setText(description);
				appointmentDate.setText(date);
				appointmentLocation.setText(location);
				
			}
		}
        
		if (found == false){
			this.finish();
		}
	}
	
	 public void onSaveInstanceState(Bundle outState) {
		    outState.putString("name", name);
	        outState.putString("description", description);
	        outState.putString("date", date);
	        outState.putString("location", location);
	        outState.putString("id", id);
	}
	 
	 
	 protected void onRestoreInstanceState(Bundle savedInstanceState) {
	        if (savedInstanceState != null) {
	            name = savedInstanceState.getString("name");
	            description = savedInstanceState.getString("description");
	            date = savedInstanceState.getString("date");
	            location = savedInstanceState.getString("location");
	            id = savedInstanceState.getString("id");
	            
	            TextView appointmentName = (TextView) findViewById(R.id.appointment_name);
				TextView appointmentDescr = (TextView) findViewById(R.id.appointment_desc);
				TextView appointmentDate = (TextView) findViewById(R.id.appointment_date);
				TextView appointmentLocation = (TextView) findViewById(R.id.appointment_location);

				
				appointmentName.setText(name);
				appointmentDescr.setText(description);
				appointmentDate.setText(date);
				appointmentLocation.setText(location);

	            
	        }
	        super.onRestoreInstanceState(savedInstanceState);
	    }
	
}
