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
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.RelativeLayout.LayoutParams;

import com.timashton.tictactoe.enums.*;

public class GameActivity extends Activity 
implements ListDialogFragment.listDialogListener
{
	public static final int	NO_PLAYER = 0;
	public static final int CROSS = 0;
	public static final int NOUGHT = 0;

	private static final String  END_GAME_DIALOG_TAG = "EndGameDialog";

	//everything we need to know about the game
	private Game game;

	//store all the imageViews
	private ImageView[][] gameSquares;

	// check if device was turned for re-creating activity
	private boolean deviceturned;

	// a tablelayout to hold the board squares
	private TableLayout board;

	//the computer player
	private AIPlayer ai;

	private boolean isGameOver;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_activity);
		Log.i("game", "+++ ON CREATE GAME +++");

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
		Log.e("game activity", "onStart called - Reading game data from file");
		loadGame();
		Log.e("game activity", "onStart exiting - game data retrieved from file");
	}


	@Override
	protected void onResume(){
		super.onResume();  // Always call the superclass method first
		Log.i("game", "++ ON resume game ++");

		if(deviceturned == false)
		{
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
		Log.e("game activity", "onStop called - writing game data to file");
		saveGame();
		Log.e("game activity", "onStop exiting - game data written to file");
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


	private void init(){

		isGameOver = false;
		ai = new AIPlayer(game.getComputerPlayer(), game.getHumanPlayer(), game.getDifficulty());
		setUpGameBoard();
		//addListenersToBoard();
		if(game.getComputerPlayer() == BoardSquaresState.CROSS)	{
			makeFirstAIMove();
		}
	}

	/*
	 * Get a move from the AI for the computer
	 * Assign the move to the gameboard and
	 * Assign the computer player's image (X or O) to the screen
	 * and check if the computer has won the game.
	 */
	private void fireComputerTurn()	{
		//get the AI move for this turn
		int[] bestMove = ai.move(game);

		//set the state of the gameboard to the computer player's type
		game.setStateOfSquare(bestMove[0], bestMove[1], game.getComputerPlayer());

		//assign the image to the game board depending on computer Playertype
		if(game.getComputerPlayer() == BoardSquaresState.CROSS){
			gameSquares[bestMove[0]][bestMove[1]].setImageResource(R.drawable.cross_1);
		}
		else{
			gameSquares[bestMove[0]][bestMove[1]].setImageResource(R.drawable.nought_1);
		}

		//check if the computer player has won the game with this move
		checkForEndGame(game.getComputerPlayer(), bestMove[0], bestMove[1]);
	}

	// return true if the game is finished
	private boolean checkForEndGame(BoardSquaresState currentPlayer, int rowNumber, int columnNumber){

		int endGameDialogTitle = 0;
		BoardSquaresState winner = BoardSquaresState.EMPTY;
		BoardSquaresState result = game.getWinner(currentPlayer, rowNumber, columnNumber);

		//TODO ... fix this mess below

		//first set the winner if there is one..
		if(result == BoardSquaresState.CROSS)
		{
			winner = BoardSquaresState.CROSS;
		}
		else if(result == BoardSquaresState.NOUGHT)
		{
			winner = BoardSquaresState.NOUGHT;
		}


		if(winner == game.getHumanPlayer())
		{
			endGameDialogTitle = R.string.you_win;
			isGameOver = true;
		}
		else if(winner == game.getComputerPlayer())
		{
			endGameDialogTitle = R.string.you_lose;
			isGameOver = true;
		}
		else if(game.generateMoves().isEmpty())
		{
			endGameDialogTitle = R.string.game_drawn;
			isGameOver = true;
		}

		if(isGameOver == true)
		{
			showEndGameDialog(endGameDialogTitle);
		}

		return isGameOver;
	}

	private void setUpGameBoard(){

		//initialize the gameSquares array to size of board
		gameSquares = new ImageView[Constants.BOARD_SIZE][Constants.BOARD_SIZE];

		//init the the TableLayout
		board = new TableLayout(this);
		board.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		board.setGravity(Gravity.CENTER);
		board.setBackgroundColor(Color.argb(0, 0, 0, 0)); //transparent

		//get the size of each square depending on the boardsize
		//TODO
		int squareSize = dpToPx(90);

		for(int i = 0; i < Constants.BOARD_SIZE; i++){
			TableRow tr = new TableRow(this);
			tr.setBackgroundColor(Color.argb(0, 0, 0, 0));//transparent
			tr.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			for(int j = 0; j < Constants.BOARD_SIZE; j++){
				//set up the area for each nought or cross 
				gameSquares[i][j] = new ImageView(this); // for the nought or cross
				TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(squareSize, squareSize); //size of each nought or cross
				gameSquares[i][j].setLayoutParams(layoutParams);
				gameSquares[i][j].setPadding(20, 20, 20, 20); //TODO
				setSquareBackground(i, j);

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

				//add the view to the row
				tr.addView(gameSquares[i][j]);
			}
			board.addView(tr);
		}

		this.addContentView(board, 
				new TableLayout.LayoutParams(
						TableLayout.LayoutParams.MATCH_PARENT, 
						TableLayout.LayoutParams.MATCH_PARENT)
				);
	}

	/*
	 * Add a Listener to the coordinate defined by i,j
	 * Listener provides the main interaction with the game play
	 */
	private void addOnClickListenerToSquare(final int i, final int j){
		gameSquares[i][j].setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

				if(game.getStateOfSquare(i, j) == BoardSquaresState.EMPTY){
					//set the state of the game board to the player
					//at the position x,y
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
					if(checkForEndGame(game.getHumanPlayer(), i, j) == true){
						return;
					}

					// fire a computer turn after the player has gone
					fireComputerTurn();
				}
			}	
		});
	}

	/*
	 * Convert a Density-independent pixels to a pixel value
	 * Can enter dp measurements for different screens and get 
	 * a value back in pixels for applying dynamic sizes
	 */
	private int dpToPx(int dp){
		float density = getApplicationContext().getResources().getDisplayMetrics().density;
		return Math.round((float)dp * density);
	}


	/*
	 * Set a particular square with the appropriate background image
	 * depending on the position of the square
	 */
	//TODO - move this to layout declaration
	private void setSquareBackground(int i, int j){
		if(i == 0) //top row
		{
			if(j == 0){  //top left
				gameSquares[i][j].setBackgroundResource(R.drawable.sq_top_left);
			}
			else if(j == (Constants.BOARD_SIZE - 1)){  //top right
				gameSquares[i][j].setBackgroundResource(R.drawable.sq_top_right);
			}
			else{
				gameSquares[i][j].setBackgroundResource(R.drawable.sq_top_center);
			}
		}
		else if(i == (Constants.BOARD_SIZE - 1)){ //bottom row 
			if(j == 0){ //bottom left
				gameSquares[i][j].setBackgroundResource(R.drawable.sq_bottom_left);
			}
			else if(j == (Constants.BOARD_SIZE - 1)){ //bottom right
				gameSquares[i][j].setBackgroundResource(R.drawable.sq_bottom_right);
			}
			else{
				gameSquares[i][j].setBackgroundResource(R.drawable.sq_bottom_center);
			}
		}
		else{
			if(j == 0){ // left
				gameSquares[i][j].setBackgroundResource(R.drawable.sq_left_center);
			}
			else if(j == (Constants.BOARD_SIZE - 1)){//right
				gameSquares[i][j].setBackgroundResource(R.drawable.sq_right_center);
			}
			else { // somewhere in the middle of the board
				gameSquares[i][j].setBackgroundResource(R.drawable.sq_center);
			}
		}
	}

	/*
	 * Reset the game back to the original state at the start
	 * - restart the same game if selected in the dialog
	 */
	private void resetGame() {
		//remove all existing images
		for(int i = 0; i < Constants.BOARD_SIZE; i++){
			for(int j = 0; j < Constants.BOARD_SIZE; j++){
				gameSquares[i][j].setImageResource(0);
				gameSquares[i][j].setBackgroundResource(0);
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

	private void showEndGameDialog(int dialogTitle) {

		int itemsList = R.array.end_game_options;
		// DialogFragment.show() will take care of adding the fragment
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
	 * private void makeFirstAIMove() used to make 
	 * the first move in the game if the computer is
	 * cross.
	 */
	private void makeFirstAIMove(){
		Random rand = new Random();
		int i = rand.nextInt(Constants.BOARD_SIZE);
		int j = rand.nextInt(Constants.BOARD_SIZE);
		gameSquares[i][j].setImageResource(R.drawable.cross_1);
		game.setStateOfSquare(i, j, BoardSquaresState.CROSS);
	}


	/*
	 * Writes the game object out to file
	 * 
	 * There was a continue game option in the original prototype.
	 * It was removed as unnecessary.
	 * 
	 * This is now only used when the device is turned.
	 * 
	 */
	private void saveGame()	{
		try {
			FileOutputStream fos = openFileOutput(Constants.GAMEBOARD_SAVE_DATA, Context.MODE_PRIVATE);
			try {
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(game);
				oos.close();
			} 
			catch (IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Reads the save game from file
	 */
	private void loadGame()	{
		try{
			FileInputStream gameBoardIn = openFileInput(Constants.GAMEBOARD_SAVE_DATA);
			try	{
				ObjectInputStream ois = new ObjectInputStream(gameBoardIn);
				game = (Game)ois.readObject();
				ois.close();
				Log.i("game", "++ We have read save data from file ++");
			} 
			catch (IOException e) 	{
				Log.e("game activity", "Unable to read from file:");
				// TODO Auto-generated catch block
				e.printStackTrace();

			} 
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}