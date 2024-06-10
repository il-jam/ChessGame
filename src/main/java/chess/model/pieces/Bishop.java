package chess.model.pieces;

import chess.model.*;


import java.util.List;

import static chess.model.Constants.BISHOP;

/**
 * This class contains the information of the bishop
 */
public class Bishop extends Piece {

    /**
     * This is the constructor for the chess pieces
     * @param color Significant color of a selected chess piece
     */
    public Bishop(ChessColor color) {
        super(color);
    }

    /**
     * Method that creates an array of possible tiles a piece can move to
     * @param board 8x8 array consisting of tiles
     * @param myPosition Position of Bishop
     * @return An array of possibles tiles a piece can move to at any time
     */
    @Override
    public List<Tile> possibleTiles(Board board, Position myPosition) {

        return getGenericPossibleTiles(board, myPosition, BISHOP, PieceMoveType.LONG);
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
            return 'B';
        return 'b';
    }
}
