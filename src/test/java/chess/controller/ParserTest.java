package chess.controller;

import chess.model.Game;
import chess.model.Tile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Parser class
 */
public class ParserTest {

    String testPositionStringA = "a2";
    String testPositionStringB = "b2";
    String testPositionStringC = "c2";
    String testPositionStringD = "d2";
    String testPositionStringE = "e2";
    String testPositionStringF = "f2";
    String testPositionStringG = "g2";
    String testPositionStringH = "h2";
    /**
     * Test method for parsInput method
     */
    @Test
    public void parseInputTest(){
        Game game = new Game();
        ClIHumanController cliController = new ClIHumanController(game);
        Parser parser = new Parser(game);

        String testInput = "a2-a3";
        List<Object> testTiles;

        testTiles = parser.parseInput(testInput);

        Tile testStartTile = (Tile) testTiles.get(0);
        Tile testEndTile = (Tile) testTiles.get(1);

        assertEquals(testStartTile , cliController.getGame().getBoard().getSpecificTile(6,0));
        assertEquals(testEndTile , cliController.getGame().getBoard().getSpecificTile( 5,0));
    }

    /**
     * Test method for charConversion() 1st Part
     */
    @Test
    public void charConversionTestAAndB1(){
        Game game = new Game();
        Parser parser = new Parser(game);

        int[] testPositionIndexA = parser.charConversion(testPositionStringA);

        int testRowIndexA = testPositionIndexA[0];
        int testColIndexA = testPositionIndexA[1];

        assertEquals(testRowIndexA, 6);
        assertEquals(testColIndexA,0);

        int[] testPositionIndexB = parser.charConversion(testPositionStringB);

        int testColIndexB = testPositionIndexB[1];
        assertEquals(testColIndexB,1);

        int[] testPositionIndexC = parser.charConversion(testPositionStringC);

        int testColIndexC = testPositionIndexC[1];
        assertEquals(testColIndexC,2);

        int[] testPositionIndexD = parser.charConversion(testPositionStringD);

        int testColIndexD = testPositionIndexD[1];
        assertEquals(testColIndexD,3);

    }

    /**
     * Test method for charConversion() 2nd Part
     */
    @Test
    public void charConversionTestAAndB2(){
        Game game = new Game();
        Parser parser = new Parser(game);

        int[] testPositionIndexE = parser.charConversion(testPositionStringE);

        int testColIndexE = testPositionIndexE[1];
        assertEquals(testColIndexE,4);

        int[] testPositionIndexF = parser.charConversion(testPositionStringF);

        int testColIndexF = testPositionIndexF[1];
        assertEquals(testColIndexF,5);

        int[] testPositionIndexG = parser.charConversion(testPositionStringG);

        int testColIndexG = testPositionIndexG[1];
        assertEquals(testColIndexG,6);

        int[] testPositionIndexH = parser.charConversion(testPositionStringH);

        int testColIndexH = testPositionIndexH[1];
        assertEquals(testColIndexH,7);
    }
}

