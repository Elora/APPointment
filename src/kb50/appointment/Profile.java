package kb50.appointment;
// <!-- https://www.airpair.com/android/list-fragment-android-studio  
//http://www.perfectapk.com/android-listfragment-tutorial.html
//-->



//import android.content.res.Resources;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kb50.appointment.Controller.Select;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends Fragment{
	//private Resources res;
	private static View view;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	        view = inflater.inflate(R.layout.profile_layout, container, false);
	    } catch (InflateException e) {
	        /* map is already there, just return view as it is */
	    }
	    
	    User user = getUser();
	    
	    TextView UN = (TextView)view.findViewById(R.id.UserNamePL);
        UN.setText(user.getName());
        TextView UE = (TextView)view.findViewById(R.id.UserEmailPL);
        UE.setText(user.getEmail());
        TextView UP = (TextView)view.findViewById(R.id.UserPhonePL);
		String phone = Integer.toString(user.getPhone());
        UP.setText(phone);
        
        //Uri uri = Uri.parse(user.getImageurl()+ ".jpg");
        
        //ImageView UI = (ImageView)view.findViewById(R.id.ImagePL);
        //UI.setImageURI(uri);
        
        new DownloadImageTask((ImageView) view.findViewById(R.id.ImagePL))
        .execute(user.getImageurl());


        
        
        
	    return view;
		
		
        /*if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        
        
        return (LinearLayout)inflater.inflate(R.layout.profile_layout, container, false);*/
    }
	

	
	public User getUser(){

		final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()); 
		int id = mSharedPreference.getInt("id", 0);
		
		List<User> users = new ArrayList<User>();
		users = getProfiels();
		
		User user = new User();
		for(int i = 0; i<users.size(); i++){
			if (users.get(i).getId() == (id)){
				user=users.get(i);
			}
		}
		return user;
			
	}
	
	
	public List<User> getProfiels(){
		
		List<User> users = new ArrayList<User>();
		try {
			for (Object o : new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/GetUsers.php"
							).execute(
					new ApiConnector()).get()) {

				users.add((User) o);

			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}

	
	
}
	

	 

