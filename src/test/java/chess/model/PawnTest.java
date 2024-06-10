package chess.model;

import chess.TestHelper;
import chess.model.pieces.*;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the test for the bishop class
 */
public class PawnTest {
    TestHelper testHelper = new TestHelper();
    /**
     * Test method for possibleTilesForBlackPawnWithoutOpponents
     */
    @Test
    public void possibleTilesForBlackPawnWithoutOpponentsTest() {
        Game game = new Game();

        Piece pawn;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();


        //set board with one pawn
        board[1][3].getValue().setPiece(pawn = new Pawn(ChessColor.Black));


        Board b = game.getBoard();

        b.setBoardTiles(board);

        Position  pos = new Position(1,3);
        List<Tile> possibleTiles = pawn.possibleTiles(b, pos);
        assertEquals(2, possibleTiles.size());
        assertTrue(possibleTiles.contains(b.getSpecificTile(2, 3)));
        assertTrue(possibleTiles.contains(b.getSpecificTile(3, 3)));


    }

    /**
     * Test method for possibleTilesForBlackPawnWithOpponents
     */
    @Test
    public void possibleTilesForBlackPawnWithOpponentsTest() {
        Game game = new Game();

        Piece pawn;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();


        //set board with black pawn
        board[1][3].getValue().setPiece(pawn = new Pawn(ChessColor.Black));
        //set board with opponent queen two rows in front of pawn
        board[3][3].getValue().setPiece(new Queen(ChessColor.White));
        //set board with opponent pawn beatable for pawn
        board[2][2].getValue().setPiece(new Pawn(ChessColor.White));


        Board b = game.getBoard();

        b.setBoardTiles(board);

        Position  pos = new Position(1,3);
        List<Tile> possibleTiles = pawn.possibleTiles(b, pos);
        assertEquals(2, possibleTiles.size());
        assertTrue(possibleTiles.contains(b.getSpecificTile(2, 3)));
        assertTrue(possibleTiles.contains(b.getSpecificTile(2, 2)));


    }

    /**
     * Second test method for possibleTilesForBlackPawnWithOpponents
     */
    @Test
    public void possibleTilesForBlackPawnWithOpponentsTest2() {
        Game game = new Game();

        Piece pawn;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();


        //set board with black pawn
        board[1][3].getValue().setPiece(pawn = new Pawn(ChessColor.Black));
        //set board with opponent queen one rows in front of pawn
        board[2][3].getValue().setPiece(new Rook(ChessColor.White));
        //set board with opponent queen two rows in front of pawn
        board[3][3].getValue().setPiece(new Queen(ChessColor.White));
        //set board with opponent pawn beatable for pawn
        board[2][2].getValue().setPiece(new Pawn(ChessColor.White));


        Board b = game.getBoard();

        b.setBoardTiles(board);

        Position  pos = new Position(1,3);
        List<Tile> possibleTiles = pawn.possibleTiles(b, pos);
        assertEquals(1, possibleTiles.size());
        assertTrue(possibleTiles.contains(b.getSpecificTile(2, 2)));


    }

    /**
     * Test method for possibleTilesForWhitePawnWithOpponents
     */
    @Test
    public void possibleTilesForWhitePawnWithOpponentsTest() {
        Game game = new Game();

        Piece pawn;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();


        //set board with black pawn
        board[6][7].getValue().setPiece(pawn = new Pawn(ChessColor.White));
        //set board with opponent queen two rows in front of pawn
        board[4][7].getValue().setPiece(new Queen(ChessColor.Black));
        //set board with opponent pawn beatable for pawn
        board[5][6].getValue().setPiece(new Pawn(ChessColor.Black));


        Board b = game.getBoard();

        b.setBoardTiles(board);

        Position  pos = new Position(6,7);
        List<Tile> possibleTiles = pawn.possibleTiles(b, pos);
        assertEquals(2, possibleTiles.size());
        assertTrue(possibleTiles.contains(b.getSpecificTile(5, 7)));
        assertTrue(possibleTiles.contains(b.getSpecificTile(5, 6)));


    }

    /**
     * Test method for isEnPassantMovePossible for left EnPassant
     */
    @Test
    public void isEnPassantMovePossibleLeftTest() {
        Game game = new Game();

        Piece attackingBlackPawnLeft;


        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();


        //set board with black pawn
        board[4][2].getValue().setPiece(attackingBlackPawnLeft = new Pawn(ChessColor.Black));
        //set board with opponent
        board[6][1].getValue().setPiece(new Pawn(ChessColor.White));

        Board b = game.getBoard();

        b.setBoardTiles(board);


        String promotionLetter = "";

        //EnPassant Left

        Tile startBlackLeft = b.getSpecificTile(4, 2);
        Tile endBlackLeft = b.getSpecificTile(5, 1);

        Tile startVictimLeft = b.getSpecificTile(6, 1);
        Tile endVictimLeft = b.getSpecificTile(4, 1);

        attackingBlackPawnLeft.increaseMoveCount();
        b.movePiece(startVictimLeft, endVictimLeft, promotionLetter);

        Position  pos = new Position(4,2);
        List<Tile> possibleTilesL = attackingBlackPawnLeft.possibleTiles(b, pos);
        assertEquals(2, possibleTilesL.size());

        b.movePiece(startBlackLeft, endBlackLeft, promotionLetter);
        assertNull(b.getSpecificTile(4, 1).getPiece());

    }
    /**
     * Test method for isEnPassantMovePossible for left EnPassant
     */
    @Test
    public void isEnPassantMovePossibleRightTest() {
        Game game = new Game();
        Piece attackingBlackPawnRight;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        board[4][2].getValue().setPiece(attackingBlackPawnRight = new Pawn(ChessColor.Black));

        board[6][3].getValue().setPiece(new Pawn(ChessColor.White));

        Board b = game.getBoard();

        b.setBoardTiles(board);

        String promotionLetter = "";

        Tile startBlackRight = b.getSpecificTile(4, 2);
        Tile endBlackRight = b.getSpecificTile(5, 3);

        Tile startVictimRight = b.getSpecificTile(6, 3);
        Tile endVictimRight = b.getSpecificTile(4, 3);

        attackingBlackPawnRight.increaseMoveCount();
        b.movePiece(startVictimRight, endVictimRight, promotionLetter);

        Position  pos = new Position(4,2);
        List<Tile> possibleTilesR = attackingBlackPawnRight.possibleTiles(b, pos);
        assertEquals(2, possibleTilesR.size());

        b.movePiece(startBlackRight, endBlackRight, promotionLetter);
        assertNull(b.getSpecificTile(4, 3).getPiece());

    }

    /**
     * Test method for movePawn
     */
    @Test
    public void movePawn() {
        Board board = new Board();
        MoveHistory moveHistory = new MoveHistory();
        board.setMoveHistory(moveHistory);
        Tile startTile = board.getSpecificTile(6, 0);
        Tile endTile = board.getSpecificTile(5, 0);
        String promotionLetter = "";
        board.movePiece(startTile, endTile, promotionLetter);
        assertNull(startTile.getPiece());
    }

    /**
     * test method for a normal move, where enPassant could be possible if wrong implemented
     */
    @Test
    public void notEnPassantTest() {
        Game game = new Game();

        Piece king;
        Piece king2;
        Piece pawnB;
        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        // white Pawns
        board[4][0].getValue().setPiece(new Pawn(ChessColor.White));
        board[6][1].getValue().setPiece(new Pawn(ChessColor.White));

        // black Pawns
        board[2][1].getValue().setPiece(pawnB = new Pawn(ChessColor.Black));
        board[2][0].getValue().setPiece(new Pawn(ChessColor.Black));

        // Kings
        board[0][7].getValue().setPiece(king = new King(ChessColor.White));
        board[7][6].getValue().setPiece(king2 = new King(ChessColor.Black));

        Board b = game.getBoard();

        b.setBoardTiles(board);


        Tile startTileKing = b.getSpecificTile(0, 7);
        Tile endTileKing = b.getSpecificTile(0, 6);

        Tile startTile2 = b.getSpecificTile(2, 1);
        Tile endTile2 = b.getSpecificTile(3, 1);

        Tile startTile3 = b.getSpecificTile(4, 0);
        Tile endTile3 = b.getSpecificTile(3, 0);

        Tile startTile4 = b.getSpecificTile(3, 1);
        Tile endTile4 = b.getSpecificTile(4, 1);

        String s = "";

        king.increaseMoveCount();
        king2.increaseMoveCount();
        b.movePiece(startTileKing, endTileKing, s);

        b.movePiece(startTile2, endTile2, s);

        b.movePiece(startTile3, endTile3, s);

        Position  pos = new Position(3,1);
        List<Tile> possibleTiles = pawnB.possibleTiles(b, pos);

        assertTrue(possibleTiles.size() == 2 && possibleTiles.contains(endTile4));

        b.movePiece(startTile4, endTile4, s);

        assertTrue(endTile4.getPiece() instanceof Pawn);

    }

    /**
     * test for enPassant movement and removing the beaten piece
     */
    @Test
    public void EnPassantTest() {
        Game game = new Game();


        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        // white Pawns
        board[5][0].getValue().setPiece(new Pawn(ChessColor.White));
        board[6][1].getValue().setPiece(new Pawn(ChessColor.White));

        // black Pawns
        board[2][1].getValue().setPiece(new Pawn(ChessColor.Black));

        // Kings
        board[7][4].getValue().setPiece(new King(ChessColor.White));
        board[0][4].getValue().setPiece(new King(ChessColor.Black));

        Board b = game.getBoard();

        b.setBoardTiles(board);


        Tile startTileKing = b.getSpecificTile(7, 4);
        Tile endTileKing = b.getSpecificTile(7, 5);

        Tile startTile2 = b.getSpecificTile(2, 1);
        Tile endTile2 = b.getSpecificTile(3, 1);

        Tile startTile3 = b.getSpecificTile(5, 0);
        Tile endTile3 = b.getSpecificTile(3, 0);

        Tile startTile4 = b.getSpecificTile(3, 1);
        Tile endTile4 = b.getSpecificTile(4, 0);

        String s = "";
        b.movePiece(startTileKing, endTileKing, s);


        b.movePiece(startTile2, endTile2, s);


        b.movePiece(startTile3, endTile3, s);


        b.movePiece(startTile4, endTile4, s);


        assertTrue(endTile4.getPiece() instanceof Pawn && endTile4.getPiece().getColor() == ChessColor.Black);
    }

    /**
     * Test method for getConsoleCharTest
     */
    @Test
    public void getConsoleCharTest() {
        Pawn testPawn = new Pawn(ChessColor.White);

        //white
        char testCharPawn = testPawn.getConsoleChar(ChessColor.White);
        assertEquals('P', testCharPawn);

        //black
        testCharPawn = testPawn.getConsoleChar(ChessColor.Black);
        assertEquals('p', testCharPawn);
    }


}