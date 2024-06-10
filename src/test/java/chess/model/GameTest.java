package chess.model;

import chess.TestHelper;
import chess.model.pieces.King;
import chess.model.pieces.Pawn;
import chess.model.pieces.Queen;
import chess.model.pieces.Rook;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for Game class
 */
public class GameTest {

    TestHelper testHelper = new TestHelper();
    /**
     * Test method for isFinished
     * Black is Checkmate
     */
    @Test
    public void isFinishedTestBCM() {
        Game game = new Game();
        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //kings
        board[7][4].getValue().setPiece(new King(ChessColor.White));
        board[0][0].getValue().setPiece(new King(ChessColor.Black));

        //queens
        board[3][2].getValue().setPiece(new Queen(ChessColor.White));

        //rooks
        board[7][0].getValue().setPiece(new Rook(ChessColor.White));

        Board b = game.getBoard();

        // set this predefined custom board
        b.setBoardTiles(board);
        b.getSpecificTile(7,4).getPiece().increaseMoveCount();
        b.getSpecificTile(0,0).getPiece().increaseMoveCount();

        Tile startTile = game.getBoard().getSpecificTile(3,2);
        Tile endTile = game.getBoard().getSpecificTile(1,0);
        String promotionLetter = "";

        game.isFinished();
        assertSame(GameState.getCurrentGameState(), GameState.GAME_RUNNING);



        game.getBoard().movePiece(startTile, endTile,promotionLetter);
        game.setCurrentPlayer(ChessColor.White);
        game.nextTurn();
        assertSame(GameState.getCurrentGameState(), GameState.WHITE_WINS);



    }
    /**
     * Test method for isFinished
     * White is Checkmate
     */
    @Test
    public void isFinishedTestWCM() {
        Game game = new Game();
        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //kings
        board[7][4].getValue().setPiece(new King(ChessColor.Black));
        board[0][0].getValue().setPiece(new King(ChessColor.White));

        //queens
        board[3][2].getValue().setPiece(new Queen(ChessColor.Black));

        //rooks
        board[7][0].getValue().setPiece(new Rook(ChessColor.Black));
        //pawn
        board[6][7].getValue().setPiece(new Pawn(ChessColor.White));

        Board b = game.getBoard();

        // set this predefined custom board
        b.setBoardTiles(board);
        b.getSpecificTile(7,4).getPiece().increaseMoveCount();
        b.getSpecificTile(0,0).getPiece().increaseMoveCount();

        Tile defaultMoveStart = game.getBoard().getSpecificTile(6,7);
        Tile defaultMoveEnd = game.getBoard().getSpecificTile(5,7);
        Tile startTileFinishHim = game.getBoard().getSpecificTile(3,2);
        Tile endTileFinishHim = game.getBoard().getSpecificTile(1,0);
        String promotionLetter = "";

        game.getBoard().movePiece(defaultMoveStart,defaultMoveEnd,promotionLetter);

        game.isFinished();

        assertSame(GameState.getCurrentGameState(), GameState.WHITE_MOVE_OUT_OF_CHECK);

        game.getBoard().movePiece(startTileFinishHim, endTileFinishHim,promotionLetter);
        game.setCurrentPlayer(ChessColor.Black);
        game.nextTurn();
        assertSame(GameState.getCurrentGameState(), GameState.BLACK_WINS);

    }

    /**
     * Test Method for isStaleMate with Black Player
     */
    @Test
    public void isStaleMateBlackTest(){
        Game game = new Game();
        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();


        //kings
        board[7][4].getValue().setPiece(new King(ChessColor.White));
        board[0][0].getValue().setPiece(new King(ChessColor.Black));

        //rooks
        board[7][1].getValue().setPiece(new Rook(ChessColor.White));
        board[1][2].getValue().setPiece(new Rook(ChessColor.White));

        Board b = game.getBoard();

        // set this predefined custom board
        b.setBoardTiles(board);
        b.getSpecificTile(7,4).getPiece().increaseMoveCount();
        b.getSpecificTile(0,0).getPiece().increaseMoveCount();

        game.setCurrentPlayer(ChessColor.Black);
        game.isFinished();
        assertSame(GameState.getCurrentGameState(), GameState.STALEMATE);



    }

    /**
     * Test Method for isStaleMate with White Player
     */
    @Test
    public void isStaleMateWhiteTest(){
        Game game = new Game();
        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();


        //kings
        board[7][4].getValue().setPiece(new King(ChessColor.Black));
        board[0][0].getValue().setPiece(new King(ChessColor.White));

        //rooks
        board[7][1].getValue().setPiece(new Rook(ChessColor.Black));
        board[1][2].getValue().setPiece(new Rook(ChessColor.Black));

        Board b = game.getBoard();

        // set this predefined custom board
        b.setBoardTiles(board);
        b.getSpecificTile(7,4).getPiece().increaseMoveCount();
        b.getSpecificTile(0,0).getPiece().increaseMoveCount();
        game.isFinished();
        assertSame(GameState.getCurrentGameState(), GameState.STALEMATE);

    }




}