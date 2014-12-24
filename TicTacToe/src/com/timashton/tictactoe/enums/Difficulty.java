package com.timashton.tictactoe.enums;

public enum Difficulty {
	EASY(1),
	MEDIUM(2),
	HARD(9);

	Difficulty(int value){this.value = value;}

    private int value;

    public int getValue(){return value;}
}
