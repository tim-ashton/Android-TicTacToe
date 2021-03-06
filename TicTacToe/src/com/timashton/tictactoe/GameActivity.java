package com.timashton.tictactoe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


import com.timashton.tictactoe.dialogs.ListDialogFragment;
import com.timashton.tictactoe.enums.*;

public class GameActivity extends Activity 
implements ListDialogFragment.listDialogListener{
	public static final int	NO_PLAYER = 0;
	public static final int CROSS = 0;
	public static final int NOUGHT = 0;

	private static final String  END_GAME_DIALOG_TAG = "EndGameDialog";

	//everything we need to know about the game
	private Game game;

	//store imageViews of the game board
	private ImageView[][] gameSquares;

	// check if device was turned for re-creating activity
	private boolean deviceturned;

	//the computer player
	private AIPlayer ai;

	private ImageView playerImage;
	private TextView gameDifficulty;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_activity);
		Log.i(this.getClass().getName(), "+++ ON CREATE GAME +++");

		playerImage = (ImageView)findViewById(R.id.game_title_player_icon);
		gameDifficulty = (TextView)findViewById(R.id.game_title_difficulty);

		if(savedInstanceState == null)
			deviceturned = false;
		else
			deviceturned = true;

		// for the initial start up
		if(game == null) game = new Game();
	}

	@Override
	public void onStart(){
		super.onStart();
		Log.i(this.getClass().getName(), "onStart called - Reading game data from file");
		loadGame();
		Log.i(this.getClass().getName(), "onStart exiting - game data retrieved from file");
	}

	@Override
	protected void onResume(){
		super.onResume();
		Log.i(this.getClass().getName(), "++ ON resume game ++");

		if(deviceturned == false){
			//create a new intent to put the one that started this activity
			Intent intent = getIntent();

			//retrieve the bundle that was sent in the intent
			Bundle recievedBundle = 
					intent.getBundleExtra(Constants.GAME_INTENT_BUNDLE);

			//placeholders for what was received in the bundle
			BoardSquaresState humanPlayer;
			BoardSquaresState computerPlayer;

			//get the human player passed from the menu
			humanPlayer = 
					(BoardSquaresState) recievedBundle.getSerializable(
							Constants.HUMAN_PLAYER);

			//get the computer player passed from the menu
			computerPlayer = (BoardSquaresState) recievedBundle.getSerializable(
					Constants.COMPUTER_PLAYER);

			//create the new game
			game = new Game(
					humanPlayer, 
					computerPlayer,
					(Difficulty) recievedBundle.getSerializable(Constants.DIFFICULTY));
		}
		init();
	}

	@Override
	public void onStop(){
		super.onStop();
		Log.i(this.getClass().getName(), "onStop called - writing game data to file");
		saveGame();
		Log.i(this.getClass().getName(), "onStop exiting - game data written to file");
	}

	/*
	 * Handle if back button is pressed during gameplay
	 * Make sure the game is destroyed
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if ((keyCode == KeyEvent.KEYCODE_BACK)){
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/* private void init()
	 * 
	 * Initializes a new game.
	 */
	private void init(){
		Log.i(this.getClass().getName(), "Enter: init()");

		if(game.getDifficulty() == Difficulty.EASY){
			gameDifficulty.setText(R.string.game_difficulty_easy);
		}
		else if(game.getDifficulty() == Difficulty.MEDIUM){
			gameDifficulty.setText(R.string.game_difficulty_medium);
		}
		else{
			gameDifficulty.setText(R.string.game_difficulty_hard);
		}

		if(game.getHumanPlayer() == BoardSquaresState.CROSS){
			playerImage.setImageResource(R.drawable.cross_2);
		}
		else{
			playerImage.setImageResource(R.drawable.nought_2);
		}

		ai = new AIPlayer(game.getComputerPlayer(), game.getHumanPlayer(), game.getDifficulty());
		setUpGameBoard();

		if((game.getComputerPlayer() == BoardSquaresState.CROSS) && game.boardEmpty())	{
			makeFirstAIMove();
		}
	}

	/* private void fireComputerTurn()
	 * 
	 * Get a move from the AI for the computer
	 * Assign the move to the gameboard and
	 * Assign the computer player's image (X or O) to the screen
	 * and check if the computer has won the game.
	 */
	private void fireComputerTurn()	{
		Log.i(this.getClass().getName(), "Enter: fireComputerTurn()");
		final int[] bestMove = ai.move(game);

		// Assign the computer move
		game.setStateOfSquare(bestMove[0], bestMove[1], game.getComputerPlayer());

		//assign the image to the game board depending on computer Playertype
		if(game.getComputerPlayer() == BoardSquaresState.CROSS){
			gameSquares[bestMove[0]][bestMove[1]].setImageResource(R.drawable.cross_1);
		}
		else{
			gameSquares[bestMove[0]][bestMove[1]].setImageResource(R.drawable.nought_1);
		}

		if(game.isWinner(game.getComputerPlayer())){
			handleEndGame(game.getComputerPlayer());
		}
		else if(game.generateMoves().isEmpty()){ //this game is a draw
			handleEndGame(BoardSquaresState.EMPTY);
		}
	}

	/* handleEndGame(BoardSquaresState winner)
	 * 
	 * handle displaying dialogs for the end of game
	 * 
	 */
	public void handleEndGame(BoardSquaresState winner){
		int endGameDialogTitle = 0;
		if(winner == game.getHumanPlayer()){
			endGameDialogTitle = R.string.you_win;
		}
		else if(winner == game.getComputerPlayer()){
			endGameDialogTitle = R.string.you_lose;
		}
		else if(winner == BoardSquaresState.EMPTY){
			endGameDialogTitle = R.string.game_drawn;
		}
		showEndGameDialog(endGameDialogTitle);
	}

	/* private void setUpGameBoard()
	 * 
	 * fill game square with the declared square from game_activity.xml and
	 * attaches onclick listener to each.
	 */
	private void setUpGameBoard(){
		Log.i(this.getClass().getName(), "Enter: setUpGameBoard()");

		//initialize the gameSquares array to size of board
		gameSquares = new ImageView[Constants.BOARD_SIZE][Constants.BOARD_SIZE];

		int squaresCount = 0;

		for(int i = 0; i < Constants.BOARD_SIZE; i++){
			for(int j = 0; j < Constants.BOARD_SIZE; j++){ 

				//attach each gameSquare to the declared imageview
				String squareID = "square_" + squaresCount++;
				int resID = getResources().getIdentifier(squareID, "id", getPackageName());
				gameSquares[i][j] = (ImageView)findViewById(resID);

				//assign drawables depending on state
				if(game.getStateOfSquare(i, j) == BoardSquaresState.EMPTY){
					gameSquares[i][j].setImageResource(android.R.color.transparent);
				}
				else if(game.getStateOfSquare(i, j) == BoardSquaresState.CROSS){
					gameSquares[i][j].setImageResource(R.drawable.cross_1);
				}
				else if(game.getStateOfSquare(i, j) == BoardSquaresState.NOUGHT){
					gameSquares[i][j].setImageResource(R.drawable.nought_1);
				}

				//add the onclick listener for the game squares
				addOnClickListenerToSquare(i, j);
			}
		}
	}


	/* addOnClickListenerToSquare(final int i, final int j)
	 * 
	 * Add a Listener to the coordinate defined by i,j
	 * Listener provides the user interaction with the game play.
	 */
	private void addOnClickListenerToSquare(final int i, final int j){
		gameSquares[i][j].setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(game.getStateOfSquare(i, j) == BoardSquaresState.EMPTY){
					//set the state of the game board to the player
					//at the position i,j
					game.setStateOfSquare(i, j, game.getHumanPlayer());

					// set the image on the game board to the current
					// human player's player type
					if(game.getHumanPlayer() == BoardSquaresState.CROSS){
						gameSquares[i][j].setImageResource(R.drawable.cross_1);
					}
					else{
						gameSquares[i][j].setImageResource(R.drawable.nought_1);
					}

					// Check if the player won and return if it is the 
					// case we do not want to continue as the game is over
					if(game.isWinner(game.getHumanPlayer())){
						handleEndGame(game.getHumanPlayer());
						return;
					}
					else if(game.generateMoves().isEmpty()){ //this game is a draw
						handleEndGame(BoardSquaresState.EMPTY);
					}
					else{
						// fire a computer turn after the player has gone
						fireComputerTurn();
					}
				}
			}	
		});
	}


	/* resetGame()
	 * 
	 * Reset the game back to the original state at the start
	 * - restart the same game if selected in the dialog
	 */
	private void resetGame() {
		//remove all existing images
		for(int i = 0; i < Constants.BOARD_SIZE; i++){
			for(int j = 0; j < Constants.BOARD_SIZE; j++){
				gameSquares[i][j].setImageResource(0);
			}
		}
		game.resetBoard();
		init();
	}


	@Override
	public void onDialogClick(DialogFragment dialog, int which){
		switch (which){
		case 0:
			resetGame();
			break;
		case 1:    
			finish(); //go back to the menu
			break;
		default:
			break;
		}
	}

	/* showEndGameDialog(int dialogTitle)
	 * 
	 * Show the dialog at the end of each game.
	 */

	private void showEndGameDialog(int dialogTitle) {

		int itemsList = R.array.end_game_options;

		// DialogFragment.show() takes care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag(END_GAME_DIALOG_TAG);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = ListDialogFragment.newInstance(dialogTitle, itemsList);
		newFragment.setCancelable(false);
		newFragment.show(ft, END_GAME_DIALOG_TAG);
	}


	/*
	 * private void makeFirstAIMove() 
	 * Make the first move in the game if the computer is
	 * cross.
	 */
	private void makeFirstAIMove(){
		if(game.getDifficulty() != Difficulty.HARD){
			Random rand = new Random();
			int i = rand.nextInt(Constants.BOARD_SIZE);
			int j = rand.nextInt(Constants.BOARD_SIZE);
			gameSquares[i][j].setImageResource(R.drawable.cross_1);
			game.setStateOfSquare(i, j, BoardSquaresState.CROSS);
		}
		else{
			//take the middle if hard [1][1]
			gameSquares[1][1].setImageResource(R.drawable.cross_1);
			game.setStateOfSquare(1, 1, BoardSquaresState.CROSS);
		}
	}


	/* saveGame()
	 * Writes the game object out to file
	 * 
	 * There was a continue game option in the original prototype.
	 * It was removed as unnecessary feature. Continuing to use
	 * this implementation.
	 * 
	 * This is now only used when the device is turned.
	 */
	private void saveGame()	{
		Log.i(this.getClass().getName(), "Entry: saveGame()");
		try {
			FileOutputStream fos = openFileOutput(Constants.GAMEBOARD_SAVE_DATA, Context.MODE_PRIVATE);
			try {
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(game);
				oos.close();
			} 
			catch (IOException e){
				Log.e(this.getClass().getName(), e.toString());;
			}
		} 
		catch (FileNotFoundException e){
			Log.e(this.getClass().getName(), e.toString());
		}
	}


	/* loadGame()
	 * 
	 * Reads the save game from file
	 */
	private void loadGame()	{
		Log.i(this.getClass().getName(), "Enter: loadGame()");
		try{
			FileInputStream gameBoardIn = openFileInput(Constants.GAMEBOARD_SAVE_DATA);
			try	{
				ObjectInputStream ois = new ObjectInputStream(gameBoardIn);
				game = (Game)ois.readObject();
				ois.close();

			} 
			catch (IOException e) 	{
				Log.e(this.getClass().getName(), e.toString());
			} 
			catch (ClassNotFoundException e) {
				Log.e(this.getClass().getName(), e.toString());
			}
		} catch (FileNotFoundException e) {
			Log.e(this.getClass().getName(), e.toString());
		}
	}
}