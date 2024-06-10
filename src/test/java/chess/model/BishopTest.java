package chess.model;

import chess.TestHelper;
import chess.model.pieces.Bishop;
import chess.model.pieces.Piece;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class contains the test for the bishop class
 */
public class BishopTest {
    TestHelper testHelper = new TestHelper();
    /**
     * Test method for possibleTilesForLeftBlackBishop
     */
    @Test
    public void possibleTilesForLeftBlackBishopTest(){
        Game game = new Game();

        Piece bishop;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //set board with bishops
        board[0][2].getValue().setPiece(bishop = new Bishop(ChessColor.Black));


        Board b = game.getBoard();

        b.setBoardTiles(board);

        Position pos = new Position(0,2);

        List<Tile> possibleTiles = bishop.possibleTiles(b, pos);

        Collection<Tile> testCollection = new LinkedList<>();
        testCollection.add(b.getSpecificTile(1,3));
        testCollection.add(b.getSpecificTile(2,4));
        testCollection.add(b.getSpecificTile(3,5));
        testCollection.add(b.getSpecificTile(4,6));
        testCollection.add(b.getSpecificTile(5,7));
        testCollection.add(b.getSpecificTile(1,1));
        testCollection.add(b.getSpecificTile(2,0));
        assertTrue(possibleTiles.containsAll(testCollection));
        assertEquals(7, possibleTiles.size());

    }

    /**
     * Test method for possibleTilesForRightBlackBishop
     */
    @Test
    public void possibleTilesForRightBlackBishopTest(){
        Game game = new Game();

        Piece bishop;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //set board with bishops
        board[0][5].getValue().setPiece(bishop = new Bishop(ChessColor.Black));

        Board b = game.getBoard();

        b.setBoardTiles(board);

        Position pos = new Position(0,5);

        List<Tile> possibleTiles = bishop.possibleTiles(b, pos);

        Collection<Tile> testCollection = new LinkedList<>();
        testCollection.add(b.getSpecificTile(1,6));
        testCollection.add(b.getSpecificTile(2,7));
        testCollection.add(b.getSpecificTile(1,4));
        testCollection.add(b.getSpecificTile(2,3));
        testCollection.add(b.getSpecificTile(3,2));
        testCollection.add(b.getSpecificTile(4,1));
        testCollection.add(b.getSpecificTile(5,0));
        assertTrue(possibleTiles.containsAll(testCollection));
        assertEquals(7, possibleTiles.size());

    }

    /**
     * Test method for possibleTilesForLeftWhiteBishop
     */
    @Test
    public void possibleTilesForLeftWhiteBishopTest(){
        Game game = new Game();

        Piece bishop;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //set board with bishops
        board[7][2].getValue().setPiece(bishop = new Bishop(ChessColor.White));


        Board b = game.getBoard();

        b.setBoardTiles(board);

        Position pos = new Position(7,2);

        List<Tile> possibleTiles = bishop.possibleTiles(b, pos);

        LinkedList<Tile> testCollection = new LinkedList<>();
        testCollection.add(b.getSpecificTile(6,1));
        testCollection.add(b.getSpecificTile(5,0));
        testCollection.add(b.getSpecificTile(6,3));
        testCollection.add(b.getSpecificTile(5,4));
        testCollection.add(b.getSpecificTile(4,5));
        testCollection.add(b.getSpecificTile(3,6));
        testCollection.add(b.getSpecificTile(2,7));
        assertTrue(possibleTiles.containsAll(testCollection));
        assertEquals(7, possibleTiles.size());

    }

    /**
     * Test method for getConsoleChar
     */
    @Test
    public void getConsoleCharTest(){
        Bishop testBishop = new Bishop(ChessColor.White);

        //white
        char testCharBishop = testBishop.getConsoleChar(ChessColor.White);
        assertEquals('B', testCharBishop);

        //black
        testCharBishop = testBishop.getConsoleChar(ChessColor.Black);
        assertEquals('b', testCharBishop);
    }

}
