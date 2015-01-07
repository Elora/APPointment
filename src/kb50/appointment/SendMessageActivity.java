package kb50.appointment;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SendMessageActivity extends Activity{

	EditText eTextMsg, eTextMblNumber;
	Spinner spinner;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message_lauout);
        eTextMblNumber = (EditText) findViewById(R.id.ToNumber);
        eTextMsg = (EditText) findViewById(R.id.Message);
	     
        
        spinner = (Spinner) findViewById(R.id.Default_messages);
	     // Create an ArrayAdapter using the string array and a default spinner layout
	     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	             R.array.Default_messages, android.R.layout.simple_spinner_item);
	     // Specify the layout to use when the list of choices appears
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     // Apply the adapter to the spinner
	     spinner.setAdapter(adapter);
	     
	     
        
    }
	
	public void SendMessage(View v){
		SmsManager sm = SmsManager.getDefault();
		String number = eTextMblNumber.getText().toString();
		
		String msg = spinner.getSelectedItem().toString();
		String msgextra = eTextMsg.getText().toString();
		
		sm.sendTextMessage(number, null, msg + " " + msgextra , null, null);
		
		
		Toast.makeText(this, msg + " " + msgextra + "sent", Toast.LENGTH_SHORT).show();
        
		
		finish();
	}
}
