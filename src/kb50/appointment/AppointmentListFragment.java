package kb50.appointment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AppointmentListFragment extends ListFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Each row in the list stores country name, currency and flag
		List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < getAppointments().size(); i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("id", getAppointments().get(i).getId() + "");
			hm.put("name", getAppointments().get(i).getName());
			hm.put("date", getAppointments().get(i).getDate().substring(0, 10));
			hm.put("time", getAppointments().get(i).getDate().substring(10));
			// hm.put("flag", Integer.toString(flags[i]) );
			aList.add(hm);
		}

		// Keys used in Hashmap
		String[] from = { "name", "date", "time", "id" };
		// Ids of views in profiellistfragment_layout
		int[] to = { R.id.name2, R.id.date2, R.id.time2 };

		// Instantiating an adapter to store each items
		// R.layout.profiellistfragment_layout defines the layout of each item
		SimpleAdapter adapter = new SimpleAdapter(getActivity()
				.getBaseContext(), aList,
				R.layout.appointmentlistfragment_layout, from, to);

		setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public List<Appointment> getAppointments() {

		final SharedPreferences mSharedPreference = PreferenceManager
				.getDefaultSharedPreferences(getActivity().getBaseContext());
		int id = mSharedPreference.getInt("id", 0);

		List<Appointment> appointments = new ArrayList<Appointment>();
		try {
			List<Object> apps = new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/GetAppointments.php?id="
							+ id).execute(new ApiConnector()).get();
			
			for (Object o : apps) {

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
		Intent i = new Intent(getActivity(), AppointmentInfoPage.class);
		for (Appointment a : getAppointments()) {

			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>) l
					.getAdapter().getItem(pos);

			if (a.getId() == Integer.parseInt(map.get("id"))) {

				i.putExtra("appointment_id", a.getId());
				i.putExtra("appointment_name", a.getName());
				i.putExtra("appointment_priority", a.getPriority());
				i.putExtra("appointment_description", a.getDescription());
				i.putExtra("appointment_date", a.getDate());
				i.putExtra("appointment_location", a.getLocation());
			}

		}
		getActivity().finish();
		startActivity(i);
	}

}
