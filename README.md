Android-TicTacToe
=================

As the name suggests, this is a Tic Tac Toe game for Android. The project was built against Android 4.4.2 SDK and tested (And designed for) my Samsung Galaxy S3. The S3 was running Android 4.3 Jelly Bean. The application interface is only designed for the 4.7 inch screen but could be easily extended with the addition of some extra xml layout files.

There is a splash screen with simple animation, a menu for game options which displays game configuration in compound relative layout buttons. A user can play an easy, medium or hard game as O or X against an A.I opponent.

The AI uses a minimax algorithm with alpha-beta pruning and hueristics to generate moves for a medium or hard game. The easy opponent will take a random turn. If a winning move is available the AI player will always take it.

The screen can be rotated only in the GameActivity, The screen is locked on portrait for the other two activities. Saving and restoring state for a screen rotation is done by storing game state to a private file.

This tic tac toe game is a complete implementation which can easily be run on device or emulator.

