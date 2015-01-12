package kb50.appointment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SendFragment extends DialogFragment{
	int mSelectedItem;
	
	
	static SendFragment newInstance(){
		SendFragment frag = new SendFragment();
		Bundle args = new Bundle();
		frag.setArguments(args);
		return frag;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.selectmessage)
	           .setSingleChoiceItems(R.array.Default_messages,0, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               // The 'which' argument contains the index position
	               // of the selected item
	            		mSelectedItem = which;
	               		
	               }
	               
	    })
	    .setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				((AppointmentInfoPage)
						getActivity()).doPositiveClickSendMessage(mSelectedItem);
			}
		})
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				((AppointmentInfoPage)
						getActivity()).doNegativeClickSendMessage();
			};
			});
	    return builder.create();

	}
	
}



