package chess.model.ai;

import chess.model.*;

/**
 * Class of the SimpleAI
 */
public class SimpleAI extends AIType{


    private String aiMoveString;
    /**
     * Constructor of SimpleAI Class
     * @param board current Board
     * @param aIColor Color of AI
     * @param isBlack boolean to routine the player color of the Moves the AI calculates
     */
    public SimpleAI(Board board, ChessColor aIColor, boolean isBlack) {
        super(board, aIColor, isBlack);
    }

    /**
     * Method which generates a calculated AI move
     */
    @Override
    public void makeAIMove() {
        Board clone = new Board(board);
        MinMaxCalc minMaxCalc = new MinMaxCalc(clone,black,null, aIColor);
        AIMove aiMove = minMaxCalc.bestValuedMove(black,0);
        useTilesToMovePiece(aiMove);
        aiMoveString = aiMove.convertToInputString();
        System.out.println();
        System.out.println("SimpleAI move was !" + aiMoveString);
    }
}
