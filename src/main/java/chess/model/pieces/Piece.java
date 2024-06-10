package chess.model.pieces;

import chess.model.*;

import java.util.ArrayList;
import java.util.List;

import static chess.model.Constants.MOVE_LIMIT_ALL;
import static chess.model.Constants.MOVE_LIMIT_ONE;

/**
 * This abstract class contains information about the chess piece
 */
public abstract class Piece {


    /**
     * The color of a piece
     */
    private final ChessColor color;

    public int moveCount;

    /**
     * This is the constructor for the chess pieces
     * @param color Significant color of a selected chess piece
     */
    public Piece(ChessColor color){
        this.color = color;
        this.moveCount = 0;
    }

    /**
     * This is the copy-constructor for the piece object
     * @param toCopy the Piece we want to copy
     */
    public Piece(Piece toCopy){
        color = toCopy.color;
        moveCount = toCopy.moveCount;
    }

    /**
     * method which gets us a generic version of a possibleTiles list
     * @param board current Board
     * @param myPosition Position of Piece
     * @param pieceOffset movement offset
     * @param type Piece type limitations
     * @return list of possible Tiles
     */
    @SuppressWarnings({"PMD.CyclomaticComplexity"})
    /*
        with this method we saving extra movement implementation in every extended piece class beside pawn.
     */
    public List<Tile> getGenericPossibleTiles(Board board, Position myPosition, int[][] pieceOffset, PieceMoveType type)
    {
        List<Tile> possibleTiles = new ArrayList<>();
        int moveLimit;
        Tile currentTile = board.getSpecificTile(myPosition.getI(), myPosition.getJ());

        if(type.equals(PieceMoveType.SHORT)){
            moveLimit = MOVE_LIMIT_ONE;
        }
        else{
            moveLimit = MOVE_LIMIT_ALL;
        }

        //first loop iterates through the offset arrays
        for(int[] o : pieceOffset){

            Tile candidateTile = currentTile;

            //second loop iterates through the length the specific piece object can move on the board
            for(int i = 1; i < moveLimit; i++){

                int plusRow = o[0];
                int plusCol = o[1];

                // checks if the candidate tile is still a legal tile after adding/subtracting the offset plus
                if (!(candidateTile.getRow()+plusRow < 0
                        || candidateTile.getColumn()+plusCol < 0
                        || candidateTile.getRow()+plusRow > 7
                        || candidateTile.getColumn() + plusCol > 7 )) {
                    candidateTile = board.getSpecificTile(candidateTile.getRow() + plusRow, candidateTile.getColumn() + plusCol);

                    if (candidateTile == null) break; //out of bounds

                    if(addCandidateTile(board,plusCol, candidateTile,currentTile, possibleTiles) )
                        break;
                }
            }
        }
        return possibleTiles;
    }

    /**
     * method which checks if a tile is possible, and then adds that tile to the possibleTile list
     * @param board current board
     * @param plusCol column we need for the pawn to see if he goes forward or diagonal
     * @param candidateTile the tile we inspect
     * @param currentTile tile with the Piece we want to move on it
     * @param possibleTiles list of possible positions
     * @return true if tile is added else its false
     */
    @SuppressWarnings("PMD.ExcessiveParameterList")
    // unfortunately we need all the Parameters of this method because we also call it in Pawn class, where they are crucial.
    protected boolean addCandidateTile(Board board, int plusCol, Tile candidateTile, Tile currentTile, List<Tile> possibleTiles)
    {
        Piece other = candidateTile.getPiece();

        if (other == null) { //no other piece?
            possibleTiles.add(candidateTile);
        } else {
            if (other.getColor() != currentTile.getPiece().getColor()) {
                possibleTiles.add(candidateTile);
            }
            return true; // cant go further in this direction
        }
        return false;
    }

    /**
     * @param board missing
     * @param myPosition Position of Piece
     * @return returns an array of tiles that a chess piece can move to at any time
     */
    public abstract List<Tile> possibleTiles(Board board, Position myPosition);

    /**
     * method to increase the MoveCount
     */
    public void increaseMoveCount(){
        this.moveCount++;
    }

    /**
     * method to decrease the MoveCount
     */
    public void decreaseMoveCount(){
        this.moveCount--;
    }

    /**
     * This method gives back a char for the piece its called on
     * @param color the chess color
     * @return the char
     */
    public char getConsoleChar(ChessColor color){
        return 'U';
    }

    public ChessColor getColor() {
        return color;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public int getMoveCount() {
        return moveCount;
    }
}
