package org.cis120.Chess;

import java.util.LinkedList;
import java.util.List;

public class Bishop extends Piece {
    private int currRow;
    private int currCol;
    private List<Square> legalMoves;
    private boolean hasMoved = false;

    public Bishop(boolean isWhite, int currRow, int currCol, String imageFile) {
        super(isWhite, imageFile, currRow, currCol);
        this.currRow = currRow;
        this.currCol = currCol;
        legalMoves = new LinkedList<>();
    }

    public int getCurrRow() {
        return this.currRow;
    }

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

        // can't move off the board
        if (endRow > 7 || endRow < 0 || endCol > 7 || endCol < 0) {
            return false;
        }

        // haven't actually moved anywhere
        if (rowDifference == 0 && colDifference == 0) {
            return false;
        }

        // won't be a diagonal movement
        if (rowDifference != colDifference) {
            return false;
        }

        // can't capture your own piece
        if (to.getCurrentPiece() != null && to.getCurrentPiece().getColor() == this.getColor()) {
            return false;
        }

        // check if there's any Piece blocking the diagonal path to the target Square
        // four different scenarios (depending on relative positions of Bishop and King)
        if (endRow > startRow && endCol > startCol) {
            int difference = endRow - startRow; // colDifference should be the same
            for (int i = 1; i < difference; i++) {
                if (board[startRow + i][startCol + i].containsPiece()) {
                    return false;
                }
            }
        } else if (endRow > startRow && endCol < startCol) {
            int difference = endRow - startRow;
            for (int i = 1; i < difference; i++) {
                if (board[startRow + i][startCol - i].containsPiece()) {
                    return false;
                }
            }
        } else if (endRow < startRow && endCol > startCol) {
            int difference = startRow - endRow;
            for (int i = 1; i < difference; i++) {
                if (board[startRow - i][startCol + i].containsPiece()) {
                    return false;
                }
            }
        } else if (endRow < startRow && endCol < startCol) {
            int difference = startRow - endRow;
            for (int i = 1; i < difference; i++) {
                if (board[startRow - i][startCol - i].containsPiece()) {
                    return false;
                }
            }
        }
        return true; // returns true if we pass all the checks
    }

    // add all legal moves to the Bishop's list of legal moves
    public void addLegalMoves(Square[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.isLegalMove(board[i][j], board)) {
                    legalMoves.add(board[i][j]);
                }
            }
        }
    }

    // want to be able to directly modify this list
    public List<Square> getLegalMoves() {
        return this.legalMoves;
    }

    // if we're in check, only add one square at a time
    public void addLegalMoveForCheck(Square some) {
        this.legalMoves.add(some);
    }

    public boolean getHasMoved() {
        return this.hasMoved;
    }

    // move the Bishop
    public void move(Square from, Square to, Square[][] board) {
        int startRow = from.getRow();
        int startCol = from.getColumn();
        to.changePiece(this);
        this.currRow = to.getRow();
        this.currCol = to.getColumn();
        from.changePiece(null);
    }
}
