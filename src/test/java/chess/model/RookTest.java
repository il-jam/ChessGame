package chess.model;

import chess.TestHelper;
import chess.model.pieces.Piece;
import chess.model.pieces.Rook;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class contains the test for the bishop class
 */
public class RookTest {
    TestHelper testHelper = new TestHelper();
    /**
     * Test method for possibleTilesForLeftBlackBishop
     */
    @Test
    public void possibleTilesForLeftBlackBishopTest(){
        Game game = new Game();

        Piece rook;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //set board with rook
        board[0][0].getValue().setPiece(rook = new Rook(ChessColor.Black));


        Board b = game.getBoard();

        b.setBoardTiles(board);

        Position  pos = new Position(0, 0);

        List<Tile> possibleTiles = rook.possibleTiles(b, pos);

        LinkedList<Tile> testCollection = new LinkedList<>();
        testCollection.add(b.getSpecificTile(1,0));
        testCollection.add(b.getSpecificTile(2,0));
        testCollection.add(b.getSpecificTile(3,0));
        testCollection.add(b.getSpecificTile(4,0));
        testCollection.add(b.getSpecificTile(5,0));
        testCollection.add(b.getSpecificTile(6,0));
        testCollection.add(b.getSpecificTile(7,0));
        testCollection.add(b.getSpecificTile(0,1));
        testCollection.add(b.getSpecificTile(0,2));
        testCollection.add(b.getSpecificTile(0,3));
        testCollection.add(b.getSpecificTile(0,4));
        testCollection.add(b.getSpecificTile(0,5));
        testCollection.add(b.getSpecificTile(0,6));
        testCollection.add(b.getSpecificTile(0,7));
        assertTrue(possibleTiles.containsAll(testCollection));
        assertEquals(14, possibleTiles.size());

    }

    /**
     * Test method for getConsoleChar
     */
    @Test
    public void getConsoleCharTest(){
        Rook testRook = new Rook(ChessColor.White);

        //white
        char testCharRook = testRook.getConsoleChar(ChessColor.White);
        assertEquals('R', testCharRook);

        //black
        testCharRook = testRook.getConsoleChar(ChessColor.Black);
        assertEquals('r', testCharRook);
    }
}
