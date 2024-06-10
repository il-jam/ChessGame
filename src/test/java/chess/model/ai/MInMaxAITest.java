package chess.model.ai;

import chess.TestHelper;
import chess.model.*;
import chess.model.pieces.*;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the MinMaxAI
 */
public class MInMaxAITest {
    TestHelper testHelper = new TestHelper();
    /**
     * Test Method for Black MinMaxAI move
     * it further tests if the AI looks at ne next turns by not choosing to beat wPawn2 because it is protected
     */
    @Test
    public void makeAIMoveBlackTest(){
        Game game = new Game();
        Piece blackPawn;
        Piece wPawn1;
        Piece wPawn2;

        boolean isBlack = true;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        board[0][1].getValue().setPiece(new King(ChessColor.Black));
        board[3][4].getValue().setPiece(blackPawn = new Pawn(ChessColor.Black));


        board[7][0].getValue().setPiece(new King(ChessColor.White));
        board[4][3].getValue().setPiece(wPawn1 = new Pawn(ChessColor.White));
        board[4][5].getValue().setPiece(wPawn2 = new Pawn(ChessColor.White));
        board[5][6].getValue().setPiece(new Pawn(ChessColor.White));

        Board b = game.getBoard();
        b.setBoardTiles(board);

        blackPawn.setMoveCount(3);
        wPawn1.setMoveCount(1);
        wPawn2.setMoveCount(1);
        game.setCurrentPlayer(ChessColor.Black);
        game.getBoard().maxDepth = 3;
        MInMaxAI mInMaxAI = new MInMaxAI(b,ChessColor.Black, isBlack);
        mInMaxAI.makeAIMove();
        System.out.println(b.getSpecificTile(4,3).getPiece().getColor());
        assertEquals(b.getSpecificTile(4,3).getPiece(), blackPawn);
    }

    /**
     * Test Method for White MinMaxAI move
     * it further tests if the AI looks at ne next turns by not choosing to beat bPawn2 because it is protected
     */
    @Test
    public void makeAIMoveWhiteTest(){
        Game game = new Game();
        Piece bPawn1;
        Piece bPawn2;
        Piece wPawn;

        boolean isBlack = false;

        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        board[7][0].getValue().setPiece(new King(ChessColor.White));
        board[3][4].getValue().setPiece(wPawn = new Pawn(ChessColor.White));


        board[0][0].getValue().setPiece(new King(ChessColor.Black));
        board[2][3].getValue().setPiece(bPawn1 = new Pawn(ChessColor.Black));
        board[2][5].getValue().setPiece(bPawn2 = new Pawn(ChessColor.Black));
        board[1][6].getValue().setPiece(new Pawn(ChessColor.Black));

        Board b = game.getBoard();
        b.setBoardTiles(board);

        wPawn.setMoveCount(3);
        bPawn1.setMoveCount(1);
        bPawn2.setMoveCount(1);
        game.setCurrentPlayer(ChessColor.White);
        game.getBoard().maxDepth = 3;
        MInMaxAI mInMaxAI = new MInMaxAI(b,ChessColor.White, isBlack);
        mInMaxAI.makeAIMove();
        assertEquals(b.getSpecificTile(2,3).getPiece(), wPawn);
    }

}