package com.timashton.tictactoe;

public class Constants 
{
	//from menu to game activity bundle data
	public static final String GAME_INTENT_BUNDLE = "gameIntentBundleTag";
	public static final String SELECTED_BOARD_SIZE = "selectedSize";
	public static final String HUMAN_PLAYER = "humanPlayer";
	public static final String COMPUTER_PLAYER = "computerPlayer";
	public static final String DIFFICULTY = "difficulty";
	public static final String NEW_GAME_REQUESTED = "newGameRequested";
	public static final String IS_GAME_OVER = "isGameOver";
	
	
	//GameActivity
	public static final String GAMEBOARD_SAVE_DATA = "gbSave";
	public static final String GAME_INFO_SAVE_DATA = "giSave";
	
	
	//Game
	public static final int BOARD_SIZE = 3;
	
	//AIPlayer
	public static final int SEARCH_DEPTH = 2;

}
