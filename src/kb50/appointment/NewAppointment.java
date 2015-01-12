package kb50.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewAppointment extends FragmentActivity {

	private Spinner prioritySpinner;
	private static Button datePicker;
	private static Button timePicker;
	private static Button reminderDatePicker;
	private static Button reminderTimePicker;
	
	private EditText name;
	private EditText description;
	private EditText location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_appointment);
		
		prioritySpinner = (Spinner) findViewById(R.id.priority_spinner);
		datePicker = (Button) findViewById(R.id.date_picker);
		timePicker =(Button) findViewById(R.id.time_picker);
		reminderDatePicker = (Button) findViewById(R.id.reminder_date_picker);
		reminderTimePicker = (Button) findViewById(R.id.reminder_time_picker);
		
		name = (EditText) findViewById(R.id.appointment_name_field);
		description = (EditText) findViewById(R.id.appointment_desc_field);
		location = (EditText) findViewById(R.id.location_field);
		
		// Create an ArrayAdapter using the String array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,  R.array.priority_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply adapter to the spinner
		prioritySpinner.setAdapter(adapter);
	}
	
	public void onClickCancel(View v){
		startActivity(new Intent(NewAppointment.this, TabLayout.class)); 
	}
	
	public void onClickSubmit(View v){
		//TODO: write data to database
		Toast.makeText(this, "push to DB", Toast.LENGTH_SHORT).show();
		
	}
	
	public void onClickDatePicker(View v){
		switch(v.getId()){
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
			reminderDateFragment.show(getSupportFragmentManager(), "datePicker");	
			break;
		}			
	}
	
	public void onClickTimePicker(View v){
		switch(v.getId()){
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
			reminderTimeFragment.show(getSupportFragmentManager(), "timePicker");	
			break;
		}	
	}
	
	public static void setDateButton(String text){
		datePicker.setText(text);
	}
	
	public static void setReminderDateButton(String text){
		reminderDatePicker.setText(text);
	}
	
	public static void setTimeButton(String text){ 
		timePicker.setText(text);
	}
	
	public static void setReminderTimeButton(String text){
		reminderTimePicker.setText(text);
	}
}
