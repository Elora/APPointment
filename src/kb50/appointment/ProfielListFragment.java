package kb50.appointment;

//http://wptrafficanalyzer.in/blog/android-listfragment-with-images-and-text-using-android-support-library/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ProfielListFragment extends ListFragment {
	View view;
	Button btn;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Each row in the list stores country name, currency and flag
		List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

		int aantalApp = 0;
		if (getAppointments().size() > 2) {

			aantalApp = 2;

		} else {

			aantalApp = getAppointments().size();

		}

		for (int i = 0; i < aantalApp; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("name", getAppointments().get(i).getName());
			hm.put("date", getAppointments().get(i).getDate().substring(0, 10));
			hm.put("time", getAppointments().get(i).getDate().substring(10));

			// hm.put("flag", Integer.toString(flags[i]) );
			aList.add(hm);
		}

		// Keys used in Hashmap
		String[] from = { "name", "date", "time" };
		// Ids of views in profiellistfragment_layout
		int[] to = { R.id.name, R.id.date, R.id.time };

		// Instantiating an adapter to store each items
		// R.layout.profiellistfragment_layout defines the layout of each item
		SimpleAdapter adapter = new SimpleAdapter(getActivity()
				.getBaseContext(), aList, R.layout.profiellistfragment_layout,
				from, to);

		setListAdapter(adapter);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public List<Appointment> getAppointments() {

		final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()); 
		int id = mSharedPreference.getInt("id", 0);
		
		List<Appointment> appointments = new ArrayList<Appointment>();
		try {
			for (Object o : new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/GetAppointments.php?id="
							+ id).execute(
					new ApiConnector()).get()) {

				appointments.add((Appointment) o);

			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return appointments;

	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		super.onListItemClick(l, v, pos, id);
		Toast.makeText(getActivity(), "Item " + pos + " was clicked " + id,
				Toast.LENGTH_SHORT).show();
		
		
		Intent i = new Intent(getActivity(), AppointmentInfoPage.class);
		i.putExtra("ID", "id van de class");
		startActivity(i);
	}

}

