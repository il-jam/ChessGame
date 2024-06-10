package chess.model.ai;

import chess.TestHelper;
import chess.model.*;
import chess.model.pieces.*;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test method for the Simple AI
 */
public class SimpleAITest {
    TestHelper testHelper = new TestHelper();
    /**
     * test method for a Black SimpleAI move
     * to test if its greedy, we compare if the AI Chooses to beat the Queen over the Pawn.
     */
    @Test
    public void makeAIMoveBlackTest(){
        Game game = new Game();
        Piece blackBishop;
        Piece whiteQueen;
        Piece whiteBishop;

        boolean isBlack = true;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        board[0][1].getValue().setPiece(new King(ChessColor.Black));
        board[4][4].getValue().setPiece(blackBishop = new Bishop(ChessColor.Black));


        board[7][0].getValue().setPiece(new King(ChessColor.White));
        board[5][3].getValue().setPiece(whiteQueen = new Queen(ChessColor.White));
        board[5][5].getValue().setPiece(whiteBishop = new Bishop(ChessColor.White));


        Board b = game.getBoard();
        b.setBoardTiles(board);

        blackBishop.setMoveCount(4);
        whiteQueen.setMoveCount(2);
        whiteBishop.setMoveCount(8);
        game.setCurrentPlayer(ChessColor.White);
        SimpleAI simpleAI = new SimpleAI(b,ChessColor.White, isBlack);
        simpleAI.makeAIMove();
        assertEquals(b.getSpecificTile(5,3).getPiece(), blackBishop);
    }

    /**
     * test method for a White SimpleAI move
     * to test if its greedy, we compare if the AI Chooses to beat the Queen over the Pawn.
     */
    @Test
    public void makeAIMoveWhiteTest(){
        Game game = new Game();
        Piece blackQueen;
        Piece blackBishop;
        Piece whiteBishop;

        boolean isBlack = false;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        board[7][1].getValue().setPiece(new King(ChessColor.White));
        board[2][4].getValue().setPiece(whiteBishop = new Bishop(ChessColor.White));


        board[0][0].getValue().setPiece(new King(ChessColor.Black));
        board[1][3].getValue().setPiece(blackQueen = new Queen(ChessColor.Black));
        board[1][5].getValue().setPiece(blackBishop = new Bishop(ChessColor.Black));


        Board b = game.getBoard();
        b.setBoardTiles(board);

        whiteBishop.setMoveCount(7);
        blackQueen.setMoveCount(4);
        blackBishop.setMoveCount(3);
        game.setCurrentPlayer(ChessColor.White);
        SimpleAI simpleAI = new SimpleAI(b,ChessColor.White, isBlack);
        simpleAI.makeAIMove();
        assertEquals(b.getSpecificTile(1,3).getPiece(), whiteBishop);
    }


}