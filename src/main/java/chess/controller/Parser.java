package chess.controller;

import chess.model.Game;
import chess.model.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parser class in the Controller package
 */
public class Parser {

    private final List<Object> tileList;
    public Game game;

    /**
     * Constructor for the Parser class
     * @param game current Game
     */
    public Parser (Game game){
        tileList = new ArrayList<>();
        this.game = game;
    }


    /**
     * Method for parsing the input string and converting it to two Tiles for further game logic
     * @param input the input String which is entered by the user, which got handed over from the ClIHumanController
     * @return An arraylist filled with the to start and endtile
     */
    public List<Object> parseInput(String input){

        tileList.clear();

        String startPos;
        String endPos;
        String promotionLetter = "";

        final Pattern movePattern = Pattern.compile("([a-h][1-8])[-]([a-h][1-8])([QNRB])?");

        if (movePattern.matcher(input).matches()) {
            // splits input
            String[] parts = input.split("-");
            startPos = parts[0];
            endPos = parts[1];

            if(endPos.length() > 2) {

                String[] endPosParts = endPos.split("(?<=\\G..)");
                endPos = endPosParts[0];
                promotionLetter = endPosParts[1];
            }

            int[] startPosition = charConversion(startPos);
            int[] endPosition = charConversion(endPos);

            int rowStart = startPosition[0];
            int colStart = startPosition[1];

            int rowEnd = endPosition[0];
            int colEnd = endPosition[1];

            if (endPos.length() > 2) {
                String[] endPosParts = endPos.split("(?<=\\G..)");
                promotionLetter = endPosParts[1];
            }


            Tile startTile;
            startTile = game.getBoard().getSpecificTile(rowStart, colStart);
            Tile endTile;
            endTile = game.getBoard().getSpecificTile(rowEnd, colEnd);

            tileList.add(startTile);
            tileList.add(endTile);
            tileList.add(promotionLetter);

        }
        return tileList;
    }

    /**
     * Method to convert the string parts from the input position to int values
     * @param position String position from a start or endtile
     * @return A int Array filled with two int values, which resemble the the row index and the column index to later build
     * a tile.
     */
    public int[] charConversion(String position){
        char colChar = position.charAt(0);
        char rowChar = position.charAt(1);

        int[] positionIndex = new int[2];

        int colInt = 0;

        switch (colChar) {
            case 'a':
                colInt = 0;
                break;
            case 'b':
                colInt = 1;
                break;
            case 'c':
                colInt = 2;
                break;
            case 'd':
                colInt = 3;
                break;
            case 'e':
                colInt = 4;
                break;
            case 'f':
                colInt = 5;
                break;
            case 'g':
                colInt = 6;
                break;
            case 'h':
                colInt = 7;
                break;
        }

        /*
        char value : int;
        49 : 1
        50 : 2
        51 : 3
        52 : 4
        53 : 5
        54 : 6
        55 : 7
        56 : 8
        but we need the int value - 1 because we start counting at 0
         */

        int rowInt = 56 - rowChar;

        positionIndex[0] = rowInt;
        positionIndex[1] = colInt;

        return positionIndex;

    }

}
