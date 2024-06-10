package chess.model.ai;

import chess.model.Board;
import chess.model.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AIMove
 */
public class AIMoveTest {

    /**
     * Test method for convertToInputString()
     */
    @Test
    public void convertToInputStringTest(){
        Board board = new Board();
        Tile startTile = board.getSpecificTile(6,0);
        Tile endTile = board.getSpecificTile(5,0);
        AIMove aiMove = new AIMove(startTile,endTile);
        String testAIMoveString = aiMove.convertToInputString();
        assertEquals(testAIMoveString, "a2-a3");
    }
}