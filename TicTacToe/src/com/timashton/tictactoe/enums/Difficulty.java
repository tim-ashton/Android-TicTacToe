package com.timashton.tictactoe.enums;

public enum Difficulty {
	EASY(0),
	MEDIUM(1),
	HARD(2);

	Difficulty(int value){this.value = value;}
    private int value;
    public int getValue(){return value;}
}
