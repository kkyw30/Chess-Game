package org.cis120.Chess;

import java.io.IOException;
import java.util.List;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.util.Objects;

public abstract class Piece implements Comparable {
    private final boolean isWhite;
    private BufferedImage image;
    private int currRow;
    private int currCol;

    public Piece(boolean isWhite, String imageFile, int currRow, int currCol) {
        this.isWhite = isWhite;
        this.currRow = currRow;
        this.currCol = currCol;

        try {
            if (this.image == null) {
                this.image = ImageIO.read(new File(imageFile));
            }
        } catch (IOException ex) {
            System.out.println("File not found");
        }
    }

    public boolean getColor() {
        return this.isWhite;
    }

    public Image getImage() {
        return this.image;
    }

    // get X-coordinate in terms of pixels
    public int getX(int col) {
        return col * 50;
    }

    // get Y-coordinate in terms of pixels
    public int getY(int row) {
        return (7 - row) * 50;
    }

    public void drawPiece(Graphics g, int col, int row) {
        int x = getX(col);
        x += 3;
        int y = getY(row);
        y += 3;
        g.drawImage(this.image.getScaledInstance(44, 44, 0), x, y, null); // don't need an Image
                                                                          // observer
    }

    // define methods that all instances of Piece (static type) need to satisfy
    public abstract boolean isLegalMove(Square to, Square[][] board);

    public abstract int getCurrRow();

    public abstract int getCurrCol();

    public abstract void move(Square from, Square to, Square[][] board);

    public abstract void addLegalMoves(Square[][] board);

    public abstract List<Square> getLegalMoves();

    public abstract void addLegalMoveForCheck(Square some);

    public abstract boolean getHasMoved();

    // Override compareTo, equals, and hashCode
    @Override
    public int compareTo(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (this.equals(o)) {
            return 0;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Piece)) {
            return false;
        }
        Piece piece = (Piece) o;
        return this.getCurrRow() == piece.getCurrRow() && this.getCurrCol() == piece.getCurrCol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isWhite, currRow, currCol);
    }
}
