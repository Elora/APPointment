package kb50.appointment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddGuestFragment extends DialogFragment{
	 ArrayList mSelectedItems = new ArrayList();
	 String[] usernames;
	
	static AddGuestFragment newInstance(){
		AddGuestFragment frag = new AddGuestFragment();
		Bundle args = new Bundle();
		frag.setArguments(args);
		return frag;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		List<User> users = getAvailableUsers();
		usernames = new String[users.size()];
		for(int b=0; b<users.size(); b++){
			usernames[b] = users.get(b).getName();
		}
	    builder.setTitle(R.string.app_name).setMultiChoiceItems(usernames, null, new DialogInterface.OnMultiChoiceClickListener() {
				
	        	    @Override
	                public void onClick(DialogInterface dialog, int which,
	                        boolean isChecked) {
	                    if (isChecked) {
	                        // If the user checked the item, add it to the selected items
	                        mSelectedItems.add(which);
	                    } else if (mSelectedItems.contains(which)) {
	                        // Else, if the item is already in the array, remove it 
	                        mSelectedItems.remove(Integer.valueOf(which));
	                    }
	                }
	             
	              
	               
	    })
	    .setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				((AppointmentInfoPage)
						getActivity()).doPositiveClickAddGuestsMessage(mSelectedItems);
			}
		})
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				((AppointmentInfoPage)
						getActivity()).doNegativeClickAddGuestsMessage();
			};
			});
	    return builder.create();

	}
	
	// returns registered users that are stored in the contactlist.
	public List<User> getAvailableUsers() {

		List<User> users = new ArrayList<User>();
		try {
			List<Object> userObject = new Controller().new Select(
					"http://eduweb.hhs.nl/~13061798/GetUsers.php").execute(
					new ApiConnector()).get();
			Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			String[] projection = new String[] {
					ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
					ContactsContract.CommonDataKinds.Phone.NUMBER };

			Cursor people = getActivity().getContentResolver().query(uri, projection, null,
					null, null);

			int indexNumber = people
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

			people.moveToFirst();
			do {
				String number = people.getString(indexNumber)
						.replaceAll("-", "").replaceAll("\\(", "")
						.replaceAll("\\)", "").replaceAll(" ", "");

				for (Object o : userObject) {
					User u = (User) o;
					String phone = "0" + u.getPhone();
					if (Integer.parseInt(phone) == Integer.parseInt(number)) {

						users.add(u);

					}

				}

			} while (people.moveToNext());
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



