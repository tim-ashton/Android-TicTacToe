package com.timashton.tictactoe;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.timashton.tictactoe.enums.*;


public class Game implements Serializable{

	private static final long serialVersionUID = 1L;

	private BoardSquaresState[][] gameBoard;
	private BoardSquaresState humanPlayer, computerPlayer;
	private Difficulty difficulty;

	public Game(){
		gameBoard = null;
		humanPlayer = BoardSquaresState.EMPTY;
		computerPlayer = BoardSquaresState.EMPTY;
	}

	public Game(BoardSquaresState hPlayer, BoardSquaresState cPlayer, Difficulty difficulty){

		gameBoard = new BoardSquaresState[Constants.BOARD_SIZE][Constants.BOARD_SIZE];

		// Fill each row with BoardSquaresState.EMPTY
		resetBoard();

		humanPlayer = hPlayer;
		computerPlayer = cPlayer;
		this.difficulty = difficulty;
	}

	/*
	 * Reset every square on the gameBoard back to PlayerType.NO_PLAYER
	 */
	public void resetBoard(){
		for (BoardSquaresState[] row: gameBoard)
			Arrays.fill(row, BoardSquaresState.EMPTY);
	}

	/*
	 * Generate a list of int[2] which are coordinates of available
	 * spaces on the playing board. 
	 * An empty list is returned if the game board is full
	 */
	public List<int[]> generateMoves(){
		List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List

		for(int i = 0; i < Constants.BOARD_SIZE; i++){
			for(int j = 0; j < Constants.BOARD_SIZE; j++){
				if (gameBoard[i][j] == BoardSquaresState.EMPTY){
					nextMoves.add(new int[] {i, j});
				}
			}
		} 	
		return nextMoves;
	}

	/* public boolean isWinner(BoardSquaresState player)
	 * 
	 * Check possible winning combinations. As soon as a winning
	 * combination is found return true otherwise return false.
	 */
	public boolean isWinner(BoardSquaresState player){

		if(checkRows(player)){
			return true;
		}
		else if(checkColumns(player)){
			return true;
		}
		else if(checkDiagRtoL(player)){
			return true;
		}
		else if(checkDiagLtoR(player)){
			return true;
		}
		else{
			return false;
		}
	}

	/*
	 * Check each row of a gameBoard to determine if the current player
	 * being checked is a winner.
	 */
	private boolean checkRows(BoardSquaresState player){
		for (int row = 0; row < Constants.BOARD_SIZE; row++){
			int count = 0;
			for (int col = 0; col < Constants.BOARD_SIZE; col++){
				if(gameBoard[row][col] == player){
					count++; // if any square is something else this player hasnt won that line
				}
				if(count == Constants.BOARD_SIZE){
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Check each column of a gameBoard to determine if the current player
	 * being checked is a winner.
	 */
	private boolean checkColumns(BoardSquaresState player){

		for (int col = 0; col < Constants.BOARD_SIZE; col++){
			int count = 0;
			for (int row = 0; row < Constants.BOARD_SIZE; row++){
				if(gameBoard[row][col] == player){

					count++; // if any square is something else this player hasnt won that line
				}
				if(count == Constants.BOARD_SIZE){
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Checks the diagonals (left to right) of gameBoard to check if the player is a winner 
	 */
	private boolean checkDiagRtoL(BoardSquaresState player){
		for(int i = 0; i < Constants.BOARD_SIZE; i++){
			if(gameBoard[i][i] != player){
				return false; // if any square is not this player the player has not won.
			}
		}
		return true; //if make it out of the loop player is the winner.
	}

	/*
	 * Checks diagonal from right to left to check if the player is a winner
	 */
	private boolean checkDiagLtoR(BoardSquaresState player){
		for(int i = 0; i < Constants.BOARD_SIZE; i++){
			if(gameBoard[i][(Constants.BOARD_SIZE -1) - i] != player){
				return false; // if any square is not this player the player has not won
			}
		}
		return true;  //if make it out of the loop player is the winner.
	}

	/*public boolean boardEmpty()
	 * 
	 * Returns true if the game board is empty
	 */
	public boolean boardEmpty(){
		for(int i = 0; i < Constants.BOARD_SIZE; i++){
			for(int j = 0; j < Constants.BOARD_SIZE; j++){
				if (gameBoard[i][j] != BoardSquaresState.EMPTY){
					return false;
				}	
			}
		}
		return true; //if get out of the loop then the board must be empty
	}

	/*
	 * Returns the human player setting -(nought or cross)
	 */
	public BoardSquaresState getHumanPlayer(){
		return humanPlayer;
	}

	/*
	 * Returns the current computer player for a game
	 */
	public BoardSquaresState getComputerPlayer(){
		return computerPlayer;
	}

	/*
	 * Return the difficulty setting for a game
	 */
	public Difficulty getDifficulty(){
		return this.difficulty;
	}

	/*
	 * Returns the state of a square with coordinates i,j
	 */
	public BoardSquaresState getStateOfSquare(int i, int j){
		return gameBoard[i][j];
	}

	/*
	 * Sets the state of a square with coordinates i,j
	 * with the playerType passed in
	 */
	public void setStateOfSquare(int i, int j, BoardSquaresState value){
		gameBoard[i][j] = value;
	}
}


