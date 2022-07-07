package org.cis120.Chess;

import java.util.LinkedList;
import java.util.List;

public class Queen extends Piece {

    private int currRow;
    private int currCol;
    private List<Square> legalMoves;
    private boolean hasMoved = false;

    public Queen(boolean isWhite, int currRow, int currCol, String imageFile) {
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

        // check to make sure that Queen has actually moved (so we can update turn
        // properly)
        if (rowDifference == 0 && colDifference == 0) {
            return false;
        }

        // can't capture your own piece
        if (to.getCurrentPiece() != null && to.getCurrentPiece().getColor() == this.getColor()) {
            return false;
        }

        // make sure that Queen is either moving along row, column, or a diagonal
        if (((startRow != endRow) && (startCol != endCol)) && rowDifference != colDifference) {
            return false;
        }

        // if Queen's moving along a row or column, check that there's nothing on column
        // blocking Queen's path
        if (startRow == endRow && endCol > startCol) {
            int difference = endCol - startCol;
            for (int i = 1; i < difference; i++) {
                if (board[startRow][i].containsPiece()) {
                    return false;
                }
            }
        } else if (startRow == endRow && endCol < startCol) {
            int difference = startCol - endCol;
            for (int i = 1; i < difference; i++) {
                if (board[startRow][startCol - i].containsPiece()) {
                    return false;
                }
            }
        } else if (startCol == endCol && endRow > startRow) {
            int difference = endRow - startRow;
            for (int i = 1; i < difference; i++) {
                if (board[startRow + i][startCol].containsPiece()) {
                    return false;
                }
            }
        } else if (startCol == endCol && endRow < startRow) {
            int difference = startRow - endRow;
            for (int i = 1; i < difference; i++) {
                if (board[startRow - i][startCol].containsPiece()) {
                    return false;
                }
            }
        }

        // if Queen moves along diagonal, check that there's nothing on diagonal
        // blocking Queen's path
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

    // get all legal moves
    public List<Square> getLegalMoves() {
        return this.legalMoves;
    }

    // if we're in check, add one Square at a time
    public void addLegalMoveForCheck(Square some) {
        this.legalMoves.add(some);
    }

    public boolean getHasMoved() {
        return this.hasMoved;
    }

    // move the Queen
    public void move(Square from, Square to, Square[][] board) {
        int startRow = from.getRow();
        int startCol = from.getColumn();
        to.changePiece(this);
        board[to.getRow()][to.getColumn()].changePiece(this);
        this.currRow = to.getRow();
        this.currCol = to.getColumn();
        hasMoved = true;
        from.changePiece(null);
    }
}
