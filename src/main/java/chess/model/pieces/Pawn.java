package chess.model.pieces;

import chess.model.*;

import java.util.List;

import static chess.model.Constants.*;

/**
 * This class contains the information of the pawn
 */
public class Pawn extends Piece {

    /**
     * This is the constructor for the chess pieces
     * @param color Significant color of a selected chess piece
     */
    public Pawn(ChessColor color) {
        super(color);
    }

    /**
     * Method that creates an array of possible tiles a piece can move to
     * @param board 8x8 array consisting of tiles
     * @param myPosition Position of Pawn
     * @return An array of possibles tiles a piece can move to at any time
     */
    @Override
    public List<Tile> possibleTiles(Board board, Position myPosition) {

        int[][] pawnOffset = getRightPawnOffset(board, myPosition);

        return getGenericPossibleTiles(board,myPosition,pawnOffset, PieceMoveType.SHORT);
    }

    /**
     * This is the method for adding possible tiles for a pawn.
     * @param board current board
     * @param plusCol column we need for the pawn to see if he goes forward or diagonal
     * @param candidateTile the tile we inspect
     * @param currentTile tile with the Piece we want to move on it
     * @param possibleTiles list of possible positions
     * @return true if tile is added else its false
     */
    @Override
    @SuppressWarnings("PMD.ExcessiveParameterList") // just as mentioned in Pawn class we do need all the parameters to compute all the possible Tiles
    protected boolean addCandidateTile(Board board, int plusCol, Tile candidateTile, Tile currentTile, List<Tile> possibleTiles) {
       // return super.AddCandidateTile(plusCol, candidateTile, currentTile, possibleTiles);
        Piece other = candidateTile.getPiece();
        //"plusCol = 0" means pawn goes forward
        if (plusCol == 0) {
            if (other == null) { //no other piece?
                possibleTiles.add(candidateTile);
            }
        } else {
            //here we check if the pawn can beat a piece
            if (other != null && other.getColor() != currentTile.getPiece().getColor()) {
                possibleTiles.add(candidateTile);
            }
            //here we check if en passant is possible
            else if (other == null && isEnPassantMovePossible(board,currentTile, candidateTile)) {
                possibleTiles.add(candidateTile);
            }
        }
        return false;
    }

    /**
     * This method checks if an en passant move is possible
     * @param board current board
     * @param currentTile tile with pawn to move on
     * @param candidateTile the tile the pawn wants to move to
     * @return boolean if EnPassant move is possible
     */
    boolean isEnPassantMovePossible (Board board, Tile currentTile, Tile candidateTile) {

        if (currentTile.getPiece().moveCount == 0) {
            return false;
        }

        ChessColor colorOfAttackingPawn = currentTile.getPiece().getColor();
        Tile nextToPawn;

        if(colorOfAttackingPawn == ChessColor.White){
            nextToPawn = board.getSpecificTile(candidateTile.getRow() + 1, candidateTile.colInt);
        }
        else{
            nextToPawn = board.getSpecificTile(candidateTile.getRow() - 1, candidateTile.colInt);
        }
        //if next to pawn is not moved
        if (nextToPawn.getPiece() != null && nextToPawn.getPiece() == board.getLastMovedPiece()  && colorOfAttackingPawn != board.getLastMovedPiece().getColor() && board.getLastMovedPiece() instanceof Pawn){
                board.setEnPassant(true);
                return true;
        }

        return false;
    }

    /**
     * This method gives back a char for the piece its called on
     * @param color Significant color of a selected chess piece
     * @return Return capital letters for white pieces, and small letter for black pieces
     */
    @Override
    public char getConsoleChar (ChessColor color){
        //eine Abfrage ob weiss oder schwarz?
        if(ChessColor.White == color)
            return 'P';
        return 'p';

    }

    /**
     * This method returns the right offset for the pawn by differentiate after chess color and if a double forward move is possible
     * @param board the chess board
     * @param myPosition Position of Pawn
     * @return int array of offsets
     */
    int[][] getRightPawnOffset(Board board, Position myPosition){
        ChessColor colorOfPawn = this.getColor();
        boolean isDoubleForward = isDoubleForwardMovePossible(board, myPosition, colorOfPawn);

        int[][] theRightOffset = new int[0][];

        if(colorOfPawn==ChessColor.White && isDoubleForward){

            theRightOffset = WHITE_PAWN_DOUBLE;
        }
        else if(colorOfPawn == ChessColor.White){
            theRightOffset = WHITE_PAWN;
        }
        else if(colorOfPawn==ChessColor.Black && isDoubleForward){

            theRightOffset = BLACK_PAWN_DOUBLE;
        }
        else if(colorOfPawn == ChessColor.Black){
            theRightOffset = BLACK_PAWN;
        }
        return theRightOffset;
    }

    /**
     * This method checks if double forward move is possible
     * @param board the chess board
     * @param myPosition Position of pawn
     * @param color the color of the pawn
     * @return bool
     */
    boolean isDoubleForwardMovePossible(Board board, Position myPosition, ChessColor color){
        int myRow = myPosition.getI();
        int myCol = myPosition.getJ();
        if(moveCount == 0){
            if(color==ChessColor.White) {
                Tile tileInFrontOfWhitePawn = board.getSpecificTile(myRow - 1, myCol);
                Tile tileInFrontOfWhitePawn2 = board.getSpecificTile(myRow - 2, myCol);

                return !tileInFrontOfWhitePawn.isOccupied() && !tileInFrontOfWhitePawn2.isOccupied();
            }
            else {
                Tile tileInFrontOfBlackPawn = board.getSpecificTile(myRow + 1, myCol);
                Tile tileInFrontOfBlackPawn2 = board.getSpecificTile(myRow + 2, myCol);

                return !tileInFrontOfBlackPawn.isOccupied() && !tileInFrontOfBlackPawn2.isOccupied();
            }
        }
        return false;
    }
}
