package org.cis120.Chess;

import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {
    private boolean hasMoved = false;
    private int currRow;
    private int currCol;
    private List<Square> legalMoves;

    public Pawn(boolean isWhite, int currRow, int currCol, String imageFile) {
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

    // determine if the Pawn's attempted move is a legal move
    public boolean isLegalMove(Square to, Square[][] board) {
        int startRow = this.currRow;
        int startCol = this.currCol;
        int endRow = to.getRow();
        int endCol = to.getColumn();

        int rowDifference = endRow - startRow;
        int colDifference = Math.abs(endCol - startCol);

        int rd = Math.abs(endRow - startRow);

        // make sure that Pawns of either color can't move backward or sideways
        if (!this.getColor()) { // case for black Pawn (top of 2D array is row 7)
            if (rowDifference >= 0 || rowDifference < -2) {
                return false;
            }
        } else { // case for white Pawn
            if (rowDifference <= 0 || rowDifference > 2) {
                return false;
            }
        }

        // Pawn can only move diagonally if it's capturing a Piece
        if (!to.containsPiece() && colDifference != 0) {
            return false;
        } else if (to.containsPiece()) {
            if (colDifference > 1 || rd > 1) { // can't capture more than one row or column away
                return false;
            }
        }

        // Pawn can't capture straight forward, even if Square is occupied by opposing
        // piece
        if (this.getColor()) {
            if (to.containsPiece() && startCol == endCol && endRow == startRow + 1) {
                return false;
            }
        } else { // since Black Pawns go in opposite direction
            if (to.containsPiece() && startCol == endCol && endRow == startRow - 1) {
                return false;
            }
        }

        // can't move sideways, no matter what
        if (colDifference == 1 && rowDifference == 0) {
            return false;
        }

        // Pawn can't jump over blocking Piece
        if (startCol == endCol && this.getColor()) {
            int difference = endRow - startRow;
            for (int i = 1; i <= difference; i++) {
                if (board[startRow + i][startCol].containsPiece()) {
                    return false;
                }
            }
        } else if (startCol == endCol && !this.getColor()) {
            int difference = startRow - endRow;
            for (int i = 1; i <= difference; i++) {
                if (board[startRow - i][startCol].containsPiece()) {
                    return false;
                }
            }
        }

        int difference = Math.abs(endCol - startCol);
        if (this.hasMoved) {
            if (difference > 1) {
                return false;
            }
        }

        // can't capture your own piece
        if (to.getCurrentPiece() != null && this.getColor() == to.getCurrentPiece().getColor()) {
            return false;
        }
        return true; // true if we pass all the other checks
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

    // add one move at a time for when we're in check
    public void addLegalMoveForCheck(Square some) {
        this.legalMoves.add(some);
    }

    // move the Pawn
    public void move(Square from, Square to, Square[][] board) {
        to.changePiece(this);
        this.currRow = to.getRow();
        this.currCol = to.getColumn();
        this.hasMoved = true;
        from.changePiece(null);
    }

    public boolean getHasMoved() {
        return this.hasMoved;
    }

}
