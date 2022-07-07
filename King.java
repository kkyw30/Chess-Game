package org.cis120.Chess;

import java.util.LinkedList;
import java.util.List;

public class King extends Piece {
    private boolean hasMoved = false; // important for determining whether we can still
                                      // castle--starts as false
    private int currRow;
    private int currCol; // will need to update this in move function
    private List<Square> legalMoves; // will need this so Chess model knows each piece's legal moves

    public King(boolean isWhite, int currRow, int currCol, String imageFile) {
        super(isWhite, imageFile, currRow, currCol);
        this.currRow = currRow;
        this.currCol = currCol;
        legalMoves = new LinkedList<>();
    }

    // access King's current coordinates
    public int getCurrRow() {
        return this.currRow;
    }

    public int getCurrCol() {
        return this.currCol;
    }

    // determine if the King's attempted move is a legal move
    public boolean isLegalMove(Square to, Square[][] board) {
        int startRow = this.currRow;
        int startCol = this.currCol;
        int endRow = to.getRow();
        int endCol = to.getColumn();

        // can't move off the board
        if (endRow > 7 || endCol > 7 || endRow < 0 || endCol < 0) {
            return false;
        }

        int rowDifference = Math.abs(endRow - startRow);
        int colDifference = Math.abs(endCol - startCol);

        // no move actually made
        if (rowDifference == 0 && colDifference == 0) {
            return false;
        }

        // since King can only move one square at a time
        if (rowDifference > 1 || colDifference > 1) {
            return false;
        }

        // can't capture your own piece
        if (to.getCurrentPiece() != null && this.getColor() == to.getCurrentPiece().getColor()) {
            return false;
        }

        // for iterating through the 8 squares immediately around the King
        int newStartRow = to.getRow() - 1;
        int newStartCol = to.getColumn() - 1;
        int newEndRow = to.getRow() + 1;
        int newEndCol = to.getColumn() + 1;

        // do these checks so we don't get ArrayIndexOutOfBounds
        if (newStartRow < 0) {
            newStartRow = 0;
        }
        if (newStartCol < 0) {
            newStartCol = 0;
        }
        if (newEndRow >= 8) {
            newEndRow = 7;
        }
        if (newEndCol >= 8) {
            newEndCol = 7;
        }

        // can't move next to the other King
        for (int i = newStartRow; i <= newEndRow; i++) {
            for (int j = newStartCol; j <= newEndCol; j++) {
                if (board[i][j].getCurrentPiece() instanceof King &&
                        board[i][j].getCurrentPiece() != this) {
                    return false;
                }
            }
        }
        return true; // true if we pass all the other checks
    }

    // add all legal moves to the King's list of legal moves
    public void addLegalMoves(Square[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.isLegalMove(board[i][j], board)) {
                    legalMoves.add(board[i][j]);
                } else {
                    legalMoves.remove(board[i][j]);
                }
            }
        }
    }

    // access list of legal moves, want to be able to change directly
    public List<Square> getLegalMoves() {
        return this.legalMoves;
    }

    // if we're in check, add one legal Square at a time
    public void addLegalMoveForCheck(Square some) {
        this.legalMoves.add(some);
    }

    // move the King
    public void move(Square from, Square to, Square[][] board) {
        int startRow = from.getRow();
        int startCol = from.getColumn();
        to.changePiece(this);
        this.currRow = to.getRow();
        this.currCol = to.getColumn();
        this.hasMoved = true;
        from.changePiece(null);
    }

    // useful for knowing if we can castle
    public boolean getHasMoved() {
        return this.hasMoved;
    }

}
