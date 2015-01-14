package kb50.appointment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import org.json.*;
import android.os.AsyncTask;
import android.util.Log;

public class Controller {

	class Select extends AsyncTask<ApiConnector, Long, List<Object>> {

		private String url;

		public Select(String url) {

			this.url = url;

		}

		private List<Object> users = new ArrayList<Object>();
		private List<Object> locations = new ArrayList<Object>();
		private List<Object> appointments = new ArrayList<Object>();

		@Override
		protected List<Object> doInBackground(ApiConnector... params) {

			if (url.contains("GetUsers")) {
				return usersToList(params[0].getTable(url));

			} else if (url.contains("GetLocations")) {

				return locationsToList(params[0].getTable(url));
			} else {
				
				if (params[0].getTable(url) == null) {

					
					
					List<Object> obs = new ArrayList<Object>();
					
					
					return obs;
				
				}
				else{
				
				usersToList(params[0].getTable(url));
				locationsToList(params[0].getTable(url));
				return appointmentsToList(params[0].getTable(url));
			}

			}

		}

		public List<Object> appointmentsToList(JSONArray jsonArray) {

			// url = "http://eduweb.hhs.nl/~13061798/GetAppointments.php";

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = null;

				try {

					json = jsonArray.getJSONObject(i);
					Appointment a = new Appointment();

					a.setId(json.getInt("id"));
					a.setDate(json.getString("datetime"));
					a.setName(json.getString("name"));
					a.setPriority(json.getInt("priority"));
					a.setDescription(json.getString("description"));
					for (Object o : locations) {
						Location l = (Location) o;
						if (l.getId() == json.getInt("locationid")) {

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

	}

	class Insert extends AsyncTask<ApiConnector, Long, List<Object>> {

		private String url;
		private Object o;

		public Insert(Object o, String url) {

			this.url = url;
			this.o = o;

		}

		@Override
		protected List<Object> doInBackground(ApiConnector... params) {

			// Building Parameters
			if (url.contains("CreateUser") || url.contains("EditUser")) {
				User u = (User) o;

				JSONParser jsonParser = new JSONParser();
				List<NameValuePair> insertParams = new ArrayList<NameValuePair>();
				insertParams.add(new BasicNameValuePair("name", u.getName()));
				insertParams.add(new BasicNameValuePair("email", u.getEmail()));
				insertParams
						.add(new BasicNameValuePair("password", u.getPwd()));
				insertParams.add(new BasicNameValuePair("phonenumber", u
						.getPhone() + ""));
				insertParams.add(new BasicNameValuePair("imageurl", u
						.getImageurl()));

				// getting JSON Object
				// Note that create product url accepts POST method
				JSONObject json = jsonParser.makeHttpRequest(url, "POST",
						insertParams);

				// check log cat for response
				Log.d("Create Response", json.toString());

				// check for success tag
				try {
					int success = json.getInt("succes");

					if (success == 1) {
						Log.d("succes", "succes");

					} else {
						Log.d("fail", "fail");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

	}

}
