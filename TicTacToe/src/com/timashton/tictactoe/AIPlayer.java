package com.timashton.tictactoe;


import java.util.List;
import java.util.Random;

import android.util.Log;

import com.timashton.tictactoe.enums.BoardSquaresState;
import com.timashton.tictactoe.enums.Difficulty;



public class AIPlayer 
{
	private BoardSquaresState AIPlayer, opponent;
	private Game thisGame;
	private int searchDepth;
	private Difficulty difficulty;



	public AIPlayer(BoardSquaresState AIPlayer, BoardSquaresState Opponent, Difficulty diff){
		difficulty = diff;
		searchDepth = diff.getValue();
		this.AIPlayer = AIPlayer;
		this.opponent = Opponent;
		thisGame = null;
	}

	int[] move(Game gb){
		thisGame = gb;
		int[] result;
		
		//TODO - tidy up
		result = canWin();
		
		if(result == null){	
			result = alphaBeta(searchDepth, AIPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE);
			// depth, max-turn, alpha, beta
			return new int[] {result[1], result[2]};   // row, col
		}
		else{
			return new int[] {result[0], result[1]};
		}

		
	}


	/** Minimax (recursive) at level of depth for maximizing or minimizing player
    with alpha-beta cut-off. Return int[3] of {score, row, col}  */
	private int[] alphaBeta(int depth, BoardSquaresState player, int alpha, int beta) {
		// Generate possible next moves in a list of int[2] of {row, col}.
		List<int[]> nextMoves = thisGame.generateMoves();

		// this player is maximizing; while opponent is minimizing
		int score = 0;
		int bestRow = -1;
		int bestCol = -1;
		
		if (nextMoves.isEmpty()){
			Log.e(this.getClass().getName(), "Nextmoves is empty!!");
		}

		if (nextMoves.isEmpty() || depth == 0){  // Gameover or depth reached, evaluate score

			score = evaluate();

			//Log.e(this.getClass().getName(), "Returning- Score: "+score);
			return new int[] {score, bestRow, bestCol};
		} 
		else{
			for (int[] move : nextMoves){ // try this move for the current "player"

				thisGame.setStateOfSquare(move[0], move[1], player);
				if (player == AIPlayer) // me (computer) is maximizing player
				{  
					score = alphaBeta(depth - 1, opponent, alpha, beta)[0];
					if (score > alpha){
						alpha = score;
						bestRow = move[0];
						bestCol = move[1];
					}
				} 
				else { // opponent is minimizing player  
					score = alphaBeta(depth - 1, AIPlayer, alpha, beta)[0];
					if (score < beta) {
						beta = score;
						bestRow = move[0];
						bestCol = move[1];
					}
				}
				// undo move
				thisGame.setStateOfSquare(move[0], move[1], BoardSquaresState.EMPTY);

				// cut-off
				if (alpha >= beta) break;
			}
			return new int[] {(player == AIPlayer) ? alpha : beta, bestRow, bestCol};
		}
	}

	/* 
	 * The heuristic evaluation function for the current board
	 * Return +100, +10, +1 for EACH 3-, 2-, 1-in-a-line for computer.
	 * -100, -10, -1 for EACH 3-, 2-, 1-in-a-line for opponent.
	 * 0 otherwise
	 */
	private int evaluate(){
		int score = 0;
		// Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
		score += evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
		score += evaluateLine(1, 0, 1, 1, 1, 2);  // row 1
		score += evaluateLine(2, 0, 2, 1, 2, 2);  // row 2
		score += evaluateLine(0, 0, 1, 0, 2, 0);  // col 0
		score += evaluateLine(0, 1, 1, 1, 2, 1);  // col 1
		score += evaluateLine(0, 2, 1, 2, 2, 2);  // col 2
		score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
		score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
		return score;  
	}
	
	/*
	 *  The heuristic evaluation function for the given line of 3 cells
	 *  Return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
	 *  -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
	 *  0 otherwise 
	 */
	private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) 
	{
		int score = 0;

		// First cell
		if ( thisGame.getStateOfSquare(row1, col1)  == AIPlayer){
			score = 1;
		} 
		else if (thisGame.getStateOfSquare(row1, col1) == opponent){
			score = -1;
		}

		// Second cell
		if (thisGame.getStateOfSquare(row2, col2) == AIPlayer){
			if (score == 1){ // cell 1 is mySeed   
				score = 10;
			} 
			else if (score == -1){ // cell 1 is oppSeed  
				return 0;
			}
			else { // cell1 is empty  
				score = 1;
			}
		} 
		else if (thisGame.getStateOfSquare(row2, col2) == opponent) {
			if (score == -1){ // cell1 is oppSeed
				score = -10;
			} 
			else if (score == 1){ // cell1 is mySeed 
				return 0;
			} 
			else {  // cell1 is empty
				score = -1;
			}
		}


		// Third cell
		if (thisGame.getStateOfSquare(row3, col3) == AIPlayer){
			if (score > 0){  // cell1 and/or cell2 is mySeed
				score *= 10;
			} 
			else if (score < 0) { // cell1 and/or cell2 is oppSeed  
				return 0;
			} 
			else { // cell1 and cell2 are empty  
				score = 1;
			}
		} 
		else if (thisGame.getStateOfSquare(row3, col3) == opponent){
			if (score < 0){ // cell1 and/or cell2 is oppSeed  
				score *= 10;
			}
			else if (score > 1) { // cell1 and/or cell2 is mySeed  
				return 0;
			} 
			else { // cell1 and cell2 are empty  
				score = -1;
			}
		}
		return score;
	}
	
	
	/*
	 * if there is a winning move return that move otherwise return null
	 */
	//TODO - rename
	private int[] canWin(){
		// Generate possible next moves in a list of int[2] of {row, col}.
		List<int[]> nextMoves = thisGame.generateMoves();
		
		for (int[] element : nextMoves) {
			int i = element[0];
			int j = element[1];
			//if the space is empty, see if computer can win if it goes there
			if(thisGame.getStateOfSquare(i, j) == BoardSquaresState.EMPTY){
				
				//take a dummy turn then check if it wins..
				thisGame.setStateOfSquare(i, j, AIPlayer);
				
				//this move will win, the AI player should take it
				if(thisGame.getWinner(AIPlayer, i, j) == AIPlayer){

					Log.e(this.getClass().getName(), "Found a winning move at "+i+" ,"+j);
					thisGame.setStateOfSquare(i, j, BoardSquaresState.EMPTY);
					return new int[] {i, j};
				}
				
				//undo the dummy move
				thisGame.setStateOfSquare(i, j, BoardSquaresState.EMPTY);
			}
		}
		return null;
	}

}