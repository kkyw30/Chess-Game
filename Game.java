package org.cis120.Chess;

import javax.swing.*;

public class Game {
    /**
     * Main method run to start and run the game. Initializes the runnable game
     * class of your choosing and runs it. IMPORTANT: Do NOT delete! You MUST
     * include a main method in your final submission.
     */
    public static void main(String[] args) {
        Runnable game = new RunChess(); // Set the game you want to run
                                        // here
        SwingUtilities.invokeLater(game);
    }
}
