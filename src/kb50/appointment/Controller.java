package kb50.appointment;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class Controller extends AsyncTask<ApiConnector, Long, JSONArray> {

	private String url = "";
	public List<User> users = new ArrayList<User>();
	private List<Location> locations = new ArrayList<Location>();
	private List<Appointment> appointments = new ArrayList<Appointment>();

	public Controller(String url) {

		this.url = url;
		this.execute(new ApiConnector());
	}

	public void appointmentsToList(JSONArray jsonArray) {

		// url = "http://eduweb.hhs.nl/~13061798/GetAppointments.php";

	}

	public void locationsToList(JSONArray jsonArray) {

		// url = "http://eduweb.hhs.nl/~13061798/GetLocations.php";

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = null;
			try {

				json = jsonArray.getJSONObject(i);
				Location l = new Location();

				l.setId(json.getInt("id"));
				l.setCountry(json.getString("country"));
				l.setCity(json.getString("city"));
				l.setAddress(json.getString("address"));
				l.setHouseNumber(json.getInt("house_number"));
				l.setPostalCode(json.getString("postal_code"));
				locations.add(l);

			} catch (JSONException e) {
				e.printStackTrace();

			}

		}

	}

	public void usersToList(JSONArray jsonArray) {

		// url = "http://eduweb.hhs.nl/~13061798/GetUsers.php";

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = null;
			try {

				json = jsonArray.getJSONObject(i);
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

	}

	public List<Location> getLocations() {

		return locations;

	}

	public List<User> getUsers() {

		return users;

	}

	public List<Appointment> getAppointments() {

		return appointments;

	}

	@Override
	protected JSONArray doInBackground(ApiConnector... params) {
		return params[0].getTable(url);
	}

	@Override
	protected void onPostExecute(JSONArray jsonArray) {

		
		if (url.contains("Users")) {
			usersToList(jsonArray);
		
		}
		if (url.contains("Locations")) {
			locationsToList(jsonArray);
		}
		if (url.contains("Appointments")) {
			appointmentsToList(jsonArray);
		}

	}

}
