package com.timashton.tictactoe;

import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.timashton.tictactoe.dialogs.MenuDialogFragment;
import com.timashton.tictactoe.enums.*;

public class MenuActivity 
extends Activity 
implements View.OnClickListener, 
MenuDialogFragment.PlayerTypeListener,
MenuDialogFragment.DifficultyListener{

	private static final String PLAYER_DIALOG_TAG = "PlayerDialog";
	private static final String DIFFICULTY_DIALOG_TAG = "DifficultyDialog";

	// the buttons !
	private RelativeLayoutButton playerSelectionButton;
	private RelativeLayoutButton difficultyButton;
	private Button resetButton;
	private Button playButton;


	private Difficulty difficulty;
	private BoardSquaresState selectedPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(this.getClass().getName(), "Enter: onCreate().");

		//set layout to main menu layout after splash screen runs
		setContentView(R.layout.menu_activity);
		
		//Set up playerSelectionButton button
		playerSelectionButton = new RelativeLayoutButton(this, R.id.player_sel_button);
		playerSelectionButton.setText(R.id.button_title, "X or O");
		playerSelectionButton.setText(R.id.button_subtitle, "Play as Nought or Cross");
		playerSelectionButton.setOnClickListener(this);

		//Set up difficultyButton button
		difficultyButton = new RelativeLayoutButton(this, R.id.difficulty_sel_button);
		difficultyButton.setText(R.id.button_title, "Difficulty");
		difficultyButton.setText(R.id.button_subtitle, "Easy, Medium or Hard");
		difficultyButton.setOnClickListener(this);

		// set up resetButton button
		resetButton = (Button)findViewById(R.id.reset_button);
		resetButton.getBackground().setAlpha(35);	//as set in relativeButtonLayout
		resetButton.setOnClickListener(this);

		// Set up start button
		playButton = (Button)findViewById(R.id.play_button);
		playButton.getBackground().setAlpha(35);	 //as set in relativeButtonLayout
		playButton.setOnClickListener(this);	
	}

	@Override
	protected void onResume(){
		super.onResume();  // Always call the superclass method first
		Log.i(this.getClass().getName(), "++ ON resume ++");
		resetMenu();
	}
	/*
	 * public void onDifficultyDialogClick(DialogFragment dialog, int which)
	 * 
	 * handle clicks in the difficulty select dialog
	 */
	@Override
	public void onDifficultyDialogClick(DialogFragment dialog, int which) {
		Log.i(this.getClass().getName(), "onDifficultyDialogClick() - Entry");
		switch (which) {
		case 0: //easy
			difficulty = Difficulty.EASY;
			difficultyButton.setText(R.id.button_title, "Easy");
			difficultyButton.setText(R.id.button_subtitle, "Difficulty set to Easy");
			difficultyButton.setImageResource(R.id.selector_button_image, R.drawable.emo_im_laughing);
			difficultyButton.setImageResource(R.id.item_set_image, R.drawable.btn_check_on_focused_holo_light);
			break;
		case 1: //medium
			difficulty = Difficulty.MEDIUM;
			difficultyButton.setText(R.id.button_title, "Medium");
			difficultyButton.setText(R.id.button_subtitle, "Difficulty set to Medium");
			difficultyButton.setImageResource(R.id.selector_button_image, R.drawable.emo_im_happy);
			difficultyButton.setImageResource(R.id.item_set_image, R.drawable.btn_check_on_focused_holo_light);
			break;
		case 2: //hard
			difficulty = Difficulty.HARD;
			difficultyButton.setText(R.id.button_title, "Hard");
			difficultyButton.setText(R.id.button_subtitle, "Difficulty set to Hard");
			difficultyButton.setImageResource(R.id.selector_button_image, R.drawable.emo_im_sad);
			difficultyButton.setImageResource(R.id.item_set_image, R.drawable.btn_check_on_focused_holo_light);
			break;
		default:
			break;
		}
		//change display of buttons
		resetButton.setTextColor(getResources().getColor(R.color.ivory));
		if(selectedPlayer != null){
			playButton.setTextColor(getResources().getColor(R.color.ivory));
		}
	}

	/*
	 * public void onPlayerDialogClick(DialogFragment dialog, int which) 
	 * 
	 * handle clicks in the player select dialog
	 */
	@Override
	public void onPlayerDialogClick(DialogFragment dialog, int which) {
		Log.i(this.getClass().getName(), "onPlayerDialogClick() - Entry");
		switch (which) {
		case 0: //cross selected
			setPlayer(BoardSquaresState.CROSS);
			break;
		case 1: // Naught selected
			setPlayer(BoardSquaresState.NOUGHT);
			break;
		case 2: //random selection - make decision and set
			Random rand=new Random();
			if(rand.nextInt(101)%2 == 1) {
				setPlayer(BoardSquaresState.CROSS);
			}
			else {
				setPlayer(BoardSquaresState.NOUGHT);
			}
			break;
		default:
			break;
		}
		//change display of buttons
		resetButton.setTextColor(getResources().getColor(R.color.ivory));
		if(difficulty != null){
			playButton.setTextColor(getResources().getColor(R.color.ivory));
		}
	}

	/*
	 * onClick() handles all click events for this menu activity
	 */
	@Override
	public void onClick(View v) {
		Log.i(this.getClass().getName(), "onClick() - Entry");

		int buttonClicked = 
				(v instanceof RelativeLayoutButton)
				? ((RelativeLayoutButton)v).getId() : ((Button)v).getId();

				switch(buttonClicked)
				{
				case R.id.player_sel_button: 
					Log.i(this.getClass().getName(), "Player select button clicked.");
					showDialog(
							PLAYER_DIALOG_TAG, 
							R.string.select_player_dialog_title, 
							R.array.play_as,
							ListenerTypes.PLAYER_TYPE_LISTENER);
					break;
				case R.id.difficulty_sel_button: 
					Log.i(this.getClass().getName(), "Difficulty select button clicked.");
					showDialog(
							DIFFICULTY_DIALOG_TAG,
							R.string.select_difficulty_dialog_title,
							R.array.difficulties,
							ListenerTypes.DIFFICULTY_LISTENER);
					break;
				case R.id.play_button:
					Log.i(this.getClass().getName(), "Play button clicked.");

					//not ready to play
					if(difficulty == null || selectedPlayer == null){
						break;
					}
					//create the intent and bundle
					Intent launchIntent = new Intent(
							MenuActivity.this, com.timashton.tictactoe.GameActivity.class);
					Bundle extraData = new Bundle();

					//assign some values to the bundle
					extraData.putSerializable(Constants.HUMAN_PLAYER, selectedPlayer);
					extraData.putSerializable(Constants.COMPUTER_PLAYER, 
							(selectedPlayer == BoardSquaresState.CROSS) ? BoardSquaresState.NOUGHT : BoardSquaresState.CROSS);

					extraData.putSerializable(Constants.DIFFICULTY, difficulty);

					//assign bundle to launchIntent
					launchIntent.putExtra(Constants.GAME_INTENT_BUNDLE, extraData);

					//start the game
					startActivity(launchIntent);

					break;
				case R.id.reset_button:
					Log.i(this.getClass().getName(), "Reset button clicked.");
					resetMenu();
					break;
				}
	}

	/*
	 * Show an alert dialog
	 * Depending on button pressed the required dialog will be shown
	 * which will send callbacks to populate user selections
	 * on the menu screen.
	 */
	private void showDialog(String tag, int dialogTitle, int dialogList, ListenerTypes requestedListener){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag(tag);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = MenuDialogFragment.newInstance(
				dialogTitle, dialogList, requestedListener);
		newFragment.show(ft, tag);
	}

	/* private void resetMenu()
	 * 
	 * Reset the menu back to starting state
	 */
	private void resetMenu(){
		// Clean up the menu items first
		// reset player button
		playerSelectionButton.setText(R.id.button_title, "X or O");
		playerSelectionButton.setText(R.id.button_subtitle, "Play as Nought or Cross");
		playerSelectionButton.setImageResource(R.id.item_set_image, R.drawable.btn_check_off_focused_holo_light);
		playerSelectionButton.setImageResource(R.id.selector_button_image, R.drawable.ic_menu_help_holo_light);

		// reset difficulty button
		difficultyButton.setText(R.id.button_title, "Difficulty");
		difficultyButton.setText(R.id.button_subtitle, "Easy, Medium or Hard");
		difficultyButton.setImageResource(R.id.item_set_image, R.drawable.btn_check_off_focused_holo_light);
		difficultyButton.setImageResource(R.id.selector_button_image, R.drawable.ic_menu_help_holo_light);

		// reset play & reset buttons
		resetButton.setTextColor(getResources().getColor(R.color.transparent_grey));
		playButton.setTextColor(getResources().getColor(R.color.transparent_grey));
		difficulty = null;
		selectedPlayer = null;
	}

	/* private void setPlayer(BoardSquaresState player)
	 * 
	 * Set attributes for player and menu
	 */
	private void setPlayer(BoardSquaresState player){

		if(player == BoardSquaresState.CROSS){
			selectedPlayer = BoardSquaresState.CROSS;
			playerSelectionButton.setText(R.id.button_title, "Cross");
			playerSelectionButton.setText(R.id.button_subtitle, "You go first!");
			playerSelectionButton.setImageResource(R.id.selector_button_image, R.drawable.cross_1);
			playerSelectionButton.setImageResource(R.id.item_set_image, R.drawable.btn_check_on_focused_holo_light);
		}
		else{
			selectedPlayer = BoardSquaresState.NOUGHT;
			playerSelectionButton.setText(R.id.button_title, "Nought");
			playerSelectionButton.setText(R.id.button_subtitle, "Computer goes first!");
			playerSelectionButton.setImageResource(R.id.selector_button_image, R.drawable.nought_1);
			playerSelectionButton.setImageResource(R.id.item_set_image, R.drawable.btn_check_on_focused_holo_light);
		}

	}
}




