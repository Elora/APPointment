package kb50.appointment;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditAppointment extends FragmentActivity {

	private Spinner prioritySpinner;
	private static Button datePicker;
	private static Button timePicker;
	private static Button reminderDatePicker;
	private static Button reminderTimePicker;

	private EditText name;
	private EditText description;
	private EditText location;
	
	private int appointment_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_appointment);
		
		Intent i = getIntent();
		appointment_id = Integer.parseInt(i.getStringExtra("id"));
		
		// TODO: Fill ALL fields
		try {
			final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext()); 
			int id = mSharedPreference.getInt("id", 0);
			
			List<Object> appointments = new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/GetAppointments.php?id=" + id)
					.execute(new ApiConnector()).get();
			
			if(!appointments.isEmpty()){
				for(Object o : appointments){
					Appointment a = (Appointment) o;
					
					if(a.getId() == appointment_id){
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
		// TODO: write data to database
		Toast.makeText(this, "push to DB", Toast.LENGTH_SHORT).show();

		String priority = prioritySpinner.getSelectedItem().toString();
		int p = setPriority(priority);

		Appointment a = new Appointment();
		a.setName(name.getText().toString());
		a.setDescription(description.getText().toString());
		a.setDate(datePicker.getText().toString());
		a.setLocation(null); // TODO: ADD locations (Domi is working on this)
		a.setPriority(p);

		new Controller().new Insert(a,
				"http://eduweb.hhs.nl/~13061798/CreateAppointment.php")
				.execute(new ApiConnector());
		this.finish();
	}

	public void onClickDatePicker(View v) {
		switch (v.getId()) {
		case R.id.date_picker:
			DialogFragment dateFragment = new DatePickerFragment();
			Bundle args = new Bundle();
			args.putString("btn", "normal");
			dateFragment.setArguments(args);
			dateFragment.show(getSupportFragmentManager(), "datePicker");
			break;
		case R.id.reminder_date_picker:
			DialogFragment reminderDateFragment = new DatePickerFragment();
			Bundle args2 = new Bundle();
			args2.putString("btn", "reminder");
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
			args.putString("btn", "normal");
			timeFragment.setArguments(args);
			timeFragment.show(getSupportFragmentManager(), "timePicker");
			break;
		case R.id.reminder_time_picker:
			DialogFragment reminderTimeFragment = new TimePickerFragment();
			Bundle args2 = new Bundle();
			args2.putString("btn", "reminder");
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
}
