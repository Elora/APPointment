package kb50.appointment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditAppointment extends FragmentActivity {

	private Spinner prioritySpinner;
	private Spinner locationSpinner;
	private static Button datePicker;
	private static Button timePicker;
	private static Button reminderDatePicker;
	private static Button reminderTimePicker;

	private static Button searchLocation;

	private ArrayAdapter<String> adapter;
	/*
	 * Number of times we will try to contact the google map server after
	 * timeouts to resolve an address to longitude and latitude
	 */
	private static final int maxretry = 5;
	/* Max Number of addresses that we get from resolving the address by name */
	private static final int maxaddresses = 5;

	private EditText name;
	private EditText description;
	private EditText location;

	private Geocoder gc;
	private Resources res;

	private double lat;
	private double lng;

	private int appointment_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_appointment);

		Intent i = getIntent();
		appointment_id = Integer.parseInt(i.getStringExtra("id"));

		// TODO: Fill ALL fields
		try {
			final SharedPreferences mSharedPreference = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			int id = mSharedPreference.getInt("id", 0);

			List<Object> appointments = new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/GetAppointments.php?id="
							+ id).execute(new ApiConnector()).get();

			if (!appointments.isEmpty()) {
				for (Object o : appointments) {
					Appointment a = (Appointment) o;

					if (a.getId() == appointment_id) {
						prioritySpinner = (Spinner) findViewById(R.id.priority_spinner);
						prioritySpinner.setSelection(a.getPriority());

						datePicker = (Button) findViewById(R.id.date_picker);
						String dateTime = a.getDate();
						String[] parts = dateTime.split("\\s+");
						String date = parts[0];
						String time = parts[1];
						datePicker.setText(date);

						timePicker = (Button) findViewById(R.id.time_picker);
						timePicker.setText(time);

						reminderDatePicker = (Button) findViewById(R.id.reminder_date_picker);
						reminderTimePicker = (Button) findViewById(R.id.reminder_time_picker);

						name = (EditText) findViewById(R.id.appointment_name_field);
						name.setText(a.getName());

						description = (EditText) findViewById(R.id.appointment_desc_field);
						description.setText(a.getDescription());

						location = (EditText) findViewById(R.id.location_field);
						locationSpinner = (Spinner) findViewById(R.id.location_spinner);
					}
				}

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		// Create an ArrayAdapter using the String array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.priority_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply adapter to the spinner
		prioritySpinner.setAdapter(adapter);
	}

	public void onClickCancel(View v) {
		startActivity(new Intent(EditAppointment.this,
				AppointmentInfoPage.class));
		this.finish();
	}

	public void onClickSubmit(View v) {

		String priority = prioritySpinner.getSelectedItem().toString();
		int p = setPriority(priority);

		Appointment a = new Appointment();
		a.setName(name.getText().toString());
		a.setDescription(description.getText().toString());
		a.setDate(datePicker.getText().toString() + " "
				+ timePicker.getText().toString());

		a.setLocation(location.getText().toString()); 

		a.setPriority(p);

		new Controller().new Insert(a,
				"http://eduweb.hhs.nl/~13061798/EditAppointment.php?id="
						+ appointment_id).execute(new ApiConnector());

		String[] date = reminderDatePicker.getText().toString().split("-");
		int day = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int year = Integer.parseInt(date[2]);

		String[] time = reminderTimePicker.getText().toString().split(":");
		int hour = Integer.parseInt(time[0]);
		int minute = Integer.parseInt(time[1]);

		Toast.makeText(this, hour + ":" + minute, Toast.LENGTH_LONG).show();

		// Calendar cur_cal = new GregorianCalendar();
		// cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current
		// time and date for this calendar

		Calendar cal = new GregorianCalendar();

		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.YEAR, year);

		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 00);

		Intent intentAlarm = new Intent(EditAppointment.this,
				AlarmReceiver.class);

		intentAlarm.putExtra("appointment name", name.getText().toString());
		intentAlarm.putExtra("date", datePicker.getText().toString());
		intentAlarm.putExtra("time", timePicker.getText().toString());
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				PendingIntent.getBroadcast(this, 1, intentAlarm,
						PendingIntent.FLAG_UPDATE_CURRENT));

		Toast.makeText(this, "Start Alarm", Toast.LENGTH_SHORT).show();

		this.finish();
	}

	public void onClickDatePicker(View v) {
		switch (v.getId()) {
		case R.id.date_picker:
			DialogFragment dateFragment = new DatePickerFragment();
			Bundle args = new Bundle();
			args.putString("btn", "editNormal");
			dateFragment.setArguments(args);
			dateFragment.show(getSupportFragmentManager(), "datePicker");
			break;
		case R.id.reminder_date_picker:
			DialogFragment reminderDateFragment = new DatePickerFragment();
			Bundle args2 = new Bundle();
			args2.putString("btn", "editReminder");
			reminderDateFragment.setArguments(args2);
			reminderDateFragment
					.show(getSupportFragmentManager(), "datePicker");
			break;
		}
	}

	public void onClickTimePicker(View v) {
		switch (v.getId()) {
		case R.id.time_picker:
			DialogFragment timeFragment = new TimePickerFragment();
			Bundle args = new Bundle();
			args.putString("btn", "editNormal");
			timeFragment.setArguments(args);
			timeFragment.show(getSupportFragmentManager(), "timePicker");
			break;
		case R.id.reminder_time_picker:
			DialogFragment reminderTimeFragment = new TimePickerFragment();
			Bundle args2 = new Bundle();
			args2.putString("btn", "editReminder");
			reminderTimeFragment.setArguments(args2);
			reminderTimeFragment
					.show(getSupportFragmentManager(), "timePicker");
			break;
		}
	}

	public static void setDateButton(String text) {
		datePicker.setText(text);
	}

	public static void setReminderDateButton(String text) {
		reminderDatePicker.setText(text);
	}

	public static void setTimeButton(String text) {
		timePicker.setText(text);
	}

	public static void setReminderTimeButton(String text) {
		reminderTimePicker.setText(text);
	}

	private int setPriority(String priority) {
		int p;

		if (priority.equals("High")) {
			p = 2;
		} else if (priority.equals("medium")) {
			p = 1;
		} else {
			p = 0;
		}

		return p;
	}

	private void hideSoftKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	public void onClickLocation(View v) {
		hideSoftKeyboard(v);

		gc = new Geocoder(this);

		String address = location.getText().toString();

		System.err.println(address + "!!!!!!!!!!!!!!!!!!!!!!!");

		try {
			if (Geocoder.isPresent()) {
				List<Address> addrList = null;

				boolean worked = false;
				int count = 0;
				while (!worked && count < maxretry) {
					try {
						addrList = gc
								.getFromLocationName(address, maxaddresses);
						worked = true;
					} catch (Exception te) {
						System.err.println(te
								+ " Exception occurred, will retry max "
								+ maxretry + " times");
						count++;
					}
				}
				if (addrList != null) {
					System.err.println("!!!!!! Found the following addresses:");
					// Create an ArrayAdapter using the String array and a
					// default spinner
					// layout
					ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
							this, android.R.layout.select_dialog_singlechoice);
					// Specify the layout to use when the list of choices
					// appears
					adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

					// Loop over all the addresses returned by google maps
					// and add them
					// into the spinner adapter array
					StringBuilder sb;
					for (Address addr : addrList) {
						// debug info can be deleted
						System.err.println(addr.getAddressLine(0)
								+ " Country: " + addr.getCountryName()
								+ " Postal Code: " + addr.getPostalCode()
								+ " Locality: " + addr.getLocality() + " lon: "
								+ addr.getLongitude() + " lat: "
								+ addr.getLatitude());

						// Add the address into the adapter for the spinner
						sb = new StringBuilder(addr.getAddressLine(0));
						// if (addr.getFeatureName() != null
						// && addr.getFeatureName().length() > 0) {
						// sb.append(", ").append(addr.getFeatureName());
						// }
						if (addr.getLocality() != null
								&& addr.getLocality().length() > 0) {
							sb.append(" ").append(addr.getLocality());
						}
						if (addr.getPremises() != null
								&& addr.getPremises().length() > 0) {
							sb.append(" ").append(addr.getPremises());
						}
						if (addr.getCountryCode() != null
								&& addr.getCountryCode().length() > 0) {
							sb.append(" ").append(addr.getCountryCode());
						}
						// if (addr.getAdminArea() != null
						// && addr.getAdminArea().length() > 0) {
						// sb.append("5 ").append(addr.getAdminArea());
						// }
						adapter.add(sb.toString());
					}
					// Apply adapter to the spinner
					// locationSpinner.setAdapter(adapter.createFromResource(this,
					// adapter.(android.R.layout.simple_spinner_item),
					// R.layout.spinner_item));
					locationSpinner.setAdapter(adapter);
					locationSpinner.performClick();
				}

				location.setText(locationSpinner.getSelectedItem().toString());

			} else {
				System.err
						.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! GEOCODER SERVICE IS NOT AVAILABLE");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
