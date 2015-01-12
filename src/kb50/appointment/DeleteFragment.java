package kb50.appointment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteFragment extends DialogFragment{

	
	static DeleteFragment newInstance(String title){
		DeleteFragment frag = new DeleteFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		String title = getArguments().getString("title");
		return new AlertDialog.Builder(getActivity())
		//.setIcon(R.drawable.icon)
		.setTitle(title)
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				((AppointmentInfoPage)
						getActivity()).doPositiveClick();
			}
		})
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				((AppointmentInfoPage)
						getActivity()).doNegativeClick();
			}
		}).create();
	}
}



