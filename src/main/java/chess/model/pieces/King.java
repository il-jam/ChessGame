package chess.model.pieces;

import chess.model.*;

import java.util.ArrayList;
import java.util.List;

import static chess.model.Constants.*;

/**
 * This class contains the information of the King
 */
public class King extends Piece {

    boolean isRowBetweenKIngAndLongRookNotOccupied = false;

    boolean isRowBetweenKIngAndShortRookNotOccupied = false;
    /**
     * This is the constructor for the chess pieces
     * @param color Significant color of a selected chess piece
     */
    public King(ChessColor color) {
        super(color);
    }


    /**
     * Method that creates an array of possible tiles a piece can move to
     * @param board 8x8 array consisting of tiles
     * @param myPosition Position of King
     * @return An array of possibles tiles a piece can move to at any time
     */
    @Override
    public List<Tile> possibleTiles(Board board, Position myPosition) {

        int[][] kingOffset = getRightKingOffset(board, myPosition);


        return getGenericPossibleTiles(board, myPosition, kingOffset, PieceMoveType.SHORT);
    }

    /**
     * Method to get the right King offset for different King movement
     * @param board 8x8 array consisting of tiles
     * @param myPosition Position of King
     * @return two dimensional int Array for the offset
     */

    int[][] getRightKingOffset(Board board, Position myPosition){

        Piece king = board.getSpecificTile(myPosition.getI(), myPosition.getJ()).getPiece();

        int[][] theRightKingOffset;

        if(king.moveCount > 0){
            // if moved only normal King Movement
             theRightKingOffset = KING_AND_QUEEN;
        }
        // if never moved before

        /*
        if Tile between King and long Rook are empty, but between King and short Rook is at least one Piece.
        King can only do King moves and long castling
         */
        else if (isOnlyLongCastling( board, myPosition)){
            theRightKingOffset = KING_LONG;
        }
        /*
         if Tile between King and short Rook are empty, but between King and long Rook is at least one Piece
        King can only do King moves and short castling
        */
        else if (isOnlyShortCastling(board, myPosition)) {
            theRightKingOffset = KING_SHORT;
        }
        /*
        if Tile between King and short Rook are empty and between King and long Rook are no Pieces
        King can do both, long and short castling
        */
        else if(isLongOrShortCastling(board, myPosition)){
            theRightKingOffset = KING_LONG_SHORT;
        }
        // King is never moved, but the tiles between him and his (same color) rooks are occupied
        else {
            theRightKingOffset = KING_AND_QUEEN;
        }

        // returning the right offset for King
        return theRightKingOffset;
    }

    /**
     * Method which returns the boolean value if the King can only do longCastling and
     * sets the flag for later usage in isLongOrShortCastling method
     * @param board Board with all the Pieces tiles etc.
     * @param myPosition position of king
     * @return the flag if only longCastling is possible
     */
    public boolean isOnlyLongCastling(Board board,Position myPosition){
        int myRow = myPosition.getI();
        int myCol = myPosition.getJ();
        // Tiles between the King and his Rooks
        Tile shortTile1 = board.getSpecificTile(myRow,myCol+1);
        Tile shortTile2 = board.getSpecificTile(myRow,myCol+2);
        Tile longTile1 = board.getSpecificTile(myRow,myCol-1);
        Tile longTile2 = board.getSpecificTile(myRow,myCol-2);
        Tile longTile3 = board.getSpecificTile(myRow,myCol-3);
        Tile rookTileLong = board.getSpecificTile(myRow,myCol -4);

        List<Tile> notNullTiles = new ArrayList<>();
        notNullTiles.add(shortTile1);
        notNullTiles.add(shortTile2);
        notNullTiles.add(longTile1);
        notNullTiles.add(longTile2);
        notNullTiles.add(longTile3);
        notNullTiles.add(rookTileLong);
        for (Tile tile: notNullTiles) {
            if (tile == null) {
                return false;
            }
        }
        if (rookTileLong.getPiece() instanceof Rook && !longTile1.isOccupied() && !longTile2.isOccupied() && !longTile3.isOccupied()){
            isRowBetweenKIngAndLongRookNotOccupied = true;
            return shortTile1.isOccupied() || shortTile2.isOccupied();
        }
        else{
            return false;
        }
    }

    /**
     * Method which returns the boolean value if the King can only do shortCastling and
     * sets the flag for later usage in isLongOrShortCastling method
     * @param board Board with all the Pieces tiles etc.
     * @param myPosition Position of King
     * @return the flag if only shortCastling is possible
     */
    public boolean isOnlyShortCastling(Board board, Position myPosition){
        int myRow = myPosition.getI();
        int myCol = myPosition.getJ();
        // Tiles between the King and his Rooks
        Tile shortTile1 = board.getSpecificTile(myRow,myCol+1);
        Tile shortTile2 = board.getSpecificTile(myRow,myCol+2);
        Tile rookTileShort = board.getSpecificTile(myRow,myCol + 3);
        Tile longTile1 = board.getSpecificTile(myRow,myCol-1);
        Tile longTile2 = board.getSpecificTile(myRow,myCol-2);
        Tile longTile3 = board.getSpecificTile(myRow,myCol-3);


        List<Tile> notNullTiles = new ArrayList<>();
        notNullTiles.add(shortTile1);
        notNullTiles.add(shortTile2);
        notNullTiles.add(longTile1);
        notNullTiles.add(longTile2);
        notNullTiles.add(longTile3);
        notNullTiles.add(rookTileShort);

        for (Tile tile: notNullTiles) {
            if (tile == null) {
                return false;
            }
        }

        if (rookTileShort.getPiece() instanceof Rook && !shortTile1.isOccupied() && !shortTile2.isOccupied()){
            isRowBetweenKIngAndShortRookNotOccupied = true;
            return longTile1.isOccupied() || longTile2.isOccupied() || longTile3.isOccupied();

        }
        else{
            return false;
        }
    }

    /**
     * Method which returns the boolean value if the King can do both Castling directions.
     * @param board Board with all the Pieces tiles etc.
     * @param myPosition position of king
     * @return the flag if LongOrShortCastling is possible
     */
    public boolean isLongOrShortCastling(Board board, Position myPosition){
        int myRow = myPosition.getI();
        int myCol = myPosition.getJ();
        // Tiles of the rooks
        Tile rookTileLong = board.getSpecificTile(myRow,myCol -4);
        Tile rookTileShort = board.getSpecificTile(myRow,myCol + 3);
        if(rookTileLong == null|| rookTileShort == null){
            return false;
        }
        if (isRowBetweenKIngAndShortRookNotOccupied && isRowBetweenKIngAndLongRookNotOccupied){
            return rookTileShort.getPiece() instanceof Rook && rookTileLong.getPiece() instanceof Rook;
        }
        else{
            return false;
        }
    }

    /**
     * This method gives back a char for the piece its called on
     * @param color Significant color of a selected chess piece
     * @return Returns capital letters for white pieces, and small letter for black pieces
     */
    @Override
    public char getConsoleChar(ChessColor color) {
        //eine Abfrage ob weiß oder schwarz
        if(ChessColor.White == color)
            return 'K';
        return 'k';
    }
}
