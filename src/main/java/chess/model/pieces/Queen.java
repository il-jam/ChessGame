package chess.model.pieces;

import chess.model.*;


import java.util.List;
import static chess.model.Constants.KING_AND_QUEEN;


/**
 * This class contains the information of the queen
 */
public class Queen extends Piece {
    /**
     * This is the constructor for the chess pieces
     *
     * @param color Significant color of a selected chess piece
     */
    public Queen(ChessColor color) {
        super(color);
    }

    /**
     * Method that creates an array of possible tiles a piece can move to
     * @param board 8x8 array consisting of tiles
     * @param myPosition current position of Piece
     * @return An array of possibles tiles a piece can move to at any time
     */
    @Override
    public List<Tile> possibleTiles(Board board, Position myPosition) {

        return getGenericPossibleTiles(board, myPosition, KING_AND_QUEEN, PieceMoveType.LONG);
    }

    /**
     * This method gives back a char for the piece its called on
     * @param color significant color of a selected chess piece
     * @return Returns capital letters for white pieces, and small letter for black pieces
     */
    @Override
    public char getConsoleChar(ChessColor color){
        if(ChessColor.White == color)
            return 'Q';
        return 'q';
    }
}
