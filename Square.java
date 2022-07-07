package org.cis120.Chess;

import java.awt.Graphics;
import java.awt.Color;

public class Square {
    private Chess chess;
    private int row;
    private int col;
    private Piece piece;
    private final boolean color; // each square on board is either black or white (true if white
                                 // square)
    private final int fileNumber;

    public Square(int row, int col, Piece piece, boolean color) {
        this.row = row;
        this.col = col;
        this.piece = piece;
        this.color = color;
        fileNumber = col + 1; // since zero indexed
    }

    public Piece getCurrentPiece() {
        return this.piece; // null if empty
    }

    public void changePiece(Piece newPiece) {
        this.piece = newPiece; // in case piece moves to square or captures current piece
    }

    public boolean containsPiece() {
        return this.piece != null;
    }

    public boolean getSquareColor() {
        return this.color;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.col;
    }

    public int getX(int col) {
        int x = col * 50;
        return x;
    }

    public int getY(int row) {
        int y = (7 - row) * 50;
        return y;
    }

    // draw the square (based on its color)
    public void drawSquare(Graphics g) {
        if (this.color) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.WHITE);
        }
        int x = getX(this.col);
        int y = getY(this.row);
        g.fillRect(x, y, 50, 50);
    }

    // for when a Piece is captured
    public void capture(Piece np) {
        Piece original = this.getCurrentPiece();
        if (original.getColor()) {
            chess.getCurrentWhitePieces().remove(original);
        } else {
            chess.getCurrentBlackPieces().remove(original);
        }
        this.changePiece(np);
    }
}
