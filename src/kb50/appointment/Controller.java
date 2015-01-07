package kb50.appointment;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;


public class Controller extends AsyncTask<ApiConnector, Long, List<Object>> {

	private String url = "";
	public List<Object> users = new ArrayList<Object>();
	private List<Object> locations = new ArrayList<Object>();
	private List<Object> appointments = new ArrayList<Object>();

	public Controller(String url) {

		this.url = url;
		this.execute(new ApiConnector());
	}

	public List<Object> appointmentsToList(JSONArray jsonArray) {

		// url = "http://eduweb.hhs.nl/~13061798/GetAppointments.php";

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = null;
			try {

				json = jsonArray.getJSONObject(i);
				Appointment a = new Appointment();

				a.setId(json.getInt("id"));
				a.setDate(json.getString("date"));
				a.setName(json.getString("name"));
				a.setPriority(json.getInt("priority"));
				a.setDescription(json.getString("description"));
				for(Object o : locations){
					Location l = (Location)o;
					if(l.getId() == json.getInt("locationid")){
						
						a.setLocation(l);
						
					}
				}
			
				appointments.add(a);

			} catch (JSONException e) {
				e.printStackTrace();

			}
		}

		return appointments;
	}

	public List<Object> locationsToList(JSONArray jsonArray) {

		// url = "http://eduweb.hhs.nl/~13061798/GetLocations.php";

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = null;
			try {

				json = jsonArray.getJSONObject(i);
				Location l = new Location();

				l.setId(json.getInt("id"));
				l.setCountry(json.getString("Country"));
				l.setCity(json.getString("City"));
				l.setAddress(json.getString("Address"));
				l.setHouseNumber(json.getInt("HouseNumber"));
				l.setPostalCode(json.getString("postal_code"));
				locations.add(l);

			} catch (JSONException e) {
				e.printStackTrace();

			}

		}
		return locations;
	}

	public List<Object> usersToList(JSONArray jsonArray) {

		// url = "http://eduweb.hhs.nl/~13061798/GetUsers.php";

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = null;
			try {

				json = jsonArray.getJSONObject(i);
				// Object o = new Object();

				User u = new User();

				u.setId(json.getInt("id"));
				u.setName(json.getString("name"));
				u.setEmail(json.getString("email"));
				u.setPwd(json.getString("password"));
				u.setPhone(json.getInt("phone_number"));
				u.setImageurl(json.getString("imageurl"));
				users.add(u);

			} catch (JSONException e) {
				e.printStackTrace();

			}

		}
		return users;
	}

	@Override
	protected List<Object> doInBackground(ApiConnector... params) {

		if (url.contains("Users")) {
			return usersToList(params[0].getTable(url));

		} else if (url.contains("Locations")) {
			return locationsToList(params[0].getTable(url));
		} else {
			usersToList(params[0].getTable(url));
			locationsToList(params[0].getTable(url));
			return appointmentsToList(params[0].getTable(url));
		}

	}

}
