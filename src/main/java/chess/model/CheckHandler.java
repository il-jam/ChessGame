package chess.model;

import chess.model.pieces.King;
import chess.model.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

import static chess.model.Constants.BOARD_LENGTH;

/**
 * CheckHandler Class which contains Check logic
 */
public class CheckHandler {

    Board board;

    /**
     * Constructor of CheckHandler class
     * @param board the board we need for current positions
     */
    public CheckHandler(Board board) {
        this.board = board;
    }


    /**
     * Method to test if a player is in Check
     * @param playerColor input color of player
     * @return a boolean if the player is in check or not
     */
    public boolean isCheck(ChessColor playerColor) {


        Tile kingTile = board.findKingTile(playerColor == ChessColor.White ? ChessColor.White : ChessColor.Black);

        List<Tile> enemyPossiblePositions = enemyPossiblePositions(board, playerColor);
        boolean isCheck = false;
        for (Tile enemyPossiblePosition : enemyPossiblePositions) {
            if (enemyPossiblePosition.equals(kingTile)) {
                isCheck = true;
                break;
            }
        }

        return isCheck;
    }

    /**
     * Checkmate method which asks if there are possible moves of a player, without being in check afterwords,
     * to fully be checkmate the player also has to be in check.
     * @param playerColor input color of player
     * @return a boolean if the player is checkmate or not
     */
    public boolean isCheckMate(ChessColor playerColor) {
        return findPossibleTilesWithoutCheck(playerColor).isEmpty() && isCheck(playerColor);
    }

    /**
     * Stalemate Method which asks if there are possible moves of a player, without being in check afterwords,
     * but must not be checked as well.
     * @param playerColor input color of player
     * @return a boolean if the player is stalemate or not
     */
    public boolean isStalemate(ChessColor playerColor) {

        if (checkForOnlyTwoKings(board)){
            return true;
        }
        return findPossibleTilesWithoutCheck(playerColor).isEmpty() && !isCheck(playerColor);
    }

    /**
     * method which returns a list with tiles where the player can go without being in check. we need it for stalemate and checkmate logic.
     * @param playerColor input color of player
     * @return A list of possible positions without the player being in check
     */
    public List<Tile> findPossibleTilesWithoutCheck(ChessColor playerColor) {
        List<Tile> tileListWithoutCheck = new ArrayList<>();
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                Tile tile = board.getTiles()[i][j].getValue();
                Piece piece = tile.getPiece();
                if (piece != null && piece.getColor() == playerColor) {
                    Position position = new Position(i,j);
                    List<Tile> myPossibleMoves = piece.possibleTiles(board,position);

                    for (Tile testTile : myPossibleMoves) {
                        Piece testTilePiece = testTile.getPiece();

                        testTile.setPiece(null);
                        testTile.setPiece(piece);
                        tile.setPiece(null);
                        if (!isCheck(playerColor)){
                            tileListWithoutCheck.add(testTile);
                        }
                        tile.setPiece(piece);
                        testTile.setPiece(testTilePiece);
                    }
                }
            }
        }
        return tileListWithoutCheck;
    }

    /**
     * a Method which gives us all the possible positions of the not active player, we need those positions to ask if the current players King is reachable for the other player.
     * @param playerColor input color of player
     * @param checkBoard the Board we want to check for enemyPositions
     * @return List of tiles the not active player can move to.
     */
    public static List<Tile> enemyPossiblePositions(Board checkBoard, ChessColor playerColor) {

        List<Tile> enemyPossiblePositions = new ArrayList<>();
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                Piece piece = checkBoard.getTiles()[i][j].getValue().getPiece();
                if (piece != null && piece.getColor() != playerColor) {
                    Position position = new Position(i,j);
                    enemyPossiblePositions.addAll(piece.possibleTiles(checkBoard, position));

                }
            }
        }
        return enemyPossiblePositions;
    }
    private boolean checkForOnlyTwoKings(Board board){

        boolean isTrue = true;
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                Tile tile = board.getTiles()[i][j].getValue();
                Piece piece = tile.getPiece();
                if (piece != null && !(piece instanceof King)){
                    isTrue = false;
                    break;
                }
            }
        }
        return isTrue;
    }
}
