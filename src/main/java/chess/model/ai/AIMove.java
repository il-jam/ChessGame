package chess.model.ai;

import chess.model.Tile;

/**
 * AIMove Class which represents a Move the AI needs to compute the best AIMove
 */
public class AIMove {

    private final Tile startTile;

    private final Tile endTile;

    /**
     * Constructor of AIMove Class
     * @param startTile start Tile of the AIMove
     * @param endTile end Tile of the AIMove
     */
    public AIMove(Tile startTile, Tile endTile){
        this.startTile = startTile;
        this.endTile = endTile;
    }

    /**
     * Method to convert the AIMove into a String
     * @return the converted String
     */
    public String convertToInputString(){

        String startTileRow = Character.toString(startTile.getRowChar());
        String startTileColumn = Character.toString(startTile.getColumnChar());
        String endTileRow = Character.toString(endTile.getRowChar());
        String endTileColumn = Character.toString(endTile.getColumnChar());

        return  startTileColumn + startTileRow + "-" + endTileColumn + endTileRow;
    }

    public Tile getStartTile() {
        return startTile;
    }

    public Tile getEndTile() {
        return endTile;
    }
}
