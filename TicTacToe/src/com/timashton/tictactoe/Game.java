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
    
 	public Game()
 	{
 		gameBoard = null;
		humanPlayer = BoardSquaresState.EMPTY;
		computerPlayer = BoardSquaresState.EMPTY;
	}
 	
 	public Game(BoardSquaresState hPlayer, BoardSquaresState cPlayer, Difficulty difficulty)
 	{
 		gameBoard = new BoardSquaresState[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
		
 		// Fill each row with 0
		for (BoardSquaresState[] row: gameBoard)
		    Arrays.fill(row, BoardSquaresState.EMPTY);
		
		humanPlayer = hPlayer;
		computerPlayer = cPlayer;
		this.difficulty = difficulty;
	}
 	
 	
 	/*
 	 * Reset every square on the gameBoard back to PlayerType.NO_PLAYER
 	 */
 	public void resetBoard()
 	{
 		// Fill each row with 0
 		for (BoardSquaresState[] row: gameBoard)
 			Arrays.fill(row, BoardSquaresState.EMPTY);
 	}
  
    /*
     * Returns true if the game board is empty
     * otherwise at least one space is used so return false
     */
     public boolean isBoardEmpty()
    {
    	for(int i = 0; i < Constants.BOARD_SIZE; i++)
    	{
    		for(int j = 0; j < Constants.BOARD_SIZE; j++)
    		{
    			if (gameBoard[i][j] != BoardSquaresState.EMPTY)
    				return false;
    		}
    	}
    	
    	//if we make it out of the loop then the board must be empty
    	return true;
    }
    

    /*
     * Generate a list of int[2] which are coordinates of available
     * spaces on the playing board. 
     * An empty list is returned if the game board is full
     */
	public List<int[]> generateMoves()
	{
		List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List
		
		for(int i = 0; i < Constants.BOARD_SIZE; i++)
    	{
    		for(int j = 0; j < Constants.BOARD_SIZE; j++)
    		{
    			if (gameBoard[i][j] == BoardSquaresState.EMPTY)
    				nextMoves.add(new int[] {i, j});
    		}
    	} 	
	   return nextMoves;
	}
    
    

	/*
	 * Determines if there is a winner of this game
	 * Checks the board to see if "player" is the winner
	 */
	public BoardSquaresState getWinner(BoardSquaresState player, int rowNumber, int columnNumber) 
	{
		boolean hasWinner = false;
		BoardSquaresState[] results = new BoardSquaresState[4];
		results[0] = checkRow(player, rowNumber, columnNumber);
		results[1] = checkColumn(player, rowNumber, columnNumber);
		results[2] = checkDiagR(player, rowNumber, columnNumber);
		results[3] = checkDiagL(player, rowNumber, columnNumber);

		for(int i = 0; i < results.length; i++)
		{
			if(results[i] == player) 
			{
					hasWinner = true;
					break; //done here
			}
		}
		
		if(hasWinner)
		{
			return player;
		}
		else
		{
			return BoardSquaresState.EMPTY;
		}

	}
	
	/*
	 * Checks each row of a gameBoard to determine if there is a winner
	 */
	private BoardSquaresState checkRow(BoardSquaresState player, int rowNumber, int columnNumber)
	{
		for(int i = 0; i < Constants.BOARD_SIZE; i++)
		{
			if(gameBoard[rowNumber][i] != player)
			{
				return BoardSquaresState.EMPTY;
			}
		}
		//if we make it out of the loop currentPlayer is the winner
		return player;
	}
	
	/*
	 * Checks each column of a gameBoard to check for a winner
	 */
	private BoardSquaresState checkColumn(BoardSquaresState player, int rowNumber, int columnNumber)
	{
		for(int i = 0; i < Constants.BOARD_SIZE; i++)
		{
			if(gameBoard[i][columnNumber] != player)
			{
				return BoardSquaresState.EMPTY;
			}
		}
		//if we make it out of the loop currentPlayer is the winner
		return player;
	}
	
	/*
	 * Checks the diagonals (left to right) of gameBoard to check for a winner 
	 */
	private BoardSquaresState checkDiagR(BoardSquaresState player, int rowNumber, int columnNumber)
	{
		for(int i = 0; i < Constants.BOARD_SIZE; i++)
		{
			//if any of the requirements for winning are not met. return
			if(gameBoard[i][i] != player)
			{
				return BoardSquaresState.EMPTY;
			}
		}
		//if we make it out of the loop currentPlayer is the winner
		return player;
	}
		
  	/*
  	 * Checks diagonal from right to left for a winner
  	 */
	private BoardSquaresState checkDiagL(BoardSquaresState player, int rowNumber, int columnNumber)
	{
		for(int i = 0; i < Constants.BOARD_SIZE; i++)
		{
			//if any of the requirements for winning are not met. return 
			if(gameBoard[i][(Constants.BOARD_SIZE -1) - i] != player) 
			{
				return BoardSquaresState.EMPTY;
			}
		}
		//if we make it out of the loop then the player won
		return player;
	}
	
	
	
	/*
	 * Getters
	 */
	
	/*
	 * Returns the  current human player nought or cross
	 */
	public BoardSquaresState getHumanPlayer()
	{
		return humanPlayer;
	}
	
	/*
	 * Returns the current computer player
	 */
	public BoardSquaresState getComputerPlayer()
	{
		return computerPlayer;
	}
	
	public Difficulty getDifficulty()
	{
		return this.difficulty;
	}
	
	
    /*
     * Returns the state of a square with coordinates i,j
     */
    public BoardSquaresState getStateOfSquare(int i, int j)
    {
    	return gameBoard[i][j];
    }
	
    
    
    /*
     * Setters
     */
    
    /*
     * Sets the state of a square with coordinates i,j
     * with the playerType passed in
     */
    public void setStateOfSquare(int i, int j, BoardSquaresState value)
    {
    	gameBoard[i][j] = value;
    }
    
    
}


