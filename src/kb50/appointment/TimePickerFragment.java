package kb50.appointment;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

	String btn;
	 
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		Bundle args = getArguments();
		btn = args.getString("btn");
		
		// Use the current time as the default value
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
		
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {		
		String text; 
		
		if(minute < 10){
			String minuteString = "0" + minute;
			text = hourOfDay + ":" + minuteString;
			
		}else{
			text = hourOfDay + ":" + minute;
		}
		if(btn.equals("normal")){
			NewAppointment.setTimeButton(text);
		}else{
			NewAppointment.setReminderTimeButton(text);
		}
	}
}
