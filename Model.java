/**
 * This file is to be completed by you.
 *
 * @author s2011447
 */

import java.util.Arrays;
import java.io.Serializable;

public final class Model implements Serializable {
	// ===========================================================================
	// ================================ CONSTANTS ================================
	// ===========================================================================

	public static final int DEFAULT_NR_ROWS = 6;
	public static final int DEFAULT_NR_COLS = 7;

	public static final int MAX_NR_ROWS = 100;
	public static final int MAX_NR_COLS = 100;

	public static final int DEFAULT_CONNECT = 4;
	public static final int NR_PLAYERS = 2;

	// Bullet mode: each player has 60 sec in total
	public static final double BULLET_TIME = 60;

	// ========================================================================
	// ================================ FIELDS ================================
	// ========================================================================

	private int nrRows;
	private int nrCols;
	private int connectX;

	private int player;
	private int numberMoves;

	private int difficulty;
	private int opponentType;
	private int gameMode;

	private final char[][] board = new char[MAX_NR_ROWS + 1][MAX_NR_COLS + 1];
	private final int[] heightOfCols = new int[MAX_NR_COLS + 1];

	// For bullet mode only: array with the time left of each player
	private final double[] bulletTime = new double[NR_PLAYERS + 1];

	// =============================================================================
	// ================================ CONSTRUCTOR ================================
	// =============================================================================
	public Model() {

		// Initialise the board size to its default values.
		nrRows = DEFAULT_NR_ROWS;
		nrCols = DEFAULT_NR_COLS;

		// The type of connect is the default one
		connectX = DEFAULT_CONNECT;

		// Player 1 always starts
		player = 0;

		numberMoves = 0;

		// The board is initially empty
		for (int i = 0; i < MAX_NR_ROWS; i++) {
			for (int j = 0; j < MAX_NR_COLS; j++) {
				board[i][j] = 0;
			}
		}

		bulletTime[0] = bulletTime[1] = BULLET_TIME;
	}
	
	// ====================================================================================
	// ================================ MODEL INTERACTIONS ================================
	// ====================================================================================

	// Check if the type of connect X is compatible with the dimensions of the board
	public boolean isSettingsCombinationValid(int connectType) {
		boolean validSettingsCombination = true;
		if(connectType > nrRows || connectType > nrCols || connectType < 0)
			validSettingsCombination = false;
		return validSettingsCombination;
	}

	// If the move is not in the interval [1, nrCols] or the column is already full, then the move is invalid
	public boolean isMoveValid(int move) {
		boolean validMove = true;
		if (move < 1 || move > nrCols)
			validMove = false;
		else if (heightOfCols[move - 1] == nrRows)
			validMove = false;
		return validMove;
	}

	// A piece of the current player is placed on the chosen column
	public void makeMove(int player, int move) {
		int col = move - 1;
		int lin = nrRows - heightOfCols[move - 1] - 1;

		// The corresponding pieces of each player
		char piece = (char) (player + '1');

		board[lin][col] = piece;
		numberMoves++;
		heightOfCols[move - 1]++;
	}

	// At the end of each game, the board and heights of the columns are reset
	public void wipeBoard() {
		// Player 1 always starts
		player = 0;

		// Number of total moves is reset
		numberMoves = 0;

		for (int i = 0; i < nrRows; i++) {
			for (int j = 0; j < nrCols; j++) {
				board[i][j] = 0;
			}
		}
		for (int j = 0; j < nrCols; j++)
			heightOfCols[j] = 0;

		// For bullet mode only: time is reset
		bulletTime[0] = bulletTime[1] = BULLET_TIME;
	}

	// The maximum number of pieces in a row is searched horizontally, vertically and diagonally
	// to decide if there is a winner
	public boolean anyWinner (int player, int move) {
		boolean isGameWon = false;
		if (maxPiecesInCol(player, move) >= connectX || maxPiecesInRow(player, move) >= connectX
				|| maxPiecesInDiagonal(player, move) >= connectX)
			isGameWon = true;
		return isGameWon;
	}

	// Maximum number of pieces on the column the last move was made on
	private int maxPiecesInCol(int player, int move) {
		int lin = nrRows - 1;
		int col = move - 1;

		int maxNrOfPiecesInCol = 1;

		char piece = (char) (player + '1');

		while(lin >= 0 && board[lin][col] != 0) {
			int nrOfPieces = 0;
			while (lin >=0 && board[lin][col] == piece) {
				nrOfPieces++;
				lin--;
			}
			if(nrOfPieces > maxNrOfPiecesInCol)
				maxNrOfPiecesInCol = nrOfPieces;
			lin--;
		}

		return maxNrOfPiecesInCol;
	}

	// Maximum number of pieces on the row the piece "fall" on
	private int maxPiecesInRow(int player, int move) {
		int lin = nrRows - heightOfCols[move - 1];
		int col = 0;

		int maxNrOfPiecesInRow = 1;
		char piece = (char) (player + '1');

		while (col < nrCols) {
			int nrOfPieces = 0;
			while (col < nrCols && board[lin][col] == piece) { // &&
				nrOfPieces++;
				col++;
			}
			if(nrOfPieces > maxNrOfPiecesInRow)
				maxNrOfPiecesInRow = nrOfPieces;
			col++;
		}
		return maxNrOfPiecesInRow;
	}

	// Maximum number of pieces on diagonal
	private int maxPiecesInDiagonal(int player, int move) {
		int lin = nrRows - heightOfCols[move - 1];
		int col = move - 1;

		int maxNrOfPiecesInDiag = 1;
		char piece = (char) (player + '1');

		// Calculate the first diagonal starting coordinates (top left) - check where we get first : on line 0 or column 0
		int firstDiagLine, firstDiagCol;
		if (lin > col) { // (lin, col) -> (0, 0)
			firstDiagLine = lin - col;
			firstDiagCol = 0;
		}
		else {
			firstDiagLine = 0;
			firstDiagCol = col - lin;
		}

		while (firstDiagLine < nrRows && firstDiagCol < nrCols) {
			int nrOfPieces = 0;

			// Top left to bottom right
			while (firstDiagLine < nrRows && firstDiagCol < nrCols && board[firstDiagLine][firstDiagCol] == piece) {
				nrOfPieces++;
				firstDiagLine++;
				firstDiagCol++;
			}
			if(nrOfPieces > maxNrOfPiecesInDiag)
				maxNrOfPiecesInDiag = nrOfPieces;
			firstDiagLine++;
			firstDiagCol++;
		}

		// Calculate the second diagonal starting coordinates (top right) - check where we get first : on line 0 or column nrCols - 1
		int secondDiagLine, secondDiagCol;
		if (nrCols - 1 - col > lin) { // (lin, col) -> (0, nrCols - 1)
			secondDiagLine = 0;
			secondDiagCol = col + lin;
		}
		else {
			secondDiagLine = lin - (nrCols - 1 - col);
			secondDiagCol = nrCols - 1;
		}

		while (secondDiagLine < nrRows && secondDiagCol >= 0) {
			int nrOfPieces = 0;

			// Top right to bottom left
			while (secondDiagLine < nrRows && secondDiagCol >= 0 && board[secondDiagLine][secondDiagCol] == piece) {
				nrOfPieces++;
				secondDiagLine++;
				secondDiagCol--;
			}
			if(nrOfPieces > maxNrOfPiecesInDiag)
				maxNrOfPiecesInDiag = nrOfPieces;
			secondDiagLine++;
			secondDiagCol--;
		}

		return maxNrOfPiecesInDiag;
	}

	// =========================================================================
	// ================================ SETTERS ================================
	// =========================================================================
	public void setBoardDimensions (int userRows, int userCols) {
		nrRows = userRows;
		nrCols = userCols;
	}

	public void setConnectX (int x) {
		connectX = x;
	}

	public void setDifficulty (int currentDifficulty) {
		difficulty = currentDifficulty;
	}

	public void setOpponentType (int currentOpponentType) { // Set the opponent to human or npc
		opponentType = currentOpponentType;
	}

	public void setGameMode (int currentGameMode) { // Set game mode to normal mode or bullet
		gameMode = currentGameMode;
	}

	public void setBulletTime (int player, double elapsedTime) {
		bulletTime[player] -= elapsedTime;
	}

	public void setPlayer (int currentPlayer) {
		player = currentPlayer;
	}
	
	// =========================================================================
	// ================================ GETTERS ================================
	// =========================================================================
	public int getNrRows() {
		return nrRows;
	}
	
	public int getNrCols() {
		return nrCols;
	}

	// A copy of the board is returned to protect it from unauthorized changes
	public char[][] getBoard() {
		return Arrays.copyOf(board, board.length);
	}

	public int getCurrentPlayer() {
		return player;
	}

	public int getNumberOfMoves() {
		return numberMoves;
	}

	public int getHeightOfColumn(int i) {
		return heightOfCols[i];
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getOpponentType() {
		return opponentType;
	}

	public int getGameMode() {
		return gameMode;
	}

	public double getBulletTime(int player) {
		return bulletTime[player];
	}
}
