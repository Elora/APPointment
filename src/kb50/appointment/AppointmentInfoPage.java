
package kb50.appointment;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String id = intent.getStringExtra("ID");
		setContentView(R.layout.appointment_info_page_layout);
		Toast.makeText(this, "Item " + id + " was passed", Toast.LENGTH_SHORT)
				.show();
		try {
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			map.setMyLocationEnabled(true);
			getLocation();
			onMapReady(map);
		} catch (IOException e) {
			Context context = getApplicationContext();
			CharSequence text = "IOException!!!!!!! :(";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
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
			Toast.makeText(this, "going to alter table intent or ...",
					Toast.LENGTH_SHORT).show();

			break;
		case R.id.button_Back:
			finish();
			break;
		case R.id.button_delete:
			Toast.makeText(this, "to delete appointment", Toast.LENGTH_SHORT)
					.show();

			break;
		case R.id.button_sendMessage:
			Intent i = new Intent(this, SendMessageActivity.class);
			startActivity(i);
			break;
		case R.id.button_viewmap:
			Toast.makeText(this, "to be implemented, google maps",
					Toast.LENGTH_SHORT).show();

			break;
		}

	}

}
