package chess.model.ai;

import chess.model.*;
import chess.model.pieces.Piece;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static chess.model.Constants.BOARD_LENGTH;

/**
 * Class of MInMaxCalc
 */
public class MinMaxCalc {

    private final int value;

    private final Board board;

    private final List<AIMove> aIMoves;

    private final ChessColor aIColor;

    private ChessColor playerColor;

    private final AIMove lastAIMove;

    private final boolean isBlacksTurn;

    private Piece beaten;

    /**
     * Constructor of MinMaxCalc Class
     * @param board Current Board
     * @param isBlacksTurn boolean to routine the player color of the Moves the AI calculates
     * @param lastAIMove the last aIMove of iteration thru the aIMoves
     * @param aIColor Color of AI
     */
    public MinMaxCalc(Board board, boolean isBlacksTurn, AIMove lastAIMove, ChessColor aIColor){
        this.board = board;
        this.isBlacksTurn = isBlacksTurn;
        this.lastAIMove = lastAIMove;
        this.value = calcBoardValue(board);
        this.aIMoves = getAIMoves(board);
        this.aIColor = aIColor;
    }

    /**
     * Method which calculates the best possible Move for the AI
     * @param isBlack boolean to routine the player color of the Moves the AI calculates
     * @param maxDepth The depth, the AI calculates thru
     * @return the Best possible AIMove
     */
    public AIMove bestValuedMove(boolean isBlack, int maxDepth){
        List<AIMove> bestMoves = new ArrayList<>();
        int bestValue;
        if(isBlack){
            bestValue = 1000;
            for (AIMove aimove: getAIMoves(board)) {
                moveSimulation(getAiStartAndEndPos(aimove)[0], getAiStartAndEndPos(aimove)[1], board);
                MinMaxCalc minMaxCalc = new MinMaxCalc(board, !isBlacksTurn, aimove, aIColor);
                int minmaxValue = minMaxCalc.minmax(maxDepth, -10000, 10000, board);
                if (minmaxValue <= bestValue){
                    if (minmaxValue < bestValue){
                        bestMoves.clear();
                        bestValue = minmaxValue;
                    }
                    bestMoves.add(minMaxCalc.getLastAIMove());
                }
                undoMove(aimove,board);
            }
        }
        else{
            bestValue = -1000;
            for (AIMove aimove: getAIMoves(board)) {
                moveSimulation(getAiStartAndEndPos(aimove)[0], getAiStartAndEndPos(aimove)[1], board);
                MinMaxCalc minMaxCalc = new MinMaxCalc(board, !isBlacksTurn, aimove, aIColor);
                int minmaxValue = minMaxCalc.minmax(maxDepth, -10000, 10000, board);
                if (minmaxValue >= bestValue){
                    if (minmaxValue > bestValue){
                        bestMoves.clear();
                        bestValue = minmaxValue;
                    }
                    bestMoves.add(minMaxCalc.getLastAIMove());
                }
                undoMove(aimove,board);
            }
        }
        int i = new Random().nextInt(bestMoves.size());
        return bestMoves.get(i);
    }

    /**
     * Method which uses alpha beta pruning to support calculation in bestValuedMove()
     * @param depth the depth the AI calculates to
     * @param newAlpha best Value for Black
     * @param newBeta best Value for White
     * @param clone cloned Board
     * @return minValue for Black Player and maxValue for WhitePlayer
     *         new Value for pruning unnecessary Moves, so the AI calculates faster
     */
    private int minmax(int depth, int newAlpha, int newBeta, Board clone){
        int alpha = newAlpha;
        int beta = newBeta;
        if (aIMoves.size() == 0){ //== checkmate
            if (isBlacksTurn){
                return 1000;
            } else {
                return -1000;
            }
        }
        if (depth == 0){
            return value;
        }
        if (isBlacksTurn){
            int minValue = 10000;
            for (AIMove aIMove : aIMoves) {
                //the Move
                moveSimulation(getAiStartAndEndPos(aIMove)[0], getAiStartAndEndPos(aIMove)[1],clone);
                // new calc
                MinMaxCalc minMaxCalc = new MinMaxCalc(clone, false, aIMove, aIColor);

                int value = minMaxCalc.minmax(depth-1, alpha ,beta,clone);
                minValue = Math.min(minValue, value);
                undoMove(aIMove,clone);
                beta = Math.min(beta,value);
                if(beta <= alpha){
                    break;
                }
            }
            return minValue;
        }
        else{
            int maxValue = -10000;
            for (AIMove aIMove: aIMoves){
                moveSimulation(getAiStartAndEndPos(aIMove)[0], getAiStartAndEndPos(aIMove)[1],clone);
                MinMaxCalc minMaxCalc = new MinMaxCalc(clone, true, aIMove, aIColor);
                int value = minMaxCalc.minmax(depth-1, alpha, beta, clone);
                maxValue = Math.max(maxValue, value);
                undoMove(aIMove, clone);
                alpha = Math.max(alpha,value);
                if(beta <= alpha){
                    break;
                }
            }
            return maxValue;
        }
    }

    /**
     * Method which helps to shorten the minmax() and bestValuedMove() methods
     * @param aiMove the current AIMove to further calculate on
     * @return Position Array for simulating a move on Cloned Board
     */
    private Position[] getAiStartAndEndPos(AIMove aiMove){
        Position[] aiStartAndEndPos = new Position[2];
        Tile startTile = aiMove.getStartTile();
        Tile endTile = aiMove.getEndTile();
        Position startPosition = new Position(startTile.getRow(), startTile.getColumn());
        Position endPosition = new Position(endTile.getRow(), endTile.getColumn());
        aiStartAndEndPos[0] = startPosition;
        aiStartAndEndPos[1] = endPosition;

        return aiStartAndEndPos;
    }

    /**
     * Method which calculates the current Value of all pieces on board
     * @param board current board, and later the cloned board
     * @return the Value of all Pieces added up
     */
    private int calcBoardValue(Board board){
        return calcWhiteValue(board) + calcBlackValue(board);
    }

    /**
     * Method to calculate the Value of all White Pieces together
     * @param clone the board with the Pieces on
     * @return the Value of all White Pieces
     */
    private int calcWhiteValue(Board clone){

        int value = 0;
        List<Piece> pieces = getAllPiecesFromColor(clone, ChessColor.White);
        for (Piece piece : pieces) {
            char pieceType = piece.getConsoleChar(ChessColor.White);
            switch (pieceType){
                case 'P':
                    value += 10;
                    break;
                case 'N':
                case 'B':
                    value += 30;
                    break;
                case 'R':
                    value += 50;
                    break;
                case 'Q':
                    value += 90;
                    break;
                case 'K':
                    value += 900;
                    break;
            }
        }
        return value;
    }

    /**
     * Method to calculate the Value of all Black Pieces together
     * @param clone the board with the Pieces on
     * @return the Value of all Black Pieces
     */
    private int calcBlackValue(Board clone){
        int value = 0;
        List<Piece> pieces = getAllPiecesFromColor(clone, ChessColor.Black);
        for (Piece piece : pieces) {
            char pieceType = piece.getConsoleChar(ChessColor.Black);
            switch (pieceType){
                case 'p':
                    value += -10;
                    break;
                case 'n':
                case 'b':
                    value += -30;
                    break;
                case 'r':
                    value += -50;
                    break;
                case 'q':
                    value += -90;
                    break;
                case 'k':
                    value += -900;
                    break;
            }
        }
        return value;

    }
    private AIMove getLastAIMove() {
        return lastAIMove;
    }

    /**
     * Method to get us all the StartTiles of all Pieces the Ai controls
     * @param board current Board we inspect
     * @return A list of all the startTiles
     */
    private List<Tile> getAIStartTiles(Board board) {
        List<Tile> aIStartTiles = new ArrayList<>();
        if (isBlacksTurn){
            setPlayerColor(ChessColor.Black);
        }
        else{
            setPlayerColor(ChessColor.White);
        }
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                SimpleObjectProperty<Tile> tile = board.getTiles()[i][j];
                Piece piece = tile.getValue().getPiece();
                if (piece != null && getAllPiecesFromColor(board, playerColor).contains(piece)){
                    aIStartTiles.add(tile.getValue());
                }
            }
        }
        return aIStartTiles;

    }

    /**
     * Method to get us all moves The AI can do
     * @param board the current Board we inspect
     * @return a List with all the possible AIMoves
     */
    public List<AIMove> getAIMoves(Board board){
        List<AIMove> allAIMoves = new ArrayList<>();
        for (Tile aIStartTile: getAIStartTiles(board)) {
            Position aIPiecePosition = new Position(aIStartTile.getRow(),aIStartTile.getColumn());
            Piece aIPiece = aIStartTile.getPiece();
            List<Tile> possibleAIPieceEndTiles = aIPiece.possibleTiles(board, aIPiecePosition);
            for (Tile aIEndTile: possibleAIPieceEndTiles) {
                CheckHandler checkHandler = new CheckHandler(board);
                Piece lastPieceFromEndTile = aIEndTile.getPiece();
                aIEndTile.setPiece(aIPiece);
                aIStartTile.setPiece(null);
                if (!checkHandler.isCheck(aIColor)){
                    AIMove aiMove = new AIMove(aIStartTile,aIEndTile);
                    allAIMoves.add(aiMove);
                }
                aIStartTile.setPiece(aIPiece);
                aIEndTile.setPiece(lastPieceFromEndTile);
            }
        }
        return  allAIMoves;
    }

    /**
     * Method which Simulates an AIMove
     * @param startPosition startPosition of AIMove
     * @param endPosition endPosition of AIMove
     * @param board current Board we Inspect
     */
    private void moveSimulation(Position startPosition, Position endPosition , Board board){
        Tile startTile = board.getSpecificTile(startPosition.getI(), startPosition.getJ());
        Tile endTile = board.getSpecificTile(endPosition.getI(), endPosition.getJ());
        if (endTile.isOccupied()){
            setBeaten(endTile.getPiece());
        }
        else {
            setBeaten(null);
        }
        board.removePiece(endTile);
        Piece pieceWeMove = startTile.getPiece();
        endTile.setPiece(pieceWeMove);
        board.removePiece(startTile);
        pieceWeMove.increaseMoveCount();
    }

    /**
     * Method to undo the Last AIMove
     * @param aiMove lastAIMove
     * @param board current Board we inspect
     */
    private void undoMove(AIMove aiMove, Board board){
        Piece lastBeaten = getBeaten();
        Tile startTile = aiMove.getStartTile();
        Tile endTile = aiMove.getEndTile();
        board.movePieceFromStartToEndTile(endTile,startTile);
        Piece movedPiece = endTile.getPiece();
        movedPiece.decreaseMoveCount();
        endTile.setPiece(null);
        endTile.setPiece(lastBeaten);

    }

    /**
     * method to get us all the Pieces of a specific Color
     * @param clone board we want to inspect
     * @param color the color we want to get the Pieces of
     * @return A list with all the Pieces of chosen Color
     */
    private List<Piece> getAllPiecesFromColor(Board clone, ChessColor color) {
        List<Piece> coloredPieces = new ArrayList<>();
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                SimpleObjectProperty<Tile> tile = clone.getTiles()[i][j];
                Piece piece = tile.getValue().getPiece();
                if (piece != null && piece.getColor() == color) {
                        coloredPieces.add(piece);
                }
            }
        }
        return coloredPieces;
    }
    public void setBeaten(Piece beaten) {
        this.beaten = beaten;
    }

    public Piece getBeaten() {
        return beaten;
    }

    public void setPlayerColor(ChessColor playerColor) {
        this.playerColor = playerColor;
    }
}
