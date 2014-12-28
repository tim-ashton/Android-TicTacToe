package com.timashton.tictactoe.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ListDialogFragment extends DialogFragment{
	// Use this instance of the interface to deliver action events
	private listDialogListener mListener;
	private int  mTitle;
	private int mItems;

	/*
	 * Create a new instance of EndGameDialogFragment, String dialogTitle
	 * and ing dialogListItems as an argument.
	 */
	public static ListDialogFragment newInstance(int dialogTitle, int dialogListItems){
		ListDialogFragment frag = new ListDialogFragment();
		Bundle args = new Bundle();
		args.putInt("title", dialogTitle);
		args.putInt("items", dialogListItems);
		frag.setArguments(args);
		return frag;
	}


	/* The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks.
	 * Each method passes the DialogFragment in case the host needs to query it. 
	 */
	public interface listDialogListener{
		public void onDialogClick(DialogFragment dialog, int which);
	}

	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);

		// Verify that the host activity implements the callback interface
		try{
			// Instantiate the NoticeDialogListener events can be sent to the host
			mListener = (listDialogListener) activity;
		} 
		catch (ClassCastException e){
			// If the activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement listDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){

		mTitle = getArguments().getInt("title"); //retrieve the titleString
		mItems = getArguments().getInt("items"); //retrieve array of items for the list (from strings.xml)

		//build the dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(mTitle);
		builder.setItems(mItems, new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which){

				switch(which){
				case 0:
					mListener.onDialogClick(ListDialogFragment.this, which);
					break;
				case 1:
					mListener.onDialogClick(ListDialogFragment.this, which);
					break;
				case 2:
					mListener.onDialogClick(ListDialogFragment.this, which);
					break;
				default:
					break;
				}
			}
		});
		return builder.create();
	}
}
