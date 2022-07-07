package org.cis120.Chess;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class is a model for TicTacToe.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 * 
 * Run this file to see the main method play a game of TicTacToe,
 * visualized with Strings printed to the console.
 */
public class Chess {

    private Square[][] board;
    private int numMoves;
    private boolean white = true; // keeps track of whose move it is (white or black)
    private boolean gameOver;
    private boolean hasResigned = false;
    private boolean isStalemate;

    // variables for handling Check and Checkmate (for each color)
    private boolean whiteInCheck = false;
    private boolean blackInCheck = false;

    // initialize King objects so we can easily check if we're in check
    private King whiteKing = new King(
            true, 0, 4,
            "files/White_King.png"
    );
    private King blackKing = new King(
            false, 7, 4,
            "files/Black_King.png"
    );

    private List<Piece> currentWhitePieces; // keep track of all Pieces throughout game
    private List<Piece> currentBlackPieces;

    // get position of Piece attacking our King, start at -1 (means no Piece
    // attacking King)
    private int attackRow = -1;
    private int attackCol = -1;

    /**
     * Constructor sets up game state.
     */
    public Chess() {
        currentWhitePieces = new LinkedList<>();
        currentBlackPieces = new LinkedList<>();
        reset();
    }

    // determine if white King in check (if any black Piece can reach white King's
    // position)
    public boolean isInCheckWhiteKing(King whiteKing, Chess chess) {
        List<Piece> currentBlackPieces = chess.getCurrentBlackPieces();
        Square[][] currentBoard = this.getBoard();

        for (Piece curr : currentBlackPieces) {
            if (curr.isLegalMove(
                    currentBoard[whiteKing.getCurrRow()][whiteKing.getCurrCol()], currentBoard
            )) {
                whiteInCheck = true;
                attackRow = curr.getCurrRow();
                attackCol = curr.getCurrCol();
                // if white in check, remove all the current legal moves for all white Pieces
                // since we will need to recheck whether they can be used to get out of check
                for (Piece our : currentWhitePieces) {
                    our.getLegalMoves().clear();
                }
                break; // only need one piece to be able to attack King to be in check
            }
        }
        return whiteInCheck;
    }

    // determine if black King in check (if a white Piece can reach black King's
    // position)
    public boolean isInCheckBlackKing(King blackKing, Chess chess) {
        List<Piece> currentWhitePieces = chess.getCurrentWhitePieces();
        Square[][] currentBoard = chess.getBoard();

        for (Piece curr : currentWhitePieces) {
            if (curr.isLegalMove(
                    currentBoard[blackKing.getCurrRow()][blackKing.getCurrCol()], currentBoard
            )) {
                blackInCheck = true;
                attackRow = curr.getCurrRow();
                attackCol = curr.getCurrCol();

                // remove all current legal moves for the black Pieces
                for (Piece our : currentBlackPieces) {
                    our.getLegalMoves().clear();
                }
                break; // only need one piece to be able to attack King to be in check
            }
        }
        return blackInCheck;
    }

    // helps check whether we can block check along a diagonal
    public boolean blockDiagonalHelper(Chess chess, King king, Square attacker) {
        // get coordinates of King and attacking piece
        int kRow = king.getCurrRow();
        int kCol = king.getCurrCol();
        int aRow = attacker.getRow();
        int aCol = attacker.getColumn();
        Square[][] board = chess.getBoard();

        int check = 0;

        // determine whether to get list of white or black pieces
        List<Piece> ourPieces = new LinkedList<>();
        if (king.getColor()) {
            ourPieces = chess.getCurrentWhitePieces();
        } else {
            ourPieces = chess.getCurrentBlackPieces();
        }

        // if King is to top right of Bishop or Queen
        if (kRow > aRow && kCol > aCol) {
            int rowDifference = kRow - aRow; // colDifference should be the same
            for (Piece curr : ourPieces) {
                for (int i = 1; i < rowDifference; i++) {
                    if (curr.isLegalMove(board[aRow + i][aCol + i], board) &&
                            !(curr instanceof King)) {
                        Square current = board[aRow + i][aCol + i];
                        curr.addLegalMoveForCheck(current); // add to Piece's legal moves list
                        check = 1;
                    }
                }
            }
        } else if (kRow > aRow && kCol < aCol) { // King to top left of Bishop
            int rowDifference = kRow - aRow;
            for (Piece curr : ourPieces) {
                for (int i = 1; i < rowDifference; i++) {
                    if (curr.isLegalMove(board[aRow + i][aCol - i], board)
                            && !(curr instanceof King)) {
                        Square current = board[aRow + i][aCol - i];
                        curr.addLegalMoveForCheck(current); // add to Piece's legal moves list
                        check = 1;
                    }
                }
            }
        } else if (kRow < aRow && kCol > aCol) { // King to bottom right of Bishop
            int rowDifference = Math.abs(kRow - aRow);
            for (Piece curr : ourPieces) {
                for (int i = 1; i < rowDifference; i++) {
                    if (curr.isLegalMove(board[aRow - i][aCol + i], board)
                            && !(curr instanceof King)) {
                        Square current = board[aRow - i][aCol + i];
                        curr.addLegalMoveForCheck(current);
                        check = 1;
                    }
                }
            }
        } else if (kRow < aRow && kCol < aCol) { // King to bottom left of Bishop
            int rowDifference = Math.abs(kRow - aRow);
            for (Piece curr : ourPieces) {
                for (int i = 1; i < rowDifference; i++) {
                    if (curr.isLegalMove(board[kRow + i][kCol + i], board)
                            && !(curr instanceof King)) {
                        Square current = board[kRow + i][kCol + i];
                        curr.addLegalMoveForCheck(current);
                        check = 1;
                    }
                }
            }
        }
        return (check == 1);
    }

    // to block check when attacking Piece and King are on the same row
    public boolean blockRowHelper(Chess chess, King king, Square attacker) {
        List<Piece> ourPieces = new LinkedList<>();
        Square[][] board = chess.getBoard();
        // column coordinates of King and Rook
        int kCol = king.getCurrCol();
        int aCol = attacker.getColumn();

        int check = 0;

        // determine whether to get white or black Pieces
        if (king.getColor()) {
            ourPieces = chess.getCurrentWhitePieces();
        } else {
            ourPieces = chess.getCurrentBlackPieces();
        }

        // two different possibilities based on relative position of King with Rook
        if (kCol > aCol) {
            for (Piece curr : ourPieces) {
                for (int i = aCol + 1; i < kCol; i++) {
                    if (curr.isLegalMove(board[king.getCurrRow()][i], board)
                            && !(curr instanceof King)) {
                        Square current = board[king.getCurrRow()][i];
                        curr.addLegalMoveForCheck(current);
                        check = 1;
                    }
                }
            }
        } else {
            for (Piece curr : ourPieces) {
                for (int i = kCol + 1; i < aCol; i++) {
                    if (curr.isLegalMove(board[king.getCurrRow()][i], board)
                            && !(curr instanceof King)) {
                        Square current = board[king.getCurrRow()][i];
                        curr.addLegalMoveForCheck(current);
                        check = 1;
                    }
                }
            }
        }
        return (check == 1);
    }

    // same logic as with blockRowHelper except we're blocking along a file
    public boolean blockColHelper(Chess chess, King king, Square attacker) {
        List<Piece> ourPieces = new LinkedList<>();
        Square[][] board = chess.getBoard();

        // column coordinates of King and Rook
        int kRow = king.getCurrRow();
        int aRow = attacker.getRow();

        int check = 0;

        // determine whether to get white or black Pieces
        if (king.getColor()) {
            ourPieces = chess.getCurrentWhitePieces();
        } else {
            ourPieces = chess.getCurrentBlackPieces();
        }

        // two different possibilities based on relative position of King with Rook
        if (kRow > aRow) {
            for (Piece curr : ourPieces) {
                for (int i = aRow + 1; i < kRow; i++) {
                    if (curr.isLegalMove(board[i][king.getCurrCol()], board)
                            && !(curr instanceof King)) {
                        Square current = board[i][king.getCurrCol()];
                        curr.addLegalMoveForCheck(current);
                        check = 1;
                    }
                }
            }
        } else {
            for (Piece curr : ourPieces) {
                for (int i = kRow + 1; i < aRow; i++) {
                    if (curr.isLegalMove(board[i][king.getCurrCol()], board)
                            && !(curr instanceof King)) {
                        Square current = board[i][king.getCurrCol()];
                        curr.addLegalMoveForCheck(current);
                        check = 1;
                    }
                }
            }
        }
        return (check == 1);
    }

    public boolean blockCheck(Chess chess, King king, Square attacker) {
        Piece attacking = attacker.getCurrentPiece();
        Square[][] board = chess.getBoard();

        int rowDifference = Math.abs(king.getCurrRow() - attacker.getRow());
        int colDifference = Math.abs(king.getCurrCol() - attacker.getColumn());

        int check = 0;

        // attacking Piece would be right next to the King
        if (rowDifference <= 1 && colDifference <= 1) {
            return false;
        }

        // determine appropriate checks based on the type of the attacking Piece
        if (attacking instanceof Knight) { // can't block a Knight
            return false;
        } else if (attacking instanceof Pawn) { // Pawn will be right next to King
            return false;
        } else if (attacking instanceof Rook) { // Rook attacks in straight lines
            return (blockColHelper(chess, king, attacker) || blockRowHelper(chess, king, attacker));
        } else if (attacking instanceof Bishop) { // Bishop attacks along diagonal
            return blockDiagonalHelper(chess, king, attacker);
        } else if (attacking instanceof Queen) { // Queen attacks in straight lines or along
                                                 // diagonal
            return (blockDiagonalHelper(chess, king, attacker)
                    && blockColHelper(chess, king, attacker)
                    && blockRowHelper(chess, king, attacker));
        }
        return false; // failed all the other checks
    }

    // we'll get Square of attacker, where we iterate through every piece and
    // determine if opposing King's position is
    // a legal move to determine if in check
    public boolean captureCheckingPiece(Chess chess, King king, Square attacker) {
        Square[][] board = chess.getBoard();
        int check = 0;

        // add Pieces to list based on color of King--goal to determine which of our
        // Pieces can capture the attacker
        if (king.getColor()) {
            for (Piece curr : currentWhitePieces) { // see if we can capture attacker
                if (curr.isLegalMove(attacker, board)) {
                    if (curr instanceof King) {
                        for (Piece enemy : currentBlackPieces) {
                            if (enemy.isLegalMove(attacker, board)) {
                                return false;
                            }
                        }
                    } else {
                        curr.addLegalMoveForCheck(attacker);
                        check = 1;
                    }
                }
            }
        } else {
            for (Piece curr : currentBlackPieces) {
                if (curr.isLegalMove(attacker, board)) {
                    if (curr instanceof King) {
                        for (Piece enemy : currentWhitePieces) {
                            if (enemy.isLegalMove(attacker, board)) {
                                return false;
                            }
                        }
                    } else {
                        curr.addLegalMoveForCheck(attacker);
                        check = 1;
                    }
                }
            }
        }
        return (check == 1);
    }

    // determine if King can move out of check
    public boolean avoidCheck(King king) {
        int row = king.getCurrRow();
        int col = king.getCurrCol();
        int startRow = row - 1;
        int startCol = col - 1;
        int endRow = row + 1;
        int endCol = col + 1;

        int check = 0;

        // do these checks so we don't get ArrayIndexOutOfBounds
        if (startRow < 0) {
            startRow = 0;
        }
        if (startCol < 0) {
            startCol = 0;
        }
        if (endRow >= 8) {
            endRow = 7;
        }
        if (endCol >= 8) {
            endCol = 7;
        }

        // check all Squares that King could potentially move to
        for (int i = startRow; i < endRow; i++) {
            for (int j = startCol; j < endCol; j++) {
                Square potential = board[i][j];

                // check White king and black King separately
                if (king.getColor()) {
                    if (king.isLegalMove(potential, this.board)
                            && !canBlackReachSquare(potential)) {
                        king.addLegalMoveForCheck(potential);
                        check = 1;
                    }
                } else {
                    if (king.isLegalMove(potential, this.board)
                            && !canWhiteReachSquare(potential)) {
                        king.addLegalMoveForCheck(potential);
                        check = 1;
                    }
                }
            }
        }

        return check == 1; // if King has no legal moves, then can't move out of check
    }

    // determine if any black Pieces can reach a certain square (useful for
    // determining if King is moving into check)
    public boolean canBlackReachSquare(Square whiteKing) {
        for (Piece curr : currentBlackPieces) {
            if (curr.isLegalMove(whiteKing, this.getBoard())) {
                return true;
            }
        }
        return false;
    }

    // important for determining if the King is moving into check
    public boolean canWhiteReachSquare(Square blackKing) {
        for (Piece curr : currentWhitePieces) {
            if (curr.isLegalMove(blackKing, this.getBoard())) {
                return true;
            }
        }
        return false;
    }

    // if can't block, capture, or move, then White is checkmated
    public boolean isWhiteCheckmated(King whiteKing, Chess chess, Square attacker) {
        if (isInCheckWhiteKing(whiteKing, chess)) {
            boolean block = blockCheck(chess, whiteKing, attacker);
            boolean capture = captureCheckingPiece(chess, whiteKing, attacker);
            boolean avoid = avoidCheck(whiteKing);
            if (!block && !capture && !avoid) {
                gameOver = true;
            }
        } else {
            gameOver = false;
        }
        return gameOver;
    }

    // if can't block, capture, or move, then Black is checkmated
    public boolean isBlackCheckmated(King blackKing, Chess chess, Square attacker) {
        if (isInCheckBlackKing(blackKing, chess)) {
            boolean block = blockCheck(chess, blackKing, attacker);
            boolean capture = captureCheckingPiece(chess, blackKing, attacker);
            boolean avoid = avoidCheck(blackKing);
            if (!block && !capture && !avoid) {
                gameOver = true;
            }
        } else {
            gameOver = false;
        }
        return gameOver;
    }

    // see if there are legal moves for white (important for determining stalemate)
    public boolean noMovesWhite() {
        boolean no = true;
        for (Piece curr : currentWhitePieces) {
            if (curr.getLegalMoves().size() != 0) {
                no = false;
            }
        }
        return true;
    }

    // see if there are legal moves for black (important for determining stalemate)
    public boolean noMovesBlack() {
        boolean no = true;
        for (Piece curr : currentBlackPieces) {
            if (curr.getLegalMoves().size() != 0) {
                no = false;
            }
        }
        return true;
    }

    // determines if the game is stalemated
    public boolean stalemate() {
        if (white) {
            if (!isInCheckWhiteKing(whiteKing, this) && noMovesWhite()) {
                isStalemate = true;
            }
        } else {
            if (!isInCheckBlackKing(blackKing, this) && noMovesBlack()) {
                isStalemate = true;
            }
        }
        return isStalemate;
    }

    // to see if game is stalemated
    public boolean isStalemated() {
        return this.isStalemate;
    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * //@param c column to play in
     * //@param r row to play in
     * 
     * @return whether the turn was successful
     */

    // TODO determine right set of rules (type of piece) to invoke when we click on
    // a Piece
    public boolean makeMove(Square selected, Square to, Square[][] board) {

        // can't move if game is over or no Piece was selected
        if (!selected.containsPiece() || gameOver) {
            return false;
        }

        // start by filling up the Piece's legalMoves list
        selected.getCurrentPiece().addLegalMoves(board);

        // can't move when it's not your turn
        if (selected.getCurrentPiece().getColor() != white) {
            return false;
        }

        // check if attempted move is a castle

        // castling for white
        if (white && selected.getCurrentPiece() instanceof King) {
            King thisOne = (King) selected.getCurrentPiece();
            if (!thisOne.getHasMoved() && to.getRow() == 0 && to.getColumn() == 6
                    && !board[0][5].containsPiece() && !board[0][6].containsPiece()
                    && !board[0][7].getCurrentPiece().getHasMoved()
                    && !canBlackReachSquare(board[0][6])) { // kingside castle
                selected.getCurrentPiece().move(board[0][4], board[0][6], board);
                board[0][7].getCurrentPiece().move(board[0][7], board[0][5], board);
                white = !white;
                return true;
            } else if (!thisOne.getHasMoved() && to.getRow() == 0 && to.getColumn() == 2
                    && !board[0][3].containsPiece() && !board[0][2].containsPiece()
                    && !board[0][1].containsPiece() && !board[0][0].getCurrentPiece().getHasMoved()
                    && !canBlackReachSquare(board[0][2])) { // queenside castle
                selected.getCurrentPiece().move(board[0][4], board[0][2], board);
                board[0][0].getCurrentPiece().move(board[0][0], board[0][3], board);
                white = !white;
                return true;
            }
        } else if (!white && selected.getCurrentPiece() instanceof King) { // castling for black
            King thisOne = (King) selected.getCurrentPiece();
            if (!thisOne.getHasMoved() && to.getRow() == 7 && to.getColumn() == 6
                    && !board[7][5].containsPiece() && !board[7][6].containsPiece()
                    && !board[7][7].getCurrentPiece().getHasMoved()
                    && !canWhiteReachSquare(board[7][6])) { // kingside castle
                selected.getCurrentPiece().move(board[7][4], board[7][6], board);
                board[7][7].getCurrentPiece().move(board[7][7], board[7][5], board);
                white = !white;
                return true;
            } else if (!thisOne.getHasMoved() && to.getRow() == 7 && to.getColumn() == 2
                    && !board[7][3].containsPiece()
                    && !board[7][2].containsPiece() && !board[7][1].containsPiece()
                    && !board[7][0].getCurrentPiece().getHasMoved()
                    && !canWhiteReachSquare(board[7][2])) { // queenside castle
                selected.getCurrentPiece().move(board[7][4], board[7][2], board);
                board[7][0].getCurrentPiece().move(board[7][0], board[7][3], board);
                white = !white;
                return true;
            }
        }

        // En Passant

        // en passant for white and black Pawns
        if (white && selected.getCurrentPiece() instanceof Pawn &&
                selected.getCurrentPiece().getCurrRow() == 4) {
            // going to the right
            if (to.getRow() == 5 && to.getColumn() == selected.getCurrentPiece().getCurrCol() + 1
                    && board[4][to.getColumn()].getCurrentPiece() instanceof Pawn
                    && board[4][to.getColumn()].getCurrentPiece().getColor() != selected
                            .getCurrentPiece().getColor()) {
                selected.getCurrentPiece().move(selected, to, board);
                board[4][to.getColumn()].changePiece(null);
                return true;
            } else if (to.getRow() == 5
                    && to.getColumn() == selected.getCurrentPiece().getCurrCol() - 1
                    && board[4][to.getColumn()].getCurrentPiece() instanceof Pawn
                    && board[4][to.getColumn()].getCurrentPiece().getColor() != selected
                            .getCurrentPiece().getColor()) { // going left
                selected.getCurrentPiece().move(selected, to, board);
                board[4][to.getColumn()].changePiece(null);
                return true;
            }
        } else if (!white && selected.getCurrentPiece() instanceof Pawn
                && selected.getCurrentPiece().getCurrRow() == 3) {
            if (to.getRow() == 2 && to.getColumn() == selected.getCurrentPiece().getCurrCol() + 1
                    && board[3][to.getColumn()].getCurrentPiece() instanceof Pawn
                    && board[3][to.getColumn()].getCurrentPiece().getColor() != selected
                            .getCurrentPiece().getColor()) {
                selected.getCurrentPiece().move(selected, to, board);
                board[3][to.getColumn()].changePiece(null);
                return true;
            } else if (to.getRow() == 2
                    && to.getColumn() == selected.getCurrentPiece().getCurrCol() - 1 &&
                    board[3][to.getColumn()].getCurrentPiece() instanceof Pawn
                    && board[3][to.getColumn()].getCurrentPiece().getColor() != selected
                            .getCurrentPiece().getColor()) {
                selected.getCurrentPiece().move(selected, to, board);
                board[3][to.getColumn()].changePiece(null);
                return true;
            }
        }

        // trying to make illegal move
        if (!selected.getCurrentPiece().getLegalMoves().contains(to)) {
            return false;
        }

        // white = !white; // switch players after clearing all legal moves

        // check for checkmate (for both white and black, depending on whose move it is)
        // check this after the move has been made
        if (white) {
            if (this.isInCheckWhiteKing(this.whiteKing, this)) {
                if (this.isWhiteCheckmated(whiteKing, this, board[attackRow][attackCol])) {
                    gameOver = true;
                }
            }
        } else {
            if (this.isInCheckBlackKing(this.blackKing, this)) {
                if (this.isBlackCheckmated(blackKing, this, board[attackRow][attackCol])) {
                    gameOver = true;
                }
            }
        }

        // no legal moves left if the game is stalemated
        if (this.isStalemate) {
            return false;
        }

        // redetermine whether move is legal after determining whether we're in check
        if (selected.getCurrentPiece().getLegalMoves().contains(to)) {
            if (to.containsPiece()) {
                if (to.getCurrentPiece().getColor()) {
                    currentWhitePieces.remove(to.getCurrentPiece()); // remove Piece from list if
                                                                     // captured
                } else {
                    currentBlackPieces.remove(to.getCurrentPiece());
                }
            }
            selected.getCurrentPiece().move(selected, to, board);
            // if we just made legal move, we can't possibly still be in check
            if (whiteInCheck) {
                whiteInCheck = false;
            } else if (blackInCheck) {
                blackInCheck = false;
            }
        }

        // make sure that the move we just made does not put our King into check
        if (white) {
            if (isInCheckWhiteKing(whiteKing, this)) {
                return false;
            }
        } else {
            if (isInCheckBlackKing(blackKing, this)) {
                return false;
            }
        }

        // switch players
        white = !white;
        numMoves += 1;
        return true; // if we pass all the other checks
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + numMoves + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 2) {
                    System.out.print(" | ");
                }
            }
            if (i < 2) {
                System.out.println("\n---------");
            }
        }
    }

    /**
     * reset (re-)sets the game state to start a new game, and puts pieces at
     * starting positions
     */
    public void reset() {
        board = new Square[8][8];
        numMoves = 0;
        white = true;
        gameOver = false;

        // populate the board with Squares
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 == 0 && col % 2 == 0) {
                    board[row][col] = new Square(row, col, null, true);
                } else if (row % 2 == 0 && col % 2 == 1) {
                    board[row][col] = new Square(row, col, null, false);
                } else if (row % 2 == 1 && col % 2 == 1) {
                    board[row][col] = new Square(row, col, null, true);
                } else if (row % 2 == 1 && col % 2 == 0) {
                    board[row][col] = new Square(row, col, null, false);
                }
            }
        }

        // set up all the black Pieces on the board
        board[7][0].changePiece(new Rook(false, 7, 0,
                "files/Black_Rook.png"));
        board[7][7].changePiece(new Rook(false, 7, 7,
                "files/Black_Rook.png"));
        board[7][1].changePiece(new Knight(false, 7, 1,
                "files/Black_Knight.png"));
        board[7][6].changePiece(new Knight(false, 7, 6,
                "files/Black_Knight.png"));
        board[7][2].changePiece(new Bishop(false, 7, 2,
                "files/Black_Bishop.png"));
        board[7][5].changePiece(new Bishop(false, 7, 5,
                "files/Black_Bishop.png"));
        board[7][3].changePiece(new Queen(false, 7, 3,
                "files/Black_Queen.png"));
        board[7][4].changePiece(blackKing);
        for (int col = 0; col < 8; col++) {
            board[6][col].changePiece(new Pawn(false, 6, col,
                    "files/Black_Pawn.png"));
        }

        // set up all the white Pieces on the board
        board[0][0].changePiece(new Rook(true, 0, 0,
                "files/White_Rook.png"));
        board[0][7].changePiece(new Rook(true, 0, 7,
                "files/White_Rook.png"));
        board[0][1].changePiece(new Knight(true, 0, 1,
                "files/White_Knight.png"));
        board[0][6].changePiece(new Knight(true, 0, 6,
                "files/White_Knight.png"));
        board[0][2].changePiece(new Bishop(true, 0, 2,
                "files/White_Bishop.png"));
        board[0][5].changePiece(new Bishop(true, 0, 5,
                "files/White_Bishop.png"));
        board[0][3].changePiece(new Queen(true, 0, 3,
                "files/White_Queen.png"));
        board[0][4].changePiece(whiteKing);
        for (int col = 0; col < 8; col++) {
            board[1][col].changePiece(new Pawn(true, 1, col,
                    "files/White_Pawn.png"));
        }

        // add all Pieces on the board to the respective lists
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].containsPiece()) {
                    Piece curr = board[i][j].getCurrentPiece();
                    if (curr.getColor()) {
                        currentWhitePieces.add(curr);
                    } else {
                        currentBlackPieces.add(curr);
                    }
                }
            }
        }
    }

    // return copy of list of Pieces to maintain encapsulation
    public List<Piece> getCurrentWhitePieces() {
        List<Piece> cwp = this.currentWhitePieces;
        return cwp;
    }

    public List<Piece> getCurrentBlackPieces() {
        List<Piece> cbp = this.currentBlackPieces;
        return cbp;
    }

    // to access state of board in other classes--make copy to encapsulate
    public Square[][] getBoard() {
        Square[][] b = this.board;
        return b;
    }

    public boolean resign() {
        this.gameOver = true;
        return white;
    }

    // variety of getter methods that will be important for playing the game and for
    // testing

    public boolean getCurrentPlayer() {
        return white; // returns true if White to move
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public King getBlackKing() {
        return this.blackKing;
    }

    public King getWhiteKing() {
        return this.whiteKing;
    }

    public int getAttackRow() {
        return this.attackRow;
    }

    public int getAttackCol() {
        return this.attackCol;
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {

        // enjoy the quick and elegant Legal's Mate

        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(chess1.makeMove(board1[1][4], board1[3][4], board1));
        assertTrue(chess1.makeMove(board1[6][4], board1[4][4], board1));
        assertTrue(chess1.makeMove(board1[0][5], board1[3][2], board1));
        assertTrue(chess1.makeMove(board1[6][3], board1[5][3], board1));
        assertTrue(chess1.makeMove(board1[0][6], board1[2][5], board1));
        assertTrue(chess1.makeMove(board1[7][2], board1[3][6], board1));
        assertTrue(chess1.makeMove(board1[0][1], board1[2][2], board1));
        assertTrue(chess1.makeMove(board1[6][6], board1[5][6], board1));
        assertTrue(chess1.makeMove(board1[2][5], board1[4][4], board1));
        assertTrue(chess1.makeMove(board1[3][6], board1[0][3], board1));
        assertTrue(chess1.makeMove(board1[3][2], board1[6][5], board1));
        assertTrue(chess1.makeMove(board1[7][4], board1[6][4], board1));
        assertTrue(chess1.makeMove(board1[2][2], board1[4][3], board1));
        assertTrue(chess1.isInCheckBlackKing(chess1.getBlackKing(), chess1));
        assertFalse(
                chess1.blockCheck(
                        chess1, chess1.getBlackKing(),
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertFalse(chess1.avoidCheck(chess1.getBlackKing()));
        assertFalse(
                chess1.captureCheckingPiece(
                        chess1, chess1.getBlackKing(),
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertTrue(
                chess1.isBlackCheckmated(
                        chess1.getBlackKing(), chess1,
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        System.out.println(chess1.isGameOver());
    }
}
