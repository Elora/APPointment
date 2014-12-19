package kb50.appointment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LogIn extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }
    
    public void onClick(View v){
    	Intent i = new Intent(LogIn.this, TabLayout.class);
    	startActivity(i);
    }
}
