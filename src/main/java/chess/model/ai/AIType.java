package chess.model.ai;

import chess.model.*;

/**
 * Abstract CLass of the AIType (simple/ minmax)
 */
public abstract class AIType {

    public Board board;

    public ChessColor aIColor;

    public boolean black;


    /**
     * Constructor of AIType Class
     * @param board   current Board
     * @param aIColor Color of AI
     * @param isBlack boolean to routine the player color of the Moves the AI calculates
     */
    public AIType(Board board, ChessColor aIColor, boolean isBlack) {
        this.board = board;
        this.aIColor = aIColor;
        this.black = isBlack;
    }

    /**
     * abstract method for making a AIMove
     */
    public abstract void makeAIMove();

    void useTilesToMovePiece(AIMove aiMove){
        Tile startTileAI = aiMove.getStartTile();
        Tile endTileAI = aiMove.getEndTile();
        Position startPosition = new Position(startTileAI.getRow(),startTileAI.getColumn());
        Position endPosition = new Position(endTileAI.getRow(),endTileAI.getColumn());
        Tile startTile = board.getSpecificTile(startPosition.getI(), startPosition.getJ());
        Tile endTile = board.getSpecificTile(endPosition.getI(), endPosition.getJ());
        board.movePiece(startTile,endTile,"");
    }
}

