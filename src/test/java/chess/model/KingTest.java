package chess.model;

import chess.TestHelper;
import chess.model.pieces.Bishop;
import chess.model.pieces.King;
import chess.model.pieces.Piece;
import chess.model.pieces.Rook;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the test for the bishop class
 */
public class KingTest {
    TestHelper testHelper = new TestHelper();
    /**
     * Test method for possibleTilesForBlackKing
     */
    @Test
    public void possibleTilesForBlackKingTest() {
        Game game = new Game();

        Piece king;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //set board with bishops
        board[0][4].getValue().setPiece(king = new King(ChessColor.Black));

        Board b = game.getBoard();

        b.setBoardTiles(board);

        Position pos = new Position(0,4);
        List<Tile> possibleTiles = king.possibleTiles(b, pos);

        LinkedList<Tile> testCollection = new LinkedList<>();
        testCollection.add(b.getSpecificTile(0, 3));
        testCollection.add(b.getSpecificTile(0, 5));
        testCollection.add(b.getSpecificTile(1, 3));
        testCollection.add(b.getSpecificTile(1, 5));
        testCollection.add(b.getSpecificTile(1, 4));
        assertTrue(possibleTiles.containsAll(testCollection));
        assertEquals(5, possibleTiles.size());

    }


    /**
     * Test Method for OnlyLongCastling
     */
    @Test
    public void longCastlingTest(){
        Game game = new Game();

        Piece king;
        Piece king2;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        board[0][4].getValue().setPiece(king = new King(ChessColor.Black));
        board[0][0].getValue().setPiece(new Rook(ChessColor.Black));
        board[0][6].getValue().setPiece(new Rook(ChessColor.Black));
        board[7][4].getValue().setPiece(king2 = new King(ChessColor.White));
        board[7][0].getValue().setPiece(new Rook(ChessColor.White));
        board[7][1].getValue().setPiece(new Bishop(ChessColor.White));


        Board b = game.getBoard();
        b.setBoardTiles(board);

        Tile startTile = b.getSpecificTile(0,4);
        Tile endTile = b.getSpecificTile(0,2);


        Position pos = new Position(0,4);
        Position notLongCastlingPos = new Position(7,4);
        List<Tile> possTiles = king.possibleTiles(b,pos);
        List<Tile> possTileIsNotLongCastling = king2.possibleTiles(b,notLongCastlingPos);
        assertEquals(6,possTiles.size());
        assertEquals(5,possTileIsNotLongCastling.size());

        b.movePiece(startTile,endTile,"");
        assertSame(endTile.getPiece(), king);
        assertTrue(b.getSpecificTile(0,3).getPiece() instanceof Rook);

    }

    /**
     * Test method for OnlyShortCastling
     */
    @Test
    public void shortCastlingTest(){
        Game game = new Game();

        Piece king;
        Piece king2;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        board[0][4].getValue().setPiece(king = new King(ChessColor.Black));
        board[0][7].getValue().setPiece(new Rook(ChessColor.Black));
        board[0][2].getValue().setPiece(new Rook(ChessColor.Black));
        board[7][4].getValue().setPiece(king2 = new King(ChessColor.White));
        board[7][7].getValue().setPiece(new Rook(ChessColor.White));
        board[7][6].getValue().setPiece(new Bishop(ChessColor.White));

        Board b = game.getBoard();
        b.setBoardTiles(board);

        Tile startTile = b.getSpecificTile(0,4);
        Tile endTile = b.getSpecificTile(0,6);
        Position shortCastlingPos = new Position(0,4);
        Position notLongCastlingPos = new Position(7,4);
        List<Tile> possTilesShortCastling = king.possibleTiles(b,shortCastlingPos);
        List<Tile> possTilesIsNotLongCastling = king2.possibleTiles(b,notLongCastlingPos);
        assertEquals(5,possTilesIsNotLongCastling.size());
        assertEquals(6,possTilesShortCastling.size());
        b.movePiece(startTile,endTile,"");
        assertSame(endTile.getPiece(), king);
        assertTrue(b.getSpecificTile(0,5).getPiece() instanceof Rook);

        Position pos = new Position(0,6);
        List<Tile> possTiles = king.possibleTiles(b,pos);
        assertEquals(4,possTiles.size());

    }

    /**
     * testMethod for LongOrShortCastling
     */
    @Test
    public void longAndShortCastlingTest(){
        Game game = new Game();

        Piece king;


        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //set board with bishops
        board[0][4].getValue().setPiece(king = new King(ChessColor.Black));
        board[0][7].getValue().setPiece(new Rook(ChessColor.Black));
        board[0][0].getValue().setPiece(new Rook(ChessColor.Black));

        Board b = game.getBoard();

        b.setBoardTiles(board);
        Position pos = new Position(0,4 );
        List<Tile> possTiles = king.possibleTiles(b,pos);
        assertEquals(7, possTiles.size());
    }

    /**
     * Test method for getConsoleChar
     */
    @Test
    public void getConsoleCharTest(){
        King testKing = new King(ChessColor.White);

        //white
        char testCharKing = testKing.getConsoleChar(ChessColor.White);
        assertEquals('K', testCharKing);

        //black
        testCharKing = testKing.getConsoleChar(ChessColor.Black);
        assertEquals('k', testCharKing);
    }


}