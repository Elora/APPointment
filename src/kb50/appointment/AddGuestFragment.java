package kb50.appointment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddGuestFragment extends DialogFragment{
	 ArrayList mSelectedItems = new ArrayList();
	
	
	static AddGuestFragment newInstance(){
		AddGuestFragment frag = new AddGuestFragment();
		Bundle args = new Bundle();
		frag.setArguments(args);
		return frag;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	    builder.setTitle(R.string.app_name)

	           .setMultiChoiceItems(R.array.Default_messages, null, new DialogInterface.OnMultiChoiceClickListener() {
				
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
	
}



