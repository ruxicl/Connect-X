import java.io.File;

public final class Controller {

	public static final int CONCEDE_GAME = -1;

	public static final double SEC_TO_NANOSEC = Math.pow(10, 9);

	private Model model;
	private final TextView view;

	private static final String filepath = "./output.txt";
	
	public Controller(Model model, TextView view) {
		this.model = model;
		this.view = view;
	}
	
	public void startSession() {

		do {

			view.displayNewGameMessage();

			boolean startNewGame = true;

			// Ask the user to choose to start a new game or to resume an old game
			if (view.askForOldGame()) {
				File file = new File(filepath);

				// Check if the file containing the old game is non-empty
				if (file.length() != 0) {
					SavedGame objectIO = new SavedGame();

					//Read object from file
					try {

						// Make sure the read model is non null
						if(objectIO.readObjectFromFile(filepath) == null)
							throw new NullPointerException();

						model = (Model) objectIO.readObjectFromFile(filepath);
						startNewGame = false;
					}
					catch (NullPointerException ex) {
						view.displayUnsavedGameMessage();
					}
				}
				// If there is no old game saved (the file is empty), inform the user
				else {
					view.displayUnsavedGameMessage();
				}
			}
			// If the user wants to start a new game or the loading from the file fails, start a new game
			if (startNewGame) {

				// Clear file containing old game
				SavedGame objectIO = new SavedGame();
				objectIO.writeObjectToFile(null);

				// Choose the settings of the new game
				int rows, cols;
				if (!view.askForDefaultDimensions()) {
					do {
						rows = view.askForRows();
					} while (rows <= 0 || rows >= Model.MAX_NR_COLS);

					do {
						cols = view.askForCols();
					} while (cols <= 0 || cols >= Model.MAX_NR_COLS);
				}
				else {
					rows = Model.DEFAULT_NR_ROWS;
					cols = Model.DEFAULT_NR_COLS;
				}

				model.setBoardDimensions(rows, cols);

				int connectX = (!view.askForDefaultConnectX() ? view.askForConnectX() : Model.DEFAULT_CONNECT);

				// Check if the chosen connect x  is compatible with the dimensions
				while (!model.isSettingsCombinationValid(connectX)) {
					view.displayInvalidCombinationMessage();
					connectX = view.askForConnectX();
				}

				model.setConnectX(connectX);

				// Ask the user to choose opponent type: human/ computer
				model.setOpponentType(view.askForOpponentType());
				if (model.getOpponentType() == 2) {
					// If the game settings are not the default ones, the npc is set on difficulty 1 (easy)
					if (rows != Model.DEFAULT_NR_ROWS || cols != Model.DEFAULT_NR_COLS || connectX != Model.DEFAULT_CONNECT) {
						model.setDifficulty(1);
					}
					else { // Otherwise the player can choose the difficulty
						model.setDifficulty(view.askForDifficulty());
					}
				}

				// Ask the player to choose game mode: bullet/normal
				model.setGameMode(view.askForGameMode());
				if (model.getGameMode() == 2) {
					view.displayBulletInstructions();
				}

			}

			// Create an instance of the ComputerPlayer
			ComputerPlayer npc = new ComputerPlayer(model);

			view.displayBoard(model);

			// Inform the player about the option of conceding the game
			view.displayConcedeMessage();

			boolean isGameContinued = true, isAnyWinner = false;
			int piecesLeft = model.getNrRows() * model.getNrCols() - model.getNumberOfMoves();

			// Check if there are pieces left, both player want to continue the game and there is no winner
			while (isGameContinued && piecesLeft > 0) {

				long startTime = System.nanoTime();
				int move;
				int player = model.getCurrentPlayer();

				// If the opponent is the NPC and if it's turn (NPC is always player 1, human player 2)
				if (model.getOpponentType() == 2 && player % 2 == 0) {
					move = npc.chooseMove(model.getDifficulty());
					view.displayComputerMove(move);
				}
				else {
					move = view.askForMove(player);
				}

				// As long as none of the players wants to concede the game, prompt the user to enter a valid move
				while (isGameContinued && !model.isMoveValid(move)) {
					if (move == CONCEDE_GAME) {
						isGameContinued = false;
					}
					else {
						view.displayInvalidMoveMessage();
						move = view.askForMove(player);
					}
				}

				// If the mode is bullet, calculate how much did the move took
				if (model.getGameMode() == 2) {
					double elapsedTime = (double) (System.nanoTime() - startTime) / SEC_TO_NANOSEC;
					model.setBulletTime(player, elapsedTime);
				}

				if (isGameContinued) {
					model.makeMove(player, move);
					view.displayBoard(model);

					// Winner detection by rules
					if (model.anyWinner(player, move)) {
						isGameContinued = false;
						view.displayWinnerMessage(player);
						isAnyWinner = true;
					}

					// In bullet mode, if one of the player is out of time
					if (model.getGameMode() == 2) {
						if (model.getBulletTime(player) < 0) {
							isGameContinued = false;
							view.displayBulletOutOfTimeMessage();
							view.displayWinnerMessage(player + 1);
							isAnyWinner = true;
						}
						else {
							view.displayBulletTimeLeft(player, model.getBulletTime(player));
						}
					}

					// Switching the player
					model.setPlayer((player + 1) % 2);
					piecesLeft--;
				}

			}

			// If one of the players concedes the game
			if (!isGameContinued && !isAnyWinner) {

				// Ask the player to save the progress
				if (view.askToSaveGame() == 'y') {
					SavedGame objectIO = new SavedGame();
					objectIO.writeObjectToFile(model);
				}
				// If the player does not want to, the file is cleared
				else {
					SavedGame objectIO = new SavedGame();
					objectIO.writeObjectToFile(null);
				}
			}

			// Check if there is a draw
			if (piecesLeft == 0 && !isAnyWinner)
				view.displayDrawMessage();

			model.wipeBoard();

		} while (view.askForNewGame() == 'y');

	}

}
