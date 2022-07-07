package org.cis120.Chess;

import java.util.LinkedList;
import java.util.List;

public class Rook extends Piece {
    private int currRow;
    private int currCol;
    private boolean hasMoved = false; // initialize to false--keep track of this for castling
    private List<Square> legalMoves;

    public Rook(boolean isWhite, int currRow, int currCol, String imageFile) {
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

        // can't move off the board
        if (endRow > 7 || endRow < 0 || endCol > 7 || endCol < 0) {
            return false;
        }

        // check that the Rook has actually moved (so we can update turn properly)
        if (startRow == endRow && startCol == endCol) {
            return false;
        }

        // can't capture your own piece
        if (to.getCurrentPiece() != null && to.getCurrentPiece().getColor() == this.getColor()) {
            return false;
        }

        // since Rook can only move in straight lines, so either the column or row index
        // will stay the same
        if ((startRow != endRow) && (startCol != endCol)) {
            return false;
        }

        // if there is any piece blocking way from starting square to ending square, we
        // know this is illegal move
        // because the Rook can't jump over pieces
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
