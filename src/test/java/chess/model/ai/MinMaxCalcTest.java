package chess.model.ai;

import chess.model.Board;
import chess.model.ChessColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for MinMaxCalc Class
 */
public class MinMaxCalcTest {

    /**
     * test method for calcBoardValue()
     */
    @Test
    public void calcBoardValueTest(){
        Board board = new Board();
        MinMaxCalc minMaxCalc = new MinMaxCalc(board,true, null, ChessColor.Black);
        assertEquals(20, minMaxCalc.getAIMoves(board).size());
    }
}