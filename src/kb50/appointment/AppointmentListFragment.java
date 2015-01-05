package kb50.appointment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class AppointmentListFragment extends ListFragment {

	//temp data
    String[] nameApp = new String[] {
        "docter",
        "school"};
    String[] dateApp = new String[]{
        "2 september 2015",
        "3 september 2015"};
    String[] timeApp = new String[]{
            "9:00 pm",
            "8:00 am"};
    //end temp data
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    		
        // Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
 
        for(int i=0;i<2;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("name", "name : " + nameApp[i]);
            hm.put("date","date : " + dateApp[i]);
            hm.put("time","time : " + timeApp[i]);
            
            //hm.put("flag", Integer.toString(flags[i]) );
            aList.add(hm);
        }
 
        // Keys used in Hashmap
        String[] from = { "name","date","time" };
        // Ids of views in profiellistfragment_layout
        int[] to = { R.id.name2,R.id.date2,R.id.time2};
 
        // Instantiating an adapter to store each items
        // R.layout.profiellistfragment_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.appointmentlistfragment_layout, from, to);
 
        setListAdapter(adapter);
 
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    
    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
      super.onListItemClick(l, v, pos, id);
      Toast.makeText(getActivity(), "Item " + pos + " was clicked", Toast.LENGTH_SHORT).show();
      Intent i = new Intent(getActivity(), AppointmentInfoPage.class);
      i.putExtra("ID", "id van de class");
      startActivity(i);
    }
	
}
