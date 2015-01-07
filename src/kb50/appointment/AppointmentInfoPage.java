package kb50.appointment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AppointmentInfoPage extends Activity{

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        setContentView(R.layout.appointment_info_page_layout);
        Toast.makeText(this, "Item " + id + " was passed", Toast.LENGTH_SHORT).show();
        
    }
	
	
	public void onClick(View v){
		 switch(v.getId()) {
	        case R.id.button_alter:
	        	Toast.makeText(this, "going to alter table intent or ...", Toast.LENGTH_SHORT).show();
	            
	        	break;
	        case R.id.button_Back:
	        	finish();
	        	break;
	        case R.id.button_delete:
	        	Toast.makeText(this, "to delete appointment", Toast.LENGTH_SHORT).show();
	             
	        	break;
	        case R.id.button_sendMessage:
	        	Intent i = new Intent(this, SendMessageActivity.class);
	            startActivity(i);
	        	break;
	        case R.id.button_viewmap:
	        	Toast.makeText(this, "to be implemented, google maps", Toast.LENGTH_SHORT).show();
	            
	        	break;
	      }
	
	
	}
	
}
