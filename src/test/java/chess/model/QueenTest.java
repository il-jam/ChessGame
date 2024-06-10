package chess.model;

import chess.TestHelper;
import chess.model.pieces.Piece;
import chess.model.pieces.Queen;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class contains the test for the bishop class
 */
public class QueenTest {
    TestHelper testHelper = new TestHelper();
    /**
     * Test method for possibleTilesForBlackQueenTest
     */
    @Test
    public void possibleTilesForBlackQueenTest() {
        Game game = new Game();

        Piece queen;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //set board with bishops
        board[0][3].getValue().setPiece(queen = new Queen(ChessColor.Black));

        Board b = game.getBoard();

        b.setBoardTiles(board);

        Position  pos = new Position(0,3);
        List<Tile> possibleTiles = queen.possibleTiles(b, pos);

        LinkedList<Tile> testCollection = new LinkedList<>();
        //horizontal
        testCollection.add(b.getSpecificTile(0, 0));
        testCollection.add(b.getSpecificTile(0, 1));
        testCollection.add(b.getSpecificTile(0, 2));
        testCollection.add(b.getSpecificTile(0, 4));
        testCollection.add(b.getSpecificTile(0, 5));
        testCollection.add(b.getSpecificTile(0, 6));
        testCollection.add(b.getSpecificTile(0, 7));
        //vertical
        testCollection.add(b.getSpecificTile(1, 3));
        testCollection.add(b.getSpecificTile(2, 3));
        testCollection.add(b.getSpecificTile(3, 3));
        testCollection.add(b.getSpecificTile(4, 3));
        testCollection.add(b.getSpecificTile(5, 3));
        testCollection.add(b.getSpecificTile(6, 3));
        testCollection.add(b.getSpecificTile(7, 3));
        //diagonal
        testCollection.add(b.getSpecificTile(1, 2));
        testCollection.add(b.getSpecificTile(2, 1));
        testCollection.add(b.getSpecificTile(3, 0));
        testCollection.add(b.getSpecificTile(1, 4));
        testCollection.add(b.getSpecificTile(2, 5));
        testCollection.add(b.getSpecificTile(3, 6));
        testCollection.add(b.getSpecificTile(4, 7));

        assertTrue(possibleTiles.containsAll(testCollection));
        assertEquals(21, possibleTiles.size());
    }

    /**
     * Test method for getConsoleChar
     */
    @Test
    public void getConsoleCharTest(){
        Queen testQueen = new Queen(ChessColor.White);

        //white
        char testCharQueen = testQueen.getConsoleChar(ChessColor.White);
        assertEquals('Q', testCharQueen);

        //black
        testCharQueen = testQueen.getConsoleChar(ChessColor.Black);
        assertEquals('q', testCharQueen);
    }
}