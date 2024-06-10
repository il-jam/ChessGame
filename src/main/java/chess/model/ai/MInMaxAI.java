package chess.model.ai;

import chess.model.Board;
import chess.model.ChessColor;
import chess.model.Tile;
import chess.model.pieces.Piece;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

import static chess.model.Constants.BOARD_LENGTH;

/**
 * Class of MinMaxAI
 */
public class MInMaxAI extends AIType{

    private String aiMoveString;
    int maxDepth;
    /**
     * Constructor of MinMaxAI Class
     * @param board current Board
     * @param aIColor Color of AI
     * @param isBlack boolean to routine the player color of the Moves the AI calculates
     */
    public MInMaxAI(Board board, ChessColor aIColor, boolean isBlack) {
        super(board, aIColor, isBlack);
        maxDepth = board.maxDepth;
    }

    /**
     * Method which makes an AI move, and prints out that move
     */
    @Override
    public void makeAIMove() {
        Board clone = new Board(board);
        MinMaxCalc minMaxCalc = new MinMaxCalc(clone,black,null, aIColor);
        if (isFirstTurn(board)){
            Tile defaultStartTile = board.getSpecificTile(6,3);
            Tile defaultEndTile = board.getSpecificTile(4,3);
            board.movePiece(defaultStartTile,defaultEndTile,"");
            aiMoveString = "d2-d4";
        }
        else{
            if (aIColor == ChessColor.Black){
                System.out.print("MinMaxAI is thinking 10000111100101100...");
            }
            else{
                System.out.print("MinMaxAI is thinking 10100111001...");
            }
            AIMove aiMove = minMaxCalc.bestValuedMove(black,maxDepth);
            useTilesToMovePiece(aiMove);
            aiMoveString = aiMove.convertToInputString();
        }

        System.out.println();
        System.out.println("MinMaxAI move was !" + aiMoveString);
    }

    private boolean isFirstTurn(Board board){
        boolean firstTurn = true;
        List<Piece> allPieces = new ArrayList<>();
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                SimpleObjectProperty<Tile> tile = board.getTiles()[i][j];
                Piece piece = tile.getValue().getPiece();
                if (piece != null) {
                    allPieces.add(piece);
                }
            }
        }
        for (Piece p : allPieces) {
            if (p.moveCount > 0){
                firstTurn = false;
                break;
            }
        }
        return firstTurn;
    }
}
