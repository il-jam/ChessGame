package chess.model;

import chess.TestHelper;
import chess.model.pieces.Bishop;
import chess.model.pieces.King;
import chess.model.pieces.Queen;
import chess.model.pieces.Rook;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the CheckHandler
 */
public class CheckHandlerTest{

    TestHelper testHelper = new TestHelper();
    /**
     * Test method for isCheck
     */
    @Test
    public void isCheck() {
        Game game = new Game();
        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //kings
        board[7][4].getValue().setPiece(new King(ChessColor.White));
        board[0][4].getValue().setPiece(new King(ChessColor.Black));

        //queens
        board[7][3].getValue().setPiece(new Queen(ChessColor.White));

        game.getBoard().setBoardTiles(board);
        CheckHandler c = new CheckHandler(game.getBoard());


        Tile startTile = game.getBoard().getSpecificTile(7,3);
        Tile endTile = game.getBoard().getSpecificTile(6,4);
        String promotionLetter = "";
        game.getBoard().movePiece(startTile,endTile, promotionLetter);


        boolean blackIsCheck = c.isCheck(ChessColor.Black);
        assertTrue(blackIsCheck, "moving queen in front of enemy king");
    }

    /**
     * Test method for isStalemate
     */
    @Test
    public void isStalemate() {
        Game game = new Game();
        CheckHandler c = new CheckHandler(game.getBoard());
        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //kings
        board[7][4].getValue().setPiece(new King(ChessColor.White));
        board[0][4].getValue().setPiece(new King(ChessColor.Black));

        //bishops
        board[3][6].getValue().setPiece(new Bishop(ChessColor.White));

        //rooks
        board[2][3].getValue().setPiece(new Rook(ChessColor.White));
        board[3][5].getValue().setPiece(new Rook(ChessColor.White));

        Board b = game.getBoard();

        // set this predefined custom board
        b.setBoardTiles(board);

        boolean isStaleMate = c.isStalemate(ChessColor.Black);
        assertTrue(isStaleMate);
    }

    /**
     * Test method for findPossibleTilesWithoutCheck
     */
    @Test
    public void findPossibleTilesWithoutCheck() {
        Game game = new Game();
        CheckHandler c = new CheckHandler(game.getBoard());
        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        //kings
        board[7][4].getValue().setPiece(new King(ChessColor.White));
        board[0][4].getValue().setPiece(new King(ChessColor.Black));

        //rooks
        board[2][3].getValue().setPiece(new Rook(ChessColor.White));
        board[3][5].getValue().setPiece(new Rook(ChessColor.White));

        Board b = game.getBoard();

        // set this predefined custom board
        b.setBoardTiles(board);

        List<Tile> ghettoList = c.findPossibleTilesWithoutCheck(ChessColor.Black);

        assertEquals(1, ghettoList.size());
    }

}