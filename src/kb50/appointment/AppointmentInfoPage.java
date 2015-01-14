package kb50.appointment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
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
	private float zoom = 14;

	private double lat;
	private double lng;

	private Geocoder gc;
	private Resources res;

	private String date;
	private String name;
	private String id;
	private String description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int idtemp =intent.getIntExtra("appointment_id", 0); 
		id = Integer.toString(idtemp);
		name = intent.getStringExtra("appointment_name");
		description = intent.getStringExtra("appointment_description");
		date = intent.getStringExtra("appointment_date");

		setContentView(R.layout.appointment_info_page_layout);

		TextView appointmentName = (TextView) findViewById(R.id.appointment_name);
		TextView appointmentDescr = (TextView) findViewById(R.id.appointment_desc);
		TextView appointmentDate = (TextView) findViewById(R.id.appointment_date);

		appointmentName.setText(name);
		appointmentDescr.setText(description);
		appointmentDate.setText(date);

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
						list = gc.getFromLocationName(
								"waldorpstraat 47, den haag", 1);
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

					gotoLocation(lat, lng, zoom);
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
			startActivity(new Intent(AppointmentInfoPage.this, EditAppointment.class));
			break;
		case R.id.button_Back:
			finish();
			break;
		case R.id.button_delete:
			deleteAppoinment();
			break;
		case R.id.button_sendMessage:
			SendMessage();
			break;
		case R.id.button_route:
			String uri = "https://maps.google.com/maps?f=d&daddr=" + lat + ","
					+ lng;
			Intent route = new Intent(android.content.Intent.ACTION_VIEW,
			// Uri.parse(("geo:" + lat + ", " + lng)));
					Uri.parse(uri));
			startActivity(route);

			break;
		}

	}

	public void deleteAppoinment() {

		DeleteFragment dialogFragment = DeleteFragment.newInstance(res
				.getString(R.string.Deletemessage));
		dialogFragment.show(getFragmentManager(), "dialog");

	}

	public void doPositiveClick() {
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

	public void SendMessage() {
		SendFragment dialogFragment2 = SendFragment.newInstance();
		dialogFragment2.show(getFragmentManager(), "dialog");
	}

	public void doPositiveClickSendMessage(int mSelectedItem) {
		String[] array = res.getStringArray(R.array.Default_messages);
		String item = array[mSelectedItem];

		//Toast.makeText(this, item + " get number ", Toast.LENGTH_SHORT).show();
		// TODO get numbers

		SmsManager sm = SmsManager.getDefault();

		List<User> users = new ArrayList<User>();
		users = getUsersSelectedAppointment(id);
		for (int i=0; i<users.size(); i++){
			String temp= Integer.toString(users.get(i).getPhone());
			sm.sendTextMessage(temp, null, item , null, null);
		}
		
		
		
		
		// 31634144093 <<-- real number do not use :P
	}

	public void doNegativeClickSendMessage() {
	}

}
