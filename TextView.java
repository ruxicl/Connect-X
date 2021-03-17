public final class TextView {

	// The maximum number of tries for input validation
	public static final int MAX_TRIES = 10;

	public TextView() {
	
	}
	
	public final void displayNewGameMessage() {
		System.out.println("---- NEW GAME STARTED ----\n");
	}

	public final boolean askForOldGame() {
		System.out.println("Choose an option by pressing one of the keys and Enter.");
		System.out.println("1 Start a new game\n2 Resume your last game");
		int userResponse = InputUtil.readIntFromUser();
		int tries = 0;
		while (userResponse != 1 && userResponse != 2 && tries < MAX_TRIES) {
			System.out.println("Please choose 1 or 2.");
			userResponse = InputUtil.readIntFromUser();
			tries++;
		}
		if (userResponse != 1 && userResponse != 2) // If the user response is still invalid after the maximum number of tries
			throw new IllegalArgumentException("Invalid input too many times\n");
		return (userResponse == 2);
	}

	public final void displayUnsavedGameMessage() {
		System.out.println("Looks like you didn't save your last game\nA new game will be started.");
	}

	public final int askForOpponentType() {
		System.out.println("Choose one of the following options pressing the indicated key:");
		System.out.println("1 Human - Human");
		System.out.println("2 Human - Computer");
		int userResponse = InputUtil.readIntFromUser();
		int tries = 0;
		while (userResponse != 1 && userResponse != 2 && tries < MAX_TRIES) {
			System.out.println("Please choose 1 or 2");
			userResponse = InputUtil.readIntFromUser();
			tries++;
		}
		if (userResponse != 1 && userResponse != 2)
			throw new IllegalArgumentException("Invalid input too many times\n");
		return userResponse;
	}

	public final boolean askForDefaultDimensions() {
		System.out.println("If you want to play using the default board of 6 * 7, press y and Enter");
		System.out.println("Otherwise, press n and Enter");
		char userResponse = InputUtil.readCharFromUser();
		int tries = 0;
		while (userResponse != 'y' && userResponse != 'n' && tries < MAX_TRIES) {
			System.out.println("Please choose y or n.");
			userResponse = InputUtil.readCharFromUser();
			tries++;
		}
		if (userResponse != 'y' && userResponse != 'n')
			throw new IllegalArgumentException("Invalid input too many times\n");

		return (userResponse == 'y');
	}

	public final boolean askForDefaultConnectX() {
		System.out.println("If you want to play the classic Connect Four " +
				"which requires 4 pieces in a row to win, press y and Enter");
		System.out.println("Otherwise, press n and choose a type of Connect X");
		char userResponse = InputUtil.readCharFromUser();
		int tries = 0;
		while (userResponse != 'y' && userResponse != 'n' && tries < MAX_TRIES) {
			System.out.println("Please choose y or n.");
			userResponse = InputUtil.readCharFromUser();
			tries++;
		}
		if (userResponse != 'y' && userResponse != 'n')
			throw new IllegalArgumentException("Invalid input too many times\n");

		return (userResponse == 'y');
	}

	public final int askForRows() {
		System.out.println("Choose your own values for the number of rows & columns " +
				"- enter two positive integers smaller than 100");
		System.out.print("Number of rows: ");
		return InputUtil.readIntFromUser();
	}

	public final int askForCols() {
		System.out.print("Number of columns: ");
		return InputUtil.readIntFromUser();
	}

	public final int askForConnectX() {
		System.out.println("Connect X: choose how many pieces you need get in a row to win the game: ");
		return InputUtil.readIntFromUser();
	}
	
	public final int askForMove(int player) {
		System.out.printf("Player %d, it's your turn. Select a free column: ", player + 1);
		return InputUtil.readIntFromUser();
	}

	public final int askForDifficulty() {
		System.out.println("Select your difficulty:");
		System.out.println("1 Easy\n2 Medium\n3 Hard");
		int userResponse = InputUtil.readIntFromUser();
		int tries = 0;
		while (userResponse != 1 && userResponse != 2 && userResponse != 3 && tries < MAX_TRIES){
			System.out.println("Please choose 1, 2 or 3.");
			userResponse = InputUtil.readIntFromUser();
			tries++;
		}
		if (userResponse < 1 || userResponse > 3)
			throw new IllegalArgumentException("Invalid input too many times\n");
		return userResponse;
	}

	public final int askForGameMode() {
		System.out.println("Select your game mode:");
		System.out.println("1 Normal\n2 Bullet");
		int userResponse = InputUtil.readIntFromUser();
		int tries = 0;
		while (userResponse != 1 && userResponse != 2 && tries < MAX_TRIES){
			System.out.println("Please choose 1 or 2.");
			userResponse = InputUtil.readIntFromUser();
			tries++;
		}
		if (userResponse != 1 && userResponse != 2)
			throw new IllegalArgumentException("Invalid input too many times\n");
		return userResponse;
	}

	public final void displayBulletInstructions() {
		System.out.println("You are in Bullet Mode: each player has exactly 60 sec.\n" +
				"If you run out of time, your opponent wins. Be quick or be dead!");
	}

	public final void displayBulletTimeLeft(int player, double time) {
		System.out.printf("Player %d has %.2f second left\n\n", player+1, time);
	}

	public final void displayBulletOutOfTimeMessage() {
		System.out.println("You are out of time!");
	}

	public final char askToSaveGame() {
		System.out.println("You quit the game. Do you want to save your progress?");
		System.out.println("y or n");
		return InputUtil.readCharFromUser();
	}

	public final void displayComputerMove(int move) {
		System.out.printf("Player 1, it's your turn. Select a free column: %d\n", move);
	}

	public final void displayConcedeMessage() {
		System.out.println("If you want to concede, type -1 any time during the game");
	}

	public final char askForNewGame() {
		System.out.println("Game over! If you want to start a new game, press y and Enter\n" +
				"Press any other key and Enter to end the session.");
		return InputUtil.readCharFromUser();
	}

	public final void displayInvalidMoveMessage() {
		System.err.println("Invalid move! Please try again.");
	}

	public final void displayInvalidCombinationMessage() {
		System.err.println("Your chosen type of Connect X is incompatible with the board dimensions. Please try again.");
	}

	public final void displayWinnerMessage(int player) {
		System.out.printf("Congrats, Player %d! You won!\n", player + 1);
	}

	public final void displayDrawMessage() {
		System.out.println("It's a draw!");
	}

	public final void displayBoard(Model model) {
		int nrRows = model.getNrRows();
		int nrCols = model.getNrCols();
		
		// Used to print a line between each row.
		String rowDivider = "-".repeat(4 * nrCols + 1);

		System.out.print("\n");

		char[][] board = model.getBoard();

		for (int i = 0; i < nrRows; i++) {
			// Using StringBuilder: assemble longer Strings more efficiently
			StringBuilder str = new StringBuilder();

			for (int j = 0; j < nrCols; j++) {
				str.append("| ");
				if (board[i][j] == 0)
					str.append(' ');
				else
					str.append(board[i][j]);
				str.append(" ");
			}

			str.append("|" + "\n");
			System.out.print(str);
			System.out.print(rowDivider + "\n");
		}

		System.out.print("\n");
	}
}
