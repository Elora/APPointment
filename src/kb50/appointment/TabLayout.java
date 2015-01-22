package kb50.appointment;

import java.util.HashMap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabContentFactory;

public class TabLayout extends FragmentActivity implements TabHost.OnTabChangeListener {
	private TabHost mTabHost;
	private HashMap mapTabInfo = new HashMap();
	private TabInfo mLastTab = null;
	private Resources res;
	private PendingIntent pendingIntent;
	private AlarmManager manager;
	private class TabInfo{
		private String tag;
		private Class clss;
		private Bundle args;
		private Fragment fragment;
		
		
		TabInfo(String tag, Class clss, Bundle args){
			this.tag = tag;
			this.clss = clss;
			this.args = args;
		}
	}
	
	class TabFactory implements TabContentFactory {
		 
        private final Context mContext;
 
        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }
 
        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
 
    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

		res = getResources();
        initializeTabHost(savedInstanceState);
        if(savedInstanceState != null){
        	mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        
     
        Intent i = new Intent("Notifcation");
        i.setAction("kb50.appointment.servicebroadcastreceiverdemo.NOTIFCATION_RECEIVER");
        sendBroadcast(i);
        
    }
    
    public void onClickNewApp(View v){
		Intent i = new Intent(TabLayout.this, NewAppointment.class);
		
		startActivityForResult(i,2);
	    
    }
    
    public void onClickRefresh(View v){
    	// TODO: find way to refresh AppointmentListFragment (preferably with notifyDataSetChanged method)
    }
    
    public void onClickEditProfiel(View v){
    	Intent i = new Intent(TabLayout.this, EditProfielActivity.class);
    	//startActivity(i);
    	startActivityForResult(i,1);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
            	
    			startActivity(new Intent(this, TabLayout.class));
    			//this.finish();
            	
                // Do something with the contact here (bigger example below)
            }
        }
        if (requestCode == 2) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
            	
    			startActivity(new Intent(this, TabLayout.class));
    			
                // Do something with the contact here (bigger example below)
            }
        }
    }
    
    protected void onSaveInstanceState(Bundle outState){
    	outState.putString("tab", mTabHost.getCurrentTabTag());
    	super.onSaveInstanceState(outState);
    }
    
    private void initializeTabHost(Bundle args){
    	mTabHost = (TabHost)findViewById(android.R.id.tabhost);
    	mTabHost.setup();
    	TabInfo tabInfo = null;
    	
    	TabLayout.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator(res.getString(R.string.Profiel)), (tabInfo = new TabInfo("Tab1", Profile.class, args)));
    	this.mapTabInfo.put(tabInfo.tag, tabInfo);
    	TabLayout.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator(res.getString(R.string.Appointment)), (tabInfo = new TabInfo("Tab2", AppointmentList.class, args)));
    	this.mapTabInfo.put(tabInfo.tag, tabInfo);
    	
    	//Default to first tab
    	this.onTabChanged("Tab1");
    	
    	mTabHost.setOnTabChangedListener(this);
    }

    private static void addTab(TabLayout activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo){
    	//Attach a tab view factory to the spec
    	tabSpec.setContent(activity.new TabFactory(activity));
    	String tag = tabSpec.getTag();
    	
    	//Check to see if we already have a fragment for this tab, probably]
    	//from a previously saved state. If so, deactivate it, because our
    	//initial state is that a tab isn't shown.
    	tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
    	if(tabInfo.fragment != null && !tabInfo.fragment.isDetached()){
    		FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
    		ft.detach(tabInfo.fragment);
    		ft.commit();
    		activity.getSupportFragmentManager().executePendingTransactions();
    	}
    	
    	tabHost.addTab(tabSpec);
    }
    
	@Override
	public void onTabChanged(String tag) {
		TabInfo newTab = (TabInfo) this.mapTabInfo.get(tag);
		if(mLastTab != newTab){
			FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
			if(mLastTab != null){
				if(mLastTab.fragment != null){
					ft.detach(mLastTab.fragment);
				}
			}
			if(newTab != null){
				if(newTab.fragment == null){
					newTab.fragment = Fragment.instantiate(this, newTab.clss.getName(), newTab.args);
					ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
				} else{
					ft.attach(newTab.fragment);
				}
			}
			
			mLastTab = newTab;
			ft.commit();
			this.getSupportFragmentManager().executePendingTransactions();
		}
		
	}
}
