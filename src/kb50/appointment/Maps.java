package kb50.appointment;

import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

//import com.google.android.maps.*;

public class Maps extends Activity {
	static final LatLng HAMBURG = new LatLng(53, 9);
	static final LatLng KIEL = new LatLng(53.3, 9.9);
	private GoogleMap map;

	// private Geocoder geocoder;
	private EditText address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appointment_info_page_layout);

		// address = (EditText) findViewById(R.id.address);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		try {
			Geocoder geocoder = new Geocoder(this);
			List<Address> addresses;

			addresses = geocoder.getFromLocationName(
					"johanna westerdijkplein, Den Haag, NL", 1);
			if (addresses.size() > 0) {
				double latitude = addresses.get(0).getLatitude();
				double longitude = addresses.get(0).getLongitude();

				Toast.makeText(this, "A", Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(this, "B", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "C", Toast.LENGTH_SHORT).show();
		}

		/*
		 * Marker hamburg = map.addMarker(new
		 * MarkerOptions().position(HAMBURG).title("Hamburg"));
		 * map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
		 */
		map.setMyLocationEnabled(true);

	}

	/*
	 * public void onClick(View v) { switch (v.getId()) {
	 * 
	 * case R.id.sendAddress: get coordinates from address geocoder = new
	 * Geocoder();
	 * 
	 * eAddress = geocoder.getFromLocationName(address, 1); if(eAddress.size() >
	 * 0){ double latitude = eAddress.get(0).getLatitude(); double longitude =
	 * eAddress.get(0).getLongitude(); } map.setMyLocationEnabled(true); break;
	 * case R.id.getDirections: //implement by opening google maps app? break;
	 * 
	 * }
	 */

}
