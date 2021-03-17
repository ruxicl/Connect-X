public class ComputerPlayer {

    private final Model model;

    public ComputerPlayer(Model model) {
        this.model = model;
    }

    // Select a random valid move
    private int randomMove() {
        int nrCols =  model.getNrCols();
        int move;
        do {
            move = (int) (1 + Math.random() * nrCols);
        } while (!model.isMoveValid(move));
        return move;
    }

    // Checks if there is a winning move for a player
    private boolean isThisMoveWinning(int move, int player) {
        boolean isWinning = false;
        char[][] board = model.getBoard();

        int col = move - 1;
        int height = model.getHeightOfColumn(col);
        int row = model.getNrRows() - height - 1;

        // player 0 uses pieces '1', player 1 uses pieces '2'
        char piece = (char) (player + '1');

        // Vertically
        if (height >= 3) {
            if (row < model.getNrRows() - 3 && board[row + 1][col] == piece && board[row + 2][col] == piece && board[row + 3][col] == piece)
                isWinning = true;
        }

        // Horizontally
        if (!isWinning) {
            // We are looking for (3 pieces + 1 empty cell) or (1 empty cell + 3 pieces)
            if (col < model.getNrCols() - 3 && board[row][col + 1] == piece && board[row][col + 2] == piece && board[row][col + 3] == piece)
                isWinning = true;
            if (col >= 3 && board[row][col - 1] == piece && board[row][col - 2] == piece && board[row][col - 3] == piece)
                isWinning = true;

            // We look for (2 pieces + 1 empty cell + 1 piece) or (1 piece + 1 empty cell + 2 pieces)
            // we only need to look at 5 cells (with the 3rd cell being the selected move - an empty cell)
            int nrPieces = 0;
            for (int j = -2; j <= 2; j++) {
                int newCol = col + j;
                if (newCol >= 0 && newCol < model.getNrCols() && board[row][newCol] == piece)
                    nrPieces++;
            }

            // we need to remove the combinations which contain cells (2, 3) or (3, 4) that are empty/have the wrong pieces
            if (nrPieces == 3 && (board[row][col - 1] == piece || board[row][col + 1] == piece))
                isWinning = true;
        }

        // Diagonally - same strategy as for horizontally
        if (!isWinning) {

            // First diagonal
            int nrPieces = 0;
            for (int i = -2; i <= 2; i++) {
                int j = i;
                int newRow = row + i;
                int newCol = col + j;
                if (newCol >= 0 && newCol < model.getNrCols() && newRow >= 0 && newRow < model.getNrRows()
                        && board[row][newCol] == piece)
                    nrPieces++;
            }
            // Obs: if the first condition is satisfied, then there is no possibility of index-out-of-bound error
            if (nrPieces == 3 && (board[row - 1][col - 1] == piece || board[row + 1][col + 1] == piece))
                isWinning = true;

            // Second diagonal
            nrPieces = 0;
            for (int i = -2; i <= 2; i++) {
                int j = -i;
                int newRow = row + i;
                int newCol = col + j;
                if (newCol >= 0 && newCol < model.getNrCols() && newRow >= 0 && newRow < model.getNrRows()
                        && board[row][newCol] == piece)
                    nrPieces++;
            }

            if (nrPieces == 3 && (board[row - 1][col - 1] == piece || board[row + 1][col + 1] == piece))
                isWinning = true;
        }
        return isWinning;
    }

    // The medium difficulty NPC either chooses a move that will win that turn or blocks the opponent for winning that turn
    // If there is no such move, NPC chooses a random one
    private int mediumMove() {

        int chosenMove = 0;

        // If it's the first move, choose the middle column
        if (model.getNumberOfMoves() == 0)
            chosenMove = Model.DEFAULT_NR_COLS / 2 + 1;

        // If there is a winning move, choose it
        int player = model.getCurrentPlayer();
        int move = 1;
        while (move <= model.getNrRows() && chosenMove == 0) {
            if (model.isMoveValid(move)) {
                if (isThisMoveWinning(move, player)) {
                    chosenMove = move;
                }
            }
            move++;
        }

        // If there is no winning move, check if there is any move that prevents the next move of the opponent to be a winning move
        if(chosenMove == 0) {
            move = 1;
            int opponent = (model.getCurrentPlayer() + 1) % 2;
            while (move <= model.getNrRows() && chosenMove == 0) {
                if (model.isMoveValid(move)) {
                    if (isThisMoveWinning(move, opponent)) {
                        chosenMove = move;
                    }
                }
                move++;
            }
        }

        // If there is no winning/blocking move, select a random valid move
        if (chosenMove == 0)
            chosenMove = randomMove();

        return chosenMove;
    }

    // NPC chooses its current move based on the selected difficulty
    public int chooseMove (int difficulty) {
        int move;
        switch (difficulty) {
            case 1: move = randomMove();
                    break;
            case 2: move = mediumMove();
                    break;
            default: move = randomMove(); // difficulty 3 (hard) is not implemented yet
                    break;
        }
        return move;
    }

}
