package org.cis120.Chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class ChessTest {
    private Chess chess = new Chess();
    private Square[][] board = chess.getBoard();

    // Basic Movement Tests: these tests test the basic movement mechanics of each
    // of the
    // Piece types. Specifically, they test that moves that should be legal are
    // actually legal,
    // and make sure that the Piece is able to obtain the correct number of legal
    // moves that
    // it can make at any given time. These tests also make sure that Pieces are
    // able to take
    // account of other Pieces blocking their path (which limits the number of legal
    // moves),
    // and also makes sure that Pieces can't capture Pieces of their own color.

    @Test
    public void testBasicPawnMovement() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(board1[1][4].getCurrentPiece().isLegalMove(board1[3][4], board1));
        assertTrue(board1[1][4].getCurrentPiece().isLegalMove(board1[2][4], board1));
        assertFalse(board1[1][4].getCurrentPiece().isLegalMove(board1[2][5], board1));
        assertFalse(board1[1][4].getCurrentPiece().isLegalMove(board1[4][4], board1));
        board1[1][4].getCurrentPiece().addLegalMoves(board1);
        assertEquals(2, board1[1][4].getCurrentPiece().getLegalMoves().size());
    }

    @Test
    public void testPawnMovementBlocked() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        Pawn wp = new Pawn(true, 3, 4, "files/White_Pawn.png");
        board1[3][4].changePiece(wp);
        Pawn bp = new Pawn(false, 4, 4, "files/Black_Pawn.png");
        board1[4][4].changePiece(bp);
        wp.addLegalMoves(board1);
        assertFalse(wp.isLegalMove(board1[5][4], board1));
        assertEquals(0, wp.getLegalMoves().size());
        bp.addLegalMoves(board1);
        assertEquals(0, bp.getLegalMoves().size());
    }

    @Test
    public void testBasicKingMovement() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        King wk = new King(true, 4, 4, "files/White_King.png");
        board1[4][4].changePiece(wk);
        wk.addLegalMoves(board1);
        assertEquals(8, wk.getLegalMoves().size());
        King bk = new King(false, 7, 7, "files/Black_King.png");
        board1[7][7].changePiece(bk);
        bk.addLegalMoves(board1);
        assertEquals(3, bk.getLegalMoves().size());
    }

    @Test
    public void testMoveNextToOtherKingBad() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        King wk = new King(true, 4, 4, "files/White_King.png");
        board1[4][4].changePiece(wk);
        King bk = new King(false, 6, 4, "files/Black_King.png");
        board1[4][6].changePiece(bk);
        bk.addLegalMoves(board1);
        assertFalse(wk.isLegalMove(board1[4][5], board1));
        assertFalse(wk.isLegalMove(board1[5][5], board1));
        wk.addLegalMoves(board1);
        assertEquals(5, wk.getLegalMoves().size());
    }

    @Test
    public void testBasicKnightMovements() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        Knight wn = new Knight(true, 4, 4, "files/White_Knight.png");
        board1[4][4].changePiece(wn);
        wn.addLegalMoves(board1);
        assertEquals(8, wn.getLegalMoves().size());
        Pawn bp = new Pawn(false, 6, 5, "files/Black_Pawn.png");
        board1[6][5].changePiece(bp);
        Knight bn = new Knight(false, 7, 7, "files/Black_Knight.png");
        board1[7][7].changePiece(bn);
        bn.addLegalMoves(board1);
        assertEquals(1, bn.getLegalMoves().size());
        Knight wn2 = new Knight(true, 0, 0, "files/White_Knight.png");
        board1[0][0].changePiece(wn2);
        Pawn wp = new Pawn(true, 1, 2, "files/White_Pawn.png");
        board1[1][2].changePiece(wp);
        wn2.addLegalMoves(board1);
        assertEquals(1, wn2.getLegalMoves().size());
    }

    @Test
    public void testBasicBishopMovements() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        Bishop wb = new Bishop(true, 4, 4, "files/White_Bishop.png");
        board1[4][4].changePiece(wb);
        wb.addLegalMoves(board1);
        assertTrue(wb.isLegalMove(board1[7][7], board1));
        assertTrue(wb.isLegalMove(board1[7][1], board1));
        assertTrue(wb.isLegalMove(board1[0][0], board1));
        assertTrue(wb.isLegalMove(board1[1][7], board1));
        assertEquals(13, wb.getLegalMoves().size());
        Bishop bb = new Bishop(false, 7, 7, "files/Black_Bishop.png");
        board1[7][7].changePiece(bb);
        bb.addLegalMoves(board1);
        assertEquals(3, bb.getLegalMoves().size());
        Bishop wb2 = new Bishop(true, 7, 0, "files/White_Bishop.png");
        board1[7][0].changePiece(wb2);
        Pawn wp = new Pawn(true, 6, 1, "files/White_Pawn.png");
        board1[6][1].changePiece(wp);
        wb2.addLegalMoves(board1);
        assertEquals(0, wb2.getLegalMoves().size());
        Bishop bb2 = new Bishop(false, 3, 0, "files/Black_Bishop.png");
        board1[3][0].changePiece(bb2);
        Pawn bp = new Pawn(false, 4, 1, "files/Black_Pawn.png");
        board1[4][1].changePiece(bp);
        Pawn bp2 = new Pawn(false, 2, 1, "files/Black_Pawn.png");
        board1[2][1].changePiece(bp);
        bb2.addLegalMoves(board1);
        assertEquals(0, bb2.getLegalMoves().size());
    }

    @Test
    public void testBasicRookMovements() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        Rook wr = new Rook(true, 4, 4, "files/White_Rook.png");
        board1[4][4].changePiece(wr);
        wr.addLegalMoves(board1);
        assertEquals(14, wr.getLegalMoves().size());
        Rook br = new Rook(false, 4, 7, "files?Black_Rook.png");
        board1[4][7].changePiece(br);
        br.addLegalMoves(board1);
        assertEquals(10, br.getLegalMoves().size());
        Rook wr2 = new Rook(true, 0, 0, "files/White_Rook.png");
        board1[0][0].changePiece(wr2);
        Queen wq = new Queen(true, 0, 1, "files/White_Queen.png");
        board1[0][1].changePiece(wq);
        assertFalse(wr2.isLegalMove(board1[0][1], board1));
    }

    @Test
    public void testBasicQueenMovements() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        Queen wq = new Queen(true, 4, 4, "files/White_Queen.png");
        board1[4][4].changePiece(wq);
        wq.addLegalMoves(board1);
        assertEquals(27, wq.getLegalMoves().size());
        Queen bq = new Queen(false, 1, 7, "files/Black_Queen.png");
        board1[1][7].changePiece(bq);
        bq.addLegalMoves(board1);
        assertEquals(18, bq.getLegalMoves().size());
        Queen bq2 = new Queen(false, 0, 0, "files/Black_Queen.png");
        board1[0][0].changePiece(bq2);
        King bk = new King(false, 1, 0, "files/Black_King.png");
        board1[1][0].changePiece(bk);
        assertFalse(bq2.isLegalMove(board1[1][0], board1));
    }

    // Setup test: make sure board is initialized properly
    // white Piece have legal moves but black doesn't because the game
    // starts off as white to move
    @Test
    public void testSetup() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertEquals(16, chess1.getCurrentWhitePieces().size());
        assertEquals(16, chess1.getCurrentBlackPieces().size());
        assertFalse(chess1.isInCheckWhiteKing(chess1.getWhiteKing(), chess1));
        assertFalse(chess1.isInCheckBlackKing(chess1.getBlackKing(), chess1));
        assertTrue(board1[0][4].getCurrentPiece() instanceof King);
        assertTrue(board1[7][4].getCurrentPiece() instanceof King);
        board1[0][6].getCurrentPiece().addLegalMoves(board1);
        assertEquals(2, board1[0][6].getCurrentPiece().getLegalMoves().size());
        assertEquals(0, board1[7][6].getCurrentPiece().getLegalMoves().size());
    }

    // Check tests: these are an integral part of ensuring our game follows the
    // rules of Chess

    @Test
    public void testRookRowCheck() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Rook br = new Rook(false, 3, 0, "files/Black_Rook.png");
        board1[3][0].changePiece(br);
        chess1.getCurrentBlackPieces().add(br);
        br.addLegalMoves(board1);
        King wk = new King(true, 3, 7, "files/White_King.png");
        board1[3][7].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        wk.addLegalMoves(board1);
        assertTrue(chess1.isInCheckWhiteKing(wk, chess1));
        assertEquals(3, chess1.getAttackRow());
        assertEquals(0, chess1.getAttackCol());
        assertTrue(chess1.avoidCheck(wk));
        Pawn wp = new Pawn(true, 1, 6, "files/White_Pawn.png");
        board1[1][6].changePiece(wp);
        chess1.getCurrentWhitePieces().add(wp);
        assertEquals(2, chess1.getCurrentWhitePieces().size());
        assertTrue(
                chess1.blockCheck(chess1, wk, board1[chess1.getAttackRow()][chess1.getAttackCol()])
        );
    }

    @Test
    public void testRookColCheck() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Rook wr = new Rook(true, 2, 6, "files/White_Rook.png");
        board1[2][6].changePiece(wr);
        chess1.getCurrentWhitePieces().add(wr);
        wr.addLegalMoves(board1);
        King bk = new King(false, 5, 6, "files/Black_King.png");
        board1[5][6].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        assertTrue(chess1.isInCheckBlackKing(bk, chess1));
        chess1.avoidCheck(bk);
        assertTrue(!bk.getLegalMoves().isEmpty());
        assertTrue(chess1.avoidCheck(bk));
        Rook br = new Rook(false, 4, 3, "files/Black_Rook.png");
        board1[4][3].changePiece(br);
        chess1.getCurrentBlackPieces().add(br);
        assertEquals(2, chess1.getAttackRow());
        assertEquals(6, chess1.getAttackCol());
        assertTrue(
                chess1.blockCheck(chess1, bk, board1[chess1.getAttackRow()][chess1.getAttackCol()])
        );
        assertEquals(1, br.getLegalMoves().size());
    }

    @Test
    public void testBishopCheck() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Bishop bb = new Bishop(false, 5, 3, "files/Black_Bishop.png");
        board1[5][3].changePiece(bb);
        chess1.getCurrentBlackPieces().add(bb);
        bb.addLegalMoves(board1);
        King wk = new King(true, 7, 1, "files/White_King.png");
        board1[7][1].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        assertTrue(chess1.isInCheckWhiteKing(wk, chess1));
        chess1.avoidCheck(wk);
        assertTrue(chess1.avoidCheck(wk));
        Queen wq = new Queen(true, 4, 4, "files/White_Queen.png");
        board1[4][4].changePiece(wq);
        chess1.getCurrentWhitePieces().add(wq);
        assertTrue(
                chess1.captureCheckingPiece(
                        chess1, wk, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertEquals(1, wq.getLegalMoves().size());
    }

    @Test
    public void testKnightCheck() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Knight wn = new Knight(true, 5, 5, "files/White_Knight.png");
        board1[5][5].changePiece(wn);
        chess1.getCurrentWhitePieces().add(wn);
        wn.addLegalMoves(board1);
        King bk = new King(false, 7, 6, "files/Black_King.png");
        board1[7][6].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        assertTrue(chess1.isInCheckBlackKing(bk, chess1));
        assertTrue(chess1.avoidCheck(bk));
        assertFalse(
                chess1.blockCheck(chess1, bk, board1[chess1.getAttackRow()][chess1.getAttackCol()])
        );
        Pawn bp = new Pawn(false, 6, 4, "files/Black_Pawn.png");
        board1[4][6].changePiece(bp);
        chess1.getCurrentBlackPieces().add(bp);
        assertTrue(bp.isLegalMove(board1[5][5], board1));
        assertTrue(
                chess1.captureCheckingPiece(
                        chess1, bk, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
    }

    @Test
    public void testPawnCheck() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Pawn wp = new Pawn(true, 3, 4, "files/White_Pawn.png");
        board1[3][4].changePiece(wp);
        chess1.getCurrentWhitePieces().add(wp);
        wp.addLegalMoves(board1);
        King bk = new King(false, 4, 5, "files/Black_King.png");
        board1[4][5].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        Rook wr = new Rook(true, 3, 1, "files/White_Rook.png");
        board1[3][1].changePiece(wr);
        chess1.getCurrentWhitePieces().add(wr);
        assertTrue(chess1.isInCheckBlackKing(bk, chess1));
        assertFalse(
                chess1.blockCheck(chess1, bk, board1[chess1.getAttackRow()][chess1.getAttackCol()])
        );
        assertFalse(
                chess1.captureCheckingPiece(
                        chess1, bk, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertTrue(chess1.avoidCheck(bk));
    }

    @Test
    public void testQueenCheck() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Queen bq = new Queen(false, 5, 6, "files/Black_Queen.png");
        board1[5][6].changePiece(bq);
        chess1.getCurrentBlackPieces().add(bq);
        assertEquals(1, chess1.getCurrentBlackPieces().size());
        bq.addLegalMoves(board1);
        King wk = new King(true, 0, 6, "files/White_King.png");
        board1[0][6].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        Knight wn = new Knight(true, 0, 4, "files/White_Knight.png");
        board1[0][4].changePiece(wn);
        chess1.getCurrentWhitePieces().add(wn);
        assertTrue(chess1.isInCheckWhiteKing(wk, chess1));
        assertTrue(chess1.avoidCheck(wk));
        assertTrue(board1[0][4].getCurrentPiece().isLegalMove(board1[1][6], board1));
        assertTrue(chess1.avoidCheck(wk));
        assertFalse(
                chess1.captureCheckingPiece(
                        chess1, wk, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
    }

    // Checkmate Tests: these are important for ensuring that we can properly end
    // our game.
    // Below are some of the most common (and uncommon) scenarios for checkmate

    @Test
    public void testBackRankMateWithRook() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Rook wr = new Rook(true, 7, 0, "files/White_Rook.png");
        board1[7][0].changePiece(wr);
        chess1.getCurrentWhitePieces().add(wr);
        King bk = new King(false, 7, 6, "files/Black_King.png");
        board1[7][6].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        assertTrue(chess1.isInCheckBlackKing(bk, chess1));
        Pawn bp = new Pawn(false, 6, 6, "files/Black_Pawn.png");
        board1[6][6].changePiece(bp);
        chess1.getCurrentBlackPieces().add(bp);
        // bp.addLegalMoves(board1);
        Pawn bp2 = new Pawn(false, 6, 5, "files/Black_Pawn.png");
        board1[6][5].changePiece(bp2);
        chess1.getCurrentBlackPieces().add(bp2);
        // bp2.addLegalMoves(board1);
        Pawn bp3 = new Pawn(false, 6, 7, "files/Black_Pawn.png");
        board1[6][7].changePiece(bp3);
        chess1.getCurrentBlackPieces().add(bp3);
        // bp3.addLegalMoves(board1);
        assertEquals(7, chess1.getAttackRow());
        assertEquals(0, chess1.getAttackCol());
        assertFalse(chess1.avoidCheck(bk));
        assertFalse(
                chess1.captureCheckingPiece(
                        chess1, bk, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertFalse(
                chess1.blockCheck(chess1, bk, board1[chess1.getAttackRow()][chess1.getAttackCol()])
        );
        assertTrue(
                chess1.isBlackCheckmated(
                        bk, chess1, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
    }

    @Test
    public void testSideRankMateWithRook() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Rook br = new Rook(false, 7, 7, "files/Black_Rook.png");
        board1[7][7].changePiece(br);
        chess1.getCurrentBlackPieces().add(br);
        King wk = new King(true, 3, 7, "files/White_King.png");
        board1[3][7].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        Pawn wp = new Pawn(true, 3, 6, "files/White_Pawn.png");
        board1[3][6].changePiece(wp);
        chess1.getCurrentWhitePieces().add(wp);
        Pawn wp2 = new Pawn(true, 2, 6, "files/White_Pawn.png");
        board1[2][6].changePiece(wp2);
        chess1.getCurrentWhitePieces().add(wp);
        Pawn wp3 = new Pawn(true, 4, 6, "files/White_Pawn.png");
        board1[4][6].changePiece(wp3);
        chess1.getCurrentWhitePieces().add(wp3);
        assertTrue(chess1.isInCheckWhiteKing(wk, chess1));
        assertTrue(
                chess1.isWhiteCheckmated(
                        wk, chess1, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertTrue(chess1.isGameOver());
    }

    @Test
    public void testQueenAndKingCheckMate() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Queen wq = new Queen(true, 6, 6, "files/White_Queen.png");
        board1[6][6].changePiece(wq);
        chess1.getCurrentWhitePieces().add(wq);
        King bk = new King(false, 7, 6, "files/Black_King.png");
        board1[7][6].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        King wk = new King(true, 5, 6, "files/White_King.png");
        board1[5][6].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        assertTrue(chess1.isInCheckBlackKing(bk, chess1));
        assertFalse(
                chess1.captureCheckingPiece(
                        chess1, bk, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertTrue(
                chess1.isBlackCheckmated(
                        bk, chess1, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
    }

    @Test
    public void testRookAndKingCheckmate() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Rook br = new Rook(false, 7, 0, "files/Black_Rook.png");
        board1[7][0].changePiece(br);
        chess1.getCurrentBlackPieces().add(br);
        King wk = new King(true, 7, 6, "files/White_King.png");
        board1[7][6].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        King bk = new King(false, 5, 6, "files/Black_King.png");
        board1[5][6].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        assertTrue(chess1.isInCheckWhiteKing(wk, chess1));
        assertTrue(
                chess1.isWhiteCheckmated(
                        wk, chess1, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
    }

    @Test
    public void testSmothersMateKnight() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Knight bn = new Knight(false, 1, 5, "files/Black_Knight.png");
        board1[1][5].changePiece(bn);
        chess1.getCurrentBlackPieces().add(bn);
        King wk = new King(true, 0, 7, "files/White_King.png");
        board1[0][7].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        Queen wq = new Queen(true, 1, 7, "files/White_Queen.png");
        board1[1][7].changePiece(wq);
        chess1.getCurrentWhitePieces().add(wq);
        Pawn wp = new Pawn(true, 1, 6, "files/White_Pawn.png");
        board1[1][6].changePiece(wp);
        chess1.getCurrentWhitePieces().add(wp);
        Rook wr = new Rook(true, 0, 6, "files/White_Rook.png");
        board1[0][6].changePiece(wr);
        chess1.getCurrentWhitePieces().add(wr);
        assertTrue(chess1.isInCheckWhiteKing(wk, chess1));
        assertTrue(
                chess1.isWhiteCheckmated(
                        wk, chess1, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
    }

    @Test
    public void testTwoPawnsMate() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        King wk = new King(true, 5, 2, "files/White_King.png");
        board1[5][2].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        King bk = new King(false, 7, 2, "files/Black_King.png");
        board1[7][2].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        Pawn wp = new Pawn(true, 6, 2, "files/White_Pawn.png");
        board1[6][2].changePiece(wp);
        chess1.getCurrentWhitePieces().add(wp);
        Pawn wp2 = new Pawn(true, 6, 3, "files/White_Pawn.png");
        board1[6][3].changePiece(wp2);
        chess1.getCurrentWhitePieces().add(wp2);
        assertTrue(chess1.isInCheckBlackKing(bk, chess1));
        assertTrue(
                chess1.isBlackCheckmated(
                        bk, chess1, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
    }

    @Test
    public void testTwoBishopsMate() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        King bk = new King(false, 2, 6, "files/Black_King.png");
        board1[2][6].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        King wk = new King(true, 0, 7, "files/White_King.png");
        board1[0][7].changePiece(wk);
        chess1.getCurrentBlackPieces().add(wk);
        Bishop bb1 = new Bishop(false, 3, 3, "files/Black_Bishop.png");
        board1[3][3].changePiece(bb1);
        chess1.getCurrentBlackPieces().add(bb1);
        Bishop bb2 = new Bishop(false, 3, 4, "files/Black_Bishop.png");
        board1[3][4].changePiece(bb1);
        chess1.getCurrentBlackPieces().add(bb2);
        assertTrue(chess1.isInCheckWhiteKing(wk, chess1));
        assertTrue(
                chess1.isWhiteCheckmated(
                        wk, chess1, board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
    }

    // Move Tests: this is to ensure we can actually make moves and progress in the
    // game state
    @Test
    public void testE4E5() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(board1[1][4].getCurrentPiece().isLegalMove(board1[3][4], board1));
        assertTrue(chess1.makeMove(board1[1][4], board1[3][4], board1));
        assertFalse(chess1.getCurrentPlayer());
        assertTrue(chess1.makeMove(board1[6][4], board1[4][4], board1));
        assertTrue(chess1.getCurrentPlayer());
        assertEquals(16, chess1.getCurrentWhitePieces().size());
        assertEquals(16, chess1.getCurrentBlackPieces().size());
        assertTrue(board1[3][4].getCurrentPiece() instanceof Pawn);
        assertTrue(board1[4][4].getCurrentPiece() instanceof Pawn);
    }

    @Test
    public void testMoveUpdateCoordinates() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        board1[1][4].getCurrentPiece().move(board1[1][4], board1[3][4], board1);
        assertTrue(board1[3][4].containsPiece());
        assertFalse(board1[6][4].getCurrentPiece().isLegalMove(board1[3][7], board1));
    }

    @Test
    public void testScholarsMate() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(chess1.makeMove(board1[1][4], board1[3][4], board1)); // e4
        assertFalse(chess1.getCurrentPlayer());
        assertTrue(chess1.makeMove(board1[6][4], board1[4][4], board1)); // e5
        assertTrue(chess1.makeMove(board1[0][3], board1[2][5], board1)); // Qf3
        assertTrue(board1[2][5].containsPiece());
        assertTrue(chess1.makeMove(board1[7][1], board1[5][2], board1)); // Nc6
        assertTrue(chess1.makeMove(board1[0][5], board1[3][2], board1)); // Bc4
        assertTrue(chess1.makeMove(board1[7][5], board1[6][4], board1)); // Be7
        assertTrue(chess1.makeMove(board1[2][5], board1[6][5], board1)); // Qf7#
        assertTrue(chess1.isInCheckBlackKing(chess1.getBlackKing(), chess1));
        assertTrue(chess1.getBlackKing().getLegalMoves().isEmpty());
        assertTrue(
                chess1.isBlackCheckmated(
                        chess1.getBlackKing(), chess1,
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertFalse(chess1.makeMove(board1[6][0], board1[5][0], board1)); // game over
    }

    @Test
    public void testFoolsMate() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(chess1.makeMove(board1[1][5], board1[3][5], board1));
        assertFalse(chess1.getCurrentPlayer());
        assertTrue(chess1.makeMove(board1[6][4], board1[4][4], board1));
        assertTrue(chess1.makeMove(board1[1][6], board1[3][6], board1));
        assertTrue(chess1.makeMove(board1[7][3], board1[3][7], board1));
        assertTrue(chess1.isInCheckWhiteKing(chess1.getWhiteKing(), chess1));
        assertFalse(
                chess1.captureCheckingPiece(
                        chess1, chess1.getWhiteKing(),
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertFalse(chess1.avoidCheck(chess1.getWhiteKing()));
        assertEquals(3, chess1.getAttackRow());
        assertEquals(7, chess1.getAttackCol());
        assertTrue(
                chess1.isWhiteCheckmated(
                        chess1.getWhiteKing(), chess1,
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertTrue(chess1.isGameOver());
        assertFalse(chess1.makeMove(board1[0][6], board1[2][5], board1));
        assertFalse(chess1.makeMove(board1[7][6], board1[5][5], board1));
    }

    @Test
    public void testFoolsMateWhite() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(chess1.makeMove(board1[1][4], board1[3][4], board1));
        assertFalse(chess1.getCurrentPlayer());
        assertTrue(chess1.makeMove(board1[6][5], board1[4][5], board1));
        assertTrue(chess1.makeMove(board1[1][3], board1[3][3], board1));
        assertTrue(chess1.makeMove(board1[6][6], board1[4][6], board1));
        assertTrue(chess1.makeMove(board1[0][3], board1[4][7], board1));
        assertTrue(chess1.isInCheckBlackKing(chess1.getBlackKing(), chess1));
        assertFalse(
                chess1.captureCheckingPiece(
                        chess1, chess1.getBlackKing(),
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertFalse(chess1.avoidCheck(chess1.getBlackKing()));
        assertEquals(4, chess1.getAttackRow());
        assertEquals(7, chess1.getAttackCol());
        assertFalse(
                chess1.blockCheck(
                        chess1, chess1.getBlackKing(),
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertTrue(
                chess1.isBlackCheckmated(
                        chess1.getBlackKing(), chess1,
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertTrue(chess1.isGameOver());
    }

    // Make sure we can block and capture checking Pieces

    @Test
    public void testBishopCheckOpening() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(chess1.makeMove(board1[1][3], board1[3][3], board1));
        assertTrue(chess1.makeMove(board1[6][4], board1[4][4], board1));
        assertTrue(chess1.makeMove(board1[1][4], board1[3][4], board1));
        assertTrue(chess1.makeMove(board1[7][5], board1[3][1], board1));
        assertTrue(chess1.makeMove(board1[1][2], board1[2][2], board1));
        assertTrue(chess1.makeMove(board1[3][1], board1[2][2], board1));
        assertTrue(chess1.makeMove(board1[1][1], board1[2][2], board1));
        assertTrue(board1[2][2].getCurrentPiece() instanceof Pawn);
    }

    @Test
    public void testStandardQueenOpeningCheck() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(chess1.makeMove(board1[1][3], board1[3][3], board1));
        assertTrue(chess1.makeMove(board1[6][2], board1[4][2], board1));
        assertTrue(chess1.makeMove(board1[1][4], board1[3][4], board1));
        assertTrue(chess1.makeMove(board1[7][3], board1[4][0], board1));
        assertTrue(chess1.isInCheckWhiteKing(chess1.getWhiteKing(), chess1));
        assertTrue(chess1.makeMove(board1[0][2], board1[1][3], board1));
        assertTrue(board1[4][0].getCurrentPiece() instanceof Queen);
        assertFalse(chess1.isInCheckBlackKing(chess1.getBlackKing(), chess1));
        assertFalse(
                chess1.isBlackCheckmated(
                        chess1.getBlackKing(), chess1,
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertFalse(
                chess1.isWhiteCheckmated(
                        chess1.getWhiteKing(), chess1,
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertFalse(chess1.isGameOver());
    }

    @Test
    public void testBishopBlockCheckOnWhite() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(chess1.makeMove(board1[1][3], board1[3][3], board1));
        assertTrue(chess1.makeMove(board1[6][4], board1[4][4], board1));
        assertTrue(chess1.makeMove(board1[1][4], board1[3][4], board1));
        assertTrue(chess1.makeMove(board1[7][5], board1[3][1], board1));
        assertTrue(chess1.makeMove(board1[0][2], board1[1][3], board1));
        assertFalse(chess1.isInCheckWhiteKing(chess1.getWhiteKing(), chess1));
        assertTrue(board1[1][3].getCurrentPiece() instanceof Bishop);
    }

    @Test
    public void testBishopBlockCheckOnBlack() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(chess1.makeMove(board1[1][4], board1[3][4], board1));
        assertTrue(chess1.makeMove(board1[6][3], board1[4][3], board1));
        assertTrue(chess1.makeMove(board1[0][5], board1[4][1], board1));
        assertTrue(chess1.isInCheckBlackKing(chess1.getBlackKing(), chess1));
        assertTrue(chess1.makeMove(board1[7][2], board1[6][3], board1));
        assertFalse(chess1.isInCheckBlackKing(chess1.getBlackKing(), chess1));
        assertTrue(board1[6][3].getCurrentPiece() instanceof Bishop);
    }

    @Test
    public void testMovingIntoCheckBad() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(chess1.makeMove(board1[1][3], board1[3][3], board1));
        assertTrue(chess1.makeMove(board1[6][4], board1[5][4], board1));
        assertTrue(chess1.makeMove(board1[0][1], board1[2][2], board1));
        assertTrue(chess1.makeMove(board1[7][5], board1[3][1], board1));
        assertFalse(chess1.makeMove(board1[2][2], board1[4][3], board1));
    }

    // Stalemate Test
    @Test
    public void testStalemateKingAndQueen() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Queen bq = new Queen(false, 2, 5, "files/Black_Queen.png");
        board1[2][5].changePiece(bq);
        chess1.getCurrentBlackPieces().add(bq);
        King bk = new King(false, 2, 7, "files/Black_King.png");
        board1[2][7].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        King wk = new King(true, 0, 6, "files/White_King.png");
        board1[0][6].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        assertTrue(wk.getLegalMoves().isEmpty());
        assertFalse(chess1.isInCheckWhiteKing(wk, chess1));
        assertTrue(chess1.noMovesWhite());
        chess1.stalemate();
        assertTrue(chess1.isStalemated());
        assertFalse(chess1.makeMove(board1[0][6], board1[1][5], board1));
    }

    @Test
    public void testStalemateKingAndPawn() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        King bk = new King(false, 0, 7, "files/White_King.png");
        board1[0][7].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        Pawn bp = new Pawn(false, 1, 7, "files/Black_Pawn.png");
        board1[1][7].changePiece(bp);
        chess1.getCurrentBlackPieces().add(bp);
        King wk = new King(true, 0, 5, "files/White_King.png");
        board1[0][5].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        chess1.stalemate();
        assertTrue(chess1.isStalemated());
        assertFalse(chess1.makeMove(board1[0][7], board1[0][6], board1));
    }

    // Castling Tests: make sure our castling mechanics don't violate the rules of
    // Chess

    @Test
    public void testKingsideCastleWhiteAndBlack() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        King wk = new King(true, 0, 4, "files/White_King.png");
        board1[0][4].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        Rook wr = new Rook(true, 0, 7, "files/White_Rook.png");
        board1[0][7].changePiece(wr);
        chess1.getCurrentWhitePieces().add(wr);
        assertTrue(chess1.makeMove(board1[0][4], board1[0][6], board1));
        King bk = new King(false, 7, 4, "files/Black_King.png");
        board1[7][4].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        Rook br = new Rook(false, 7, 7, "files/Black_Rook.png");
        board1[7][7].changePiece(br);
        chess1.getCurrentBlackPieces().add(br);
        assertFalse(chess1.getCurrentPlayer());
        assertTrue(chess1.makeMove(board1[7][4], board1[7][6], board1));
    }

    @Test
    public void testKingSideCastleAfterKingHasMoved() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        King wk = new King(true, 0, 4, "files/White_King.png");
        board1[0][4].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        Rook wr = new Rook(true, 0, 7, "files/White_Rook.png");
        board1[0][7].changePiece(wr);
        chess1.getCurrentWhitePieces().add(wr);
        assertTrue(chess1.makeMove(board1[0][4], board1[0][3], board1));
        King bk = new King(false, 7, 4, "files/Black_King.png");
        board1[7][4].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        Rook br = new Rook(false, 7, 7, "files/Black_Rook.png");
        board1[7][7].changePiece(br);
        chess1.getCurrentBlackPieces().add(br);
        assertTrue(chess1.makeMove(board1[7][4], board1[7][3], board1));
        assertTrue(chess1.makeMove(board1[0][3], board1[0][4], board1));
        assertTrue(chess1.makeMove(board1[7][3], board1[7][4], board1));
        assertFalse(chess1.makeMove(board1[0][4], board1[0][6], board1));
        assertFalse(chess1.makeMove(board1[7][4], board1[7][6], board1));
    }

    @Test
    public void testQueenSideCastleWithObstruction() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        King wk = new King(true, 0, 4, "files/White_King.png");
        board1[0][4].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        Rook wr = new Rook(true, 0, 0, "files/White_Rook.png");
        board1[0][0].changePiece(wr);
        chess1.getCurrentWhitePieces().add(wr);
        Queen wq = new Queen(true, 0, 3, "files/White_Queen.png");
        board1[0][3].changePiece(wq);
        chess1.getCurrentWhitePieces().add(wq);
        assertFalse(chess1.makeMove(board1[0][4], board1[0][2], board1));
        assertTrue(chess1.makeMove(board1[0][3], board1[3][0], board1));
        King bk = new King(false, 7, 4, "files/Black_King.png");
        board1[7][4].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        Rook br = new Rook(false, 7, 0, "files/Black_Rook.png");
        board1[7][0].changePiece(br);
        chess1.getCurrentBlackPieces().add(br);
        assertTrue(chess1.makeMove(board1[7][4], board1[7][2], board1));
    }

    @Test
    public void castleIntoCheckBad() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        King wk = new King(true, 0, 4, "files/White_King.png");
        board1[0][4].changePiece(wk);
        chess1.getCurrentWhitePieces().add(wk);
        Rook wr = new Rook(true, 0, 0, "files/White_Rook.png");
        board1[0][0].changePiece(wr);
        chess1.getCurrentWhitePieces().add(wr);
        Queen bq = new Queen(false, 5, 2, "files/Black_Queen.png");
        board1[5][2].changePiece(bq);
        chess1.getCurrentBlackPieces().add(bq);
        assertFalse(chess1.makeMove(board1[0][4], board1[0][2], board1));
        assertTrue(chess1.makeMove(board1[0][4], board1[0][5], board1));
        King bk = new King(false, 7, 4, "files/Black_King.png");
        board1[7][4].changePiece(bk);
        chess1.getCurrentBlackPieces().add(bk);
        Rook br = new Rook(false, 7, 7, "files/Black_Rook.png");
        board1[7][7].changePiece(br);
        chess1.getCurrentBlackPieces().add(br);
        Queen wq = new Queen(true, 4, 6, "files/White_Queen.png");
        board1[4][6].changePiece(wq);
        chess1.getCurrentWhitePieces().add(wq);
        assertFalse(chess1.makeMove(board1[7][4], board1[7][6], board1));
    }

    // En Passant Tests

    @Test
    public void testEnPassantWhite() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Pawn wp = new Pawn(true, 4, 4, "files/White_Pawn.png");
        board1[4][4].changePiece(wp);
        chess1.getCurrentWhitePieces().add(wp);
        Pawn bp = new Pawn(false, 4, 5, "files/Black_Pawn.png");
        board1[4][5].changePiece(bp);
        chess1.getCurrentBlackPieces().add(bp);
        assertTrue(chess1.makeMove(board1[4][4], board1[5][5], board1));
        assertFalse(board1[4][5].containsPiece());
    }

    @Test
    public void testEnPassantBlack() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board1[i][j].changePiece(null);
            }
        }
        chess1.getCurrentWhitePieces().clear();
        chess1.getCurrentBlackPieces().clear();
        Pawn wp = new Pawn(true, 1, 4, "files/White_Pawn.png");
        board1[1][4].changePiece(wp);
        chess1.getCurrentWhitePieces().add(wp);
        Pawn bp = new Pawn(false, 3, 3, "files/Black_Pawn.png");
        board1[3][3].changePiece(bp);
        chess1.getCurrentBlackPieces().add(bp);
        assertTrue(chess1.makeMove(board1[1][4], board1[3][4], board1));
        assertTrue(chess1.makeMove(board1[3][3], board1[2][4], board1));
        assertFalse(board1[3][4].containsPiece());
    }

    // Test miniature game

    @Test
    public void testLegalMate() {
        Chess chess1 = new Chess();
        Square[][] board1 = chess1.getBoard();
        assertTrue(chess1.makeMove(board1[1][4], board1[3][4], board1));
        assertTrue(chess1.makeMove(board1[6][4], board1[4][4], board1));
        assertTrue(chess1.makeMove(board1[0][5], board1[3][2], board1));
        assertTrue(chess1.makeMove(board1[6][3], board1[5][3], board1));
        assertTrue(chess1.makeMove(board1[0][6], board1[2][5], board1));
        assertTrue(chess1.makeMove(board1[7][2], board1[3][6], board1));
        assertTrue(chess1.makeMove(board1[0][1], board1[2][2], board1));
        assertTrue(chess1.makeMove(board1[6][6], board1[5][6], board1));
        assertTrue(chess1.makeMove(board1[2][5], board1[4][4], board1));
        assertTrue(chess1.makeMove(board1[3][6], board1[0][3], board1));
        assertTrue(chess1.makeMove(board1[3][2], board1[6][5], board1));
        assertTrue(chess1.makeMove(board1[7][4], board1[6][4], board1));
        assertTrue(chess1.makeMove(board1[2][2], board1[4][3], board1));
        assertTrue(chess1.isInCheckBlackKing(chess1.getBlackKing(), chess1));
        assertFalse(
                chess1.blockCheck(
                        chess1, chess1.getBlackKing(),
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertFalse(chess1.avoidCheck(chess1.getBlackKing()));
        assertFalse(
                chess1.captureCheckingPiece(
                        chess1, chess1.getBlackKing(),
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
        assertTrue(
                chess1.isBlackCheckmated(
                        chess1.getBlackKing(), chess1,
                        board1[chess1.getAttackRow()][chess1.getAttackCol()]
                )
        );
    }

}