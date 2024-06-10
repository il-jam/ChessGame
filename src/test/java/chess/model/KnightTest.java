package chess.model;
import chess.TestHelper;
import chess.model.pieces.Knight;
import chess.model.pieces.Piece;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class contains the test for the bishop class
 */
public class KnightTest {
    TestHelper testHelper = new TestHelper();
    /**
     * Test method for possibleTilesForLeftBlackKnight
     */
    @Test
    public void possibleTilesForLeftBlackKnightTest() {
        Game game = new Game();

        Piece knight;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //set board with knight
        board[0][1].getValue().setPiece(knight = new Knight(ChessColor.Black));

        Board b = game.getBoard();

        b.setBoardTiles(board);

        Position  pos = new Position(0,1);
        List<Tile> possibleTiles = knight.possibleTiles(b, pos);
        assertEquals(3, possibleTiles.size());

        assertTrue(possibleTiles.contains(b.getSpecificTile(2, 0)));
        assertTrue(possibleTiles.contains(b.getSpecificTile(2, 2)));
        assertTrue(possibleTiles.contains(b.getSpecificTile(1, 3)));

    }

    /**
     * Test method for getConsoleChar
     */
    @Test
    public void getConsoleCharTest(){
        Knight testKnight = new Knight(ChessColor.White);

        //white
        char testCharKnight = testKnight.getConsoleChar(ChessColor.White);
        assertEquals('N', testCharKnight);

        //black
        testCharKnight = testKnight.getConsoleChar(ChessColor.Black);
        assertEquals('n', testCharKnight);
    }
}