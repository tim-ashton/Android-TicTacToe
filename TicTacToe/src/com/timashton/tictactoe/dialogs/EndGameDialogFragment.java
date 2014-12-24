package com.timashton.tictactoe.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class EndGameDialogFragment extends DialogFragment{
	
	// Use this instance of the interface to deliver action events
	private endGameDialogListener mListener;
	private String  title;
	private int items;

    /*
     * Create a new instance of EndGameDialogFragment, String dialogTitle
     * and ing dialogListItems as an argument.
     */
    static EndGameDialogFragment newInstance(String dialogTitle, int dialogListItems){
    	
    	EndGameDialogFragment frag = new EndGameDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("title", dialogTitle);
        args.putInt("items", dialogListItems);
        frag.setArguments(args);

        return frag;
    }
    
    
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface endGameDialogListener{
        public void onDialogClick(DialogFragment dialog, int which);
    }
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) 
    {
        super.onAttach(activity);
        
        // Verify that the host activity implements the callback interface
        try{
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (endGameDialogListener) activity;
        } 
        catch (ClassCastException e){
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement endGameDialogListener");
        }
    }
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		title = getArguments().getString("title"); //retrive the titleString
		items = getArguments().getInt("items"); //retrive array of items for the list (from strings.xml)
		
		//build the dialog
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(title);
	    builder.setItems(items, new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface dialog, int which){
	    		
	    		switch(which){
		    		case 0:
		    			mListener.onDialogClick(EndGameDialogFragment.this, which);
		    			break;
		    		case 1:
		    			mListener.onDialogClick(EndGameDialogFragment.this, which);
		    			break;
		    		default:
		    			break;
	    		}
	        }
	    });
	    return builder.create();
	}
}
