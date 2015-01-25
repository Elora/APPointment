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
import android.widget.Toast;

/**
 * 
 * This class shows a dialog box containing users that are registered in the
 * database and available in the contact list.
 * 
 */

public class AddGuestFragment extends DialogFragment {
	List<User> mSelectedItems = new ArrayList<User>();
	String[] usernames;
	List<User> users;

	static AddGuestFragment newInstance() {
		AddGuestFragment frag = new AddGuestFragment();
		Bundle args = new Bundle();
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		users = getAvailableUsers();
		usernames = new String[users.size()];
		for (int b = 0; b < users.size(); b++) {
			usernames[b] = users.get(b).getName();
		}
		builder.setTitle(R.string.app_name)
				.setMultiChoiceItems(usernames, null,
						new DialogInterface.OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								if (isChecked) {

									mSelectedItems.add(users.get(which));
								} else if (mSelectedItems.contains(which)) {

									mSelectedItems.remove(Integer
											.valueOf(which));
								}
							}

						})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Appointment a = new Appointment();
						a.setId(Integer.parseInt(getTag()));
						a.setUsers(mSelectedItems);

						new Controller().new Insert(a,
								"http://eduweb.hhs.nl/~13061798/AddGuests.php")
								.execute(new ApiConnector());
						((AppointmentInfoPage) getActivity())
								.doPositiveClickAddGuestsMessage(mSelectedItems);

						Toast t = Toast.makeText(getActivity()
								.getApplicationContext(), "Guests added!",
								Toast.LENGTH_SHORT);
						t.show();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								((AppointmentInfoPage) getActivity())
										.doNegativeClickAddGuestsMessage();
							};
						});
		return builder.create();

	}

	// returns registered users that are stored in the contact list.
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

			Cursor people = getActivity().getContentResolver().query(uri,
					projection, null, null, null);

			int indexNumber = people
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

			people.moveToFirst();

			while (people.moveToNext()) {
				String number = people.getString(indexNumber)
						.replaceAll("-", "").replaceAll("\\(", "")
						.replaceAll("\\)", "").replaceAll(" ", "")
						.replaceAll("\\+", "");
				
				if(number.startsWith("31")){
					
					number = "0"+number.substring(2);
					
				}

				for (Object o : userObject) {
					User u = (User) o;
					String phone = "0" + u.getPhone();
					
					if (phone.equals(number)) {

						users.add(u);

					}
				}
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
