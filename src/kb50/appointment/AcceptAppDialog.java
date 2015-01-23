package kb50.appointment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AcceptAppDialog extends Activity {

	private String name;
	private String description;
	private String date;
	private String userId;
	private String appId;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		Context ctx = this;
		
		Intent i = getIntent();
		name = i.getStringExtra("appName");
		description = i.getStringExtra("appDescription");
		date = i.getStringExtra("appDate");
		userId = i.getStringExtra("userId");
		appId = i.getStringExtra("appId");
		
		final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
	    alert.setTitle(name);
	    alert.setMessage(description + "\n" + date);
	    
	    alert.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog,int whichButton)
			{
			
			new Controller().new Select("http://eduweb.hhs.nl/~13061798/SetAccepted.php?userid="+userId+"&appid="+appId+"&accepted=1").execute(new ApiConnector());

			Toast.makeText(getBaseContext(), "Appointment accepted!", Toast.LENGTH_SHORT).show();
			
			finish();
	
			}});
	    
	    alert.setNegativeButton("Decline", new DialogInterface.OnClickListener(){
	    	
	    	public void onClick(DialogInterface dialog,int whichButton)
			{
				new Controller().new Select("http://eduweb.hhs.nl/~13061798/SetAccepted.php?userid="+userId+"&appid="+appId+"&accepted=0").execute(new ApiConnector());
				Toast.makeText(getBaseContext(), "Appointment declined!", Toast.LENGTH_SHORT).show();
				dialog.cancel();
			finish();
	
			}});
	    
	      alert.create();
	      alert.show();
	}
	}

