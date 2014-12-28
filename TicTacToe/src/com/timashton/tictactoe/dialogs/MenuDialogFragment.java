package com.timashton.tictactoe.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.timashton.tictactoe.R;
import com.timashton.tictactoe.enums.ListenerTypes;

public class MenuDialogFragment extends DialogFragment{
	private static final String TITLE = "title";
	private static final String ITEMS = "items";
	private static final String LISTENER = "listener";

	// Use these instance of the interface to deliver action events
	private PlayerTypeListener playerTypeListener;
	private DifficultyListener difficultyListener;

	private int  mTitle;
	private int mItems;

	/*
	 * Create a new instance of MainMenuDialogFragment send
	 * String dialogTitle, int dialogListItems and
	 * int requestedListener as an argument in the bundle
	 * for extraction onCreate.
	 */
	public static MenuDialogFragment newInstance(int dialogTitle, int dialogListItems, ListenerTypes listener){
		MenuDialogFragment frag = new MenuDialogFragment();
		Bundle args = new Bundle();
		args.putInt(TITLE, dialogTitle);
		args.putInt(ITEMS, dialogListItems);
		args.putSerializable(LISTENER, listener);
		frag.setArguments(args);
		return frag;
	}


	/* 
	 * MainMenu activity must implement each of these 
	 * to receive the required callbacks
	 * */
	//for choosing nought or cross
	public interface PlayerTypeListener{
		public void onPlayerDialogClick(DialogFragment dialog, int which);
	}
	//for choosing the difficulty
	public interface DifficultyListener{
		public void onDifficultyDialogClick(DialogFragment dialog, int which);
	}

	/*
	 * Override the Fragment.onAttach() method to instantiate listeners
	 * when this fragment is attached to the MainMenu Activity
	 */
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);

		// Verify that the MainMenu activity implements the callback interface
		try{
			// Get a handle to the listeners that have been instantiated
			// in the activity can send events to the host
			playerTypeListener = (PlayerTypeListener) activity;
			difficultyListener = (DifficultyListener) activity;
		} 
		catch (ClassCastException e){

			throw new ClassCastException(activity.toString()
					+ " must implement appropriate Listeners for MainMenuDialogFragment!");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		//retrieve the arguments from the bundle
		mTitle = getArguments().getInt(TITLE);
		mItems = getArguments().getInt(ITEMS);

		ListenerTypes requestedListener = (ListenerTypes) getArguments().getSerializable(LISTENER);

		//build the dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Show a title if there is one
		if(mTitle != R.string.null_dialog_title){
			builder.setTitle(mTitle);
		}

		//retrieve the listener that the Activity has requested
		builder.setItems(mItems, getRequestedListener(requestedListener));
		return builder.create();
	}

	/*
	 *  Creates the requested DialogInterface.OnclickListener
	 *  as per the button that is pressed
	 *  Used for sending callbacks to the activity
	 */
	private DialogInterface.OnClickListener getRequestedListener(ListenerTypes requestedListener){

		DialogInterface.OnClickListener listener = null;
		switch(requestedListener){
		case PLAYER_TYPE_LISTENER:
			listener = createPlayerTypeListener();
			break;
		case DIFFICULTY_LISTENER:
			listener = createDifficultyListener();
			break;
		}
		return listener;
	}

	/*
	 * Create a listener for selecting the player type
	 */
	private DialogInterface.OnClickListener createPlayerTypeListener(){
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch(which){
				case 0:
					playerTypeListener.onPlayerDialogClick(MenuDialogFragment.this, which);
					break;
				case 1:
					playerTypeListener.onPlayerDialogClick(MenuDialogFragment.this, which);
					break;
				case 2:
					playerTypeListener.onPlayerDialogClick(MenuDialogFragment.this, which);
					break;
				default:
					break;		
				}
			}
		};
		return listener;
	}


	/*
	 * Create a listener for selecting the difficulty
	 */
	private DialogInterface.OnClickListener createDifficultyListener(){
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which){

				switch(which){
				case 0:
					difficultyListener.onDifficultyDialogClick(
							MenuDialogFragment.this, which);
					break;
				case 1:
					difficultyListener.onDifficultyDialogClick(
							MenuDialogFragment.this, which);
					break;
				case 2:
					difficultyListener.onDifficultyDialogClick(
							MenuDialogFragment.this, which);
					break;
				default:
					break;
				}
			}
		};
		return listener;
	}
}
