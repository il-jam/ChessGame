package chess.model;

import chess.TestHelper;
import chess.model.pieces.*;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Board class
 */
public class BoardTest {
    TestHelper testHelper = new TestHelper();
    /**
     * Test method for fillBoard and correct piece and color
     */
    @Test
    public void fillBoardTest(){
        Board board = new Board();
        Piece expectedKing = board.getTiles()[0][4].getValue().getPiece();
        ChessColor color = expectedKing.getColor();
        assertTrue(expectedKing instanceof King);
        assertEquals(color, ChessColor.Black);
    }

    /**
     * Test method for the movePieceFromStartToEndTile method
     */
    @Test
    public void movePieceFromStartToEndTileTest(){
        Board board = new Board();
        Tile startTile = board.getSpecificTile(6,0);
        Tile endTile = board.getSpecificTile(5,0);
        assertNull(endTile.getPiece());
        assertTrue(startTile.getPiece() instanceof Pawn);
        board.movePieceFromStartToEndTile(startTile,endTile);
        assertTrue(endTile.getPiece() instanceof Pawn);
    }

    /**
     * Test for the removePiece method
     */
    @Test
    public void removePieceTest(){
        Board board = new Board();
        Tile testTile = board.getSpecificTile(6, 0);
        assertTrue(testTile.getPiece() instanceof Pawn);
        board.removePiece(testTile);
        assertNull(testTile.getPiece());

    }

    /**
     * Test method for movePiece method
     */
    @Test
    public void movePieceTest(){
        Board board = new Board();

        MoveHistory moveHistory = new MoveHistory();
        board.setMoveHistory(moveHistory);

        // illegal EndTile for move
        Tile legalStartTile = board.getSpecificTile(7,1);
        Tile illegalEndTile = board.getSpecificTile(2,4);
        // illegal move
        board.movePiece(legalStartTile,illegalEndTile,"");
        // legal move
        Tile startTile = board.getSpecificTile(7,1);
        Tile endTile = board.getSpecificTile(5,0);
        String promotionLetter = "";

        assertTrue(startTile.getPiece() instanceof Knight);
        assertNull(endTile.getPiece());

        board.movePiece(startTile,endTile, promotionLetter);

        assertNull(startTile.getPiece());
        assertTrue(endTile.getPiece() instanceof Knight);
    }

    /**
     * Test Method to see if our copy Constructor is working
     */
    @Test
    public void cloneBoardTest(){
        Board original = new Board();

        MoveHistory moveHistory = new MoveHistory();
        original.setMoveHistory(moveHistory);

        Tile startTile = original.getSpecificTile(7,1);
        Tile endTile = original.getSpecificTile(5,0);

        original.movePiece(startTile, endTile,"");

        Board clone = new Board(original);

        Tile cloneEndTile = clone.getSpecificTile(5,0);

        assertSame(cloneEndTile.getPiece() instanceof Knight, endTile.getPiece() instanceof Knight);
        assertEquals(cloneEndTile.getPiece().moveCount, endTile.getPiece().moveCount);
        assertSame(cloneEndTile.getPiece().getColor(), endTile.getPiece().getColor());


    }

    /**
     * test for PawnPromotion 1st Part
     */
    @Test
    public void promotionTest1(){

        Game game = new Game();

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        Piece pawn1;
        Piece pawn2;
        Piece pawn3;


        //set board with pawns
        board[6][0].getValue().setPiece(new Pawn(ChessColor.Black));
        board[1][4].getValue().setPiece(new Pawn(ChessColor.White));


        //set board with pawns we want to promote
        board[6][0].getValue().setPiece(pawn1 = new Pawn(ChessColor.Black));
        board[1][4].getValue().setPiece(pawn2 = new Pawn(ChessColor.White));
        board[6][7].getValue().setPiece(pawn3 = new Pawn(ChessColor.Black));


        pawn1.increaseMoveCount();
        pawn2.increaseMoveCount();
        pawn3.increaseMoveCount();

        Board b = game.getBoard();

        b.setBoardTiles(board);

        //for pawn1
        Tile startTile1 = b.getSpecificTile(6,0);
        Tile endTile1 = b.getSpecificTile(7,0);
        String promotionLetter1 = "";

        game.getBoard().movePiece(startTile1,endTile1, promotionLetter1);

        assertTrue(endTile1.getPiece() instanceof Queen);

        //for pawn2
        Tile startTile2 = b.getSpecificTile(1,4);
        Tile endTile2 = b.getSpecificTile(0,4);
        String promotionLetter2 = "B";

        game.getBoard().movePiece(startTile2,endTile2, promotionLetter2);

        assertTrue(endTile2.getPiece() instanceof Bishop);

        //for pawn3
        Tile startTile3 = b.getSpecificTile(6,7);
        Tile endTile3 = b.getSpecificTile(7,7);
        String promotionLetter3 = "N";

        game.getBoard().movePiece(startTile3,endTile3, promotionLetter3);

        assertTrue(endTile3.getPiece() instanceof Knight);

    }

    /**
     * test for PawnPromotion 2nd Part
     */
    @Test
    public void promotionTest2(){
        Game game = new Game();

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();
        Piece pawn4;
        Piece pawn5;
        Piece pawn6;

        board[1][0].getValue().setPiece(pawn4 = new Pawn(ChessColor.White));
        board[1][7].getValue().setPiece(pawn5 = new Pawn(ChessColor.White));
        board[6][4].getValue().setPiece(pawn6 = new Pawn(ChessColor.Black));

        pawn4.increaseMoveCount();
        pawn5.increaseMoveCount();
        pawn6.increaseMoveCount();

        Board b = game.getBoard();

        b.setBoardTiles(board);

        //for pawn4
        Tile startTile4 = b.getSpecificTile(1,0);
        Tile endTile4 = b.getSpecificTile(0,0);
        String promotionLetter4 = "R";

        game.getBoard().movePiece(startTile4,endTile4, promotionLetter4);

        assertTrue(endTile4.getPiece() instanceof Rook);

        //for pawn5
        Tile startTile5 = b.getSpecificTile(1,7);
        Tile endTile5 = b.getSpecificTile(0,7);
        String promotionLetter5 = "Q";


        game.getBoard().movePiece(startTile5,endTile5, promotionLetter5);

        assertTrue(endTile5.getPiece() instanceof Queen);

        //for pawn5
        Tile startTile6 = b.getSpecificTile(6,4);
        Tile endTile6 = b.getSpecificTile(7,4);
        String promotionLetter6 = "C";


        game.getBoard().movePiece(startTile6,endTile6, promotionLetter6);

        assertNull(endTile6.getPiece());
    }

    /**
     * testing a piece beating another situation
     */
    @Test
    public void beatPieceTest(){
        Game game = new Game();
        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();
        // set the Pieces we need
        board[0][4].getValue().setPiece(new King(ChessColor.Black));
        board[0][0].getValue().setPiece(new Rook(ChessColor.Black));
        board[0][1].getValue().setPiece(new Rook(ChessColor.Black));

        board[1][0].getValue().setPiece(new Pawn(ChessColor.White));
        board[7][1].getValue().setPiece(new Rook(ChessColor.White));
        board[7][4].getValue().setPiece(new King(ChessColor.White));


        Board b = game.getBoard();

        // set this predefined custom board
        b.setBoardTiles(board);

        // white Rook beats black Rook2, so removed pieces list should be size 1
        Tile startTile1 = b.getSpecificTile(7,1);
        Tile endTile1 = b.getSpecificTile(0,1);

        // black Rook1 beats white Pawn but black is in check so move should be reset, so removed pieces list should be size 1 again
        Tile startTile2 =b.getSpecificTile(0,0);
        Tile endTile2 = b.getSpecificTile(1,0);

        String hsString = "";

        b.movePiece(startTile1,endTile1,hsString);
        // the move should happen and the beaten Rook should have been added to the removedPiecesList.
        assertTrue(endTile1.getPiece().moveCount > 0 && endTile1.getPiece() instanceof Rook);
        assertEquals(1,b.getRemovedPieces().size());

        b.movePiece(startTile2,endTile2,hsString);
        // move should reset and beaten Pawn should be removed out of removedPiecesList.
        assertTrue(endTile2.getPiece() instanceof Pawn && startTile2.getPiece() instanceof Rook);
        assertEquals(1,b.getRemovedPieces().size());


    }

}
