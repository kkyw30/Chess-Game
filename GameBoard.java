package org.cis120.Chess;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Chess chess; // model for the game
    private JLabel status; // current status text

    // for the currently selected Piece
    private Piece currentPiece = null; // starts off as having nothing selected
    private double currX = -1;
    private double currY = -1;

    private boolean whiteToMove = true;

    // starting Square from where we want to move
    private Square start;

    // Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    // Piece image file names (to draw actual piece)
    private static final String WHITE_KING = "White_King.png";
    private static final String BLACK_KING = "Black_King.png";
    private static final String WHITE_QUEEN = "White_Queen.png";
    private static final String BLACK_QUEEN = "Black_Queen.png";
    private static final String WHITE_ROOK = "White_Rook.png";
    private static final String BLACK_ROOK = "Black_Rook.png";
    private static final String WHITE_BISHOP = "White_Bishop.png";
    private static final String BLACK_BISHOP = "Black_Bishop.png";
    private static final String WHITE_KNIGHT = "White_Knight.png";
    private static final String BLACK_KNIGHT = "Black_Knight.png";
    private static final String WHITE_PAWN = "White_Pawn.png";
    private static final String BLACK_PAWN = "Black_Pawn.png";

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        chess = new Chess(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Point initial = e.getPoint();
                currX = initial.getX();
                currY = initial.getY();

                if (chess.isGameOver()) {
                    return;
                }

                // convert mouse coordinates to 2D array coordinates
                int col = getCol(currX);
                int row = getRow(currY);

                // record starting Square
                start = chess.getBoard()[row][col];
                if (start.containsPiece()) {
                    currentPiece = start.getCurrentPiece(); // record current Piece
                    if (currentPiece.getColor() && chess.getCurrentPlayer()) {
                        return;
                    }
                    if (!currentPiece.getColor() && !chess.getCurrentPlayer()) {
                        return;
                    }
                } else {
                    status.setText("choose a Piece");
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                currX = e.getX();
                currY = e.getY();
                int col = getCol(currX);
                int row = getRow(currY);

                Square selected = chess.getBoard()[row][col];

                // determine if attempted move is legal
                if (chess.makeMove(start, selected, chess.getBoard())) {
                    repaint(); // only repaint the board for legal move
                    updateStatus();
                } else {
                    status.setText("illegal move");
                    return;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                currX = e.getX();
                currY = e.getY();

                repaint();
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        chess.reset();
        status.setText("White to move");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (chess.getCurrentPlayer()) {
            status.setText("White to move");
        } else {
            status.setText("Black to move");
        }

        if (chess.isGameOver()) {
            if (chess.getCurrentPlayer()) { // if it was going to be white's move
                status.setText("Black Wins!");
            } else { // if it was going to be black's move
                status.setText("White Wins!");
            }
        }
    }

    //
    public void resign() {
        if (!chess.resign()) {
            status.setText("Black Wins by Resignation!");
        } else {
            status.setText("White Wins by Resignation!");
        }
    }

    // inverse of getX in Square class (input will be mouse Xcoord)
    public int getCol(double x) {
        return (int) x / 50;
    }

    // inverse of getY in Square class
    public int getRow(double y) {
        return 7 - (int) y / 50;
    }

    /**
     * Draws the game board. Divides the board into 64 Squares, and then draws
     * the Piece on the Squares for the Squares that currently have Pieces
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the 64 Squares
        g.drawLine(0, 0, 0, 400);
        g.drawLine(50, 0, 50, 400);
        g.drawLine(100, 0, 100, 400);
        g.drawLine(150, 0, 150, 400);
        g.drawLine(200, 0, 200, 400);
        g.drawLine(250, 0, 250, 400);
        g.drawLine(300, 0, 300, 400);
        g.drawLine(350, 0, 350, 400);
        g.drawLine(400, 0, 400, 400);

        g.drawLine(0, 0, 400, 0);
        g.drawLine(0, 50, 400, 50);
        g.drawLine(0, 100, 400, 100);
        g.drawLine(0, 150, 400, 150);
        g.drawLine(0, 200, 400, 200);
        g.drawLine(0, 250, 400, 250);
        g.drawLine(0, 300, 400, 300);
        g.drawLine(0, 350, 400, 350);
        g.drawLine(0, 400, 400, 400);

        Square[][] board = chess.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j].drawSquare(g);
                if (board[i][j].containsPiece()) {
                    board[i][j].getCurrentPiece().drawPiece(g, j, i);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
