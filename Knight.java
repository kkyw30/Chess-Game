package org.cis120.Chess;

import java.util.LinkedList;
import java.util.List;

public class Knight extends Piece {
    private int currRow; // keep track of which square Knight is currently in
    private int currCol;
    private List<Square> legalMoves;
    private boolean hasMoved = false;

    public Knight(boolean isWhite, int currRow, int currCol, String imageFile) {
        super(isWhite, imageFile, currRow, currCol);
        this.currRow = currRow;
        this.currCol = currCol;
        legalMoves = new LinkedList<>();
    }

    // get current row
    public int getCurrRow() {
        return this.currRow;
    }

    // get current file
    public int getCurrCol() {
        return this.currCol;
    }

    public boolean isLegalMove(Square to, Square[][] board) {
        int startRow = this.currRow;
        int startCol = this.currCol;
        int endRow = to.getRow();
        int endCol = to.getColumn();

        int rowDifference = Math.abs(endRow - startRow);
        int colDifference = Math.abs(endCol - startCol);

        // can't capture your own piece
        if (to.getCurrentPiece() != null && to.getCurrentPiece().getColor() == this.getColor()) {
            return false;
        }

        // can't move off the board (2D array)
        if (endRow < 0 || endRow > 7 || endCol < 0 || endCol > 7) {
            return false;
        }
        // returns true if attempted move is an L-shape
        return ((rowDifference == 2 && colDifference == 1)
                || (rowDifference == 1 && colDifference == 2));
    }

    // add all legal moves
    public void addLegalMoves(Square[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.isLegalMove(board[i][j], board)) {
                    legalMoves.add(board[i][j]);
                }
            }
        }
    }

    // access list of all legal moves
    public List<Square> getLegalMoves() {
        return this.legalMoves;
    }

    // add one Square at a time if we're in check
    public void addLegalMoveForCheck(Square some) {
        this.legalMoves.add(some);
    }

    public boolean getHasMoved() {
        return this.hasMoved;
    }

    // move the Knight
    public void move(Square from, Square to, Square[][] board) {
        int startRow = from.getRow();
        int startCol = from.getColumn();
        to.changePiece(this);
        this.currRow = to.getRow();
        this.currCol = to.getColumn();
        // hasMoved = true;
        from.changePiece(null);
    }

}
