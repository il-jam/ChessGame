package chess.controller;
import chess.TestHelper;
import chess.model.*;

import chess.model.pieces.King;
import chess.model.pieces.Pawn;
import chess.model.pieces.Piece;
import chess.model.pieces.Rook;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * test class for ClIHumanController
 */
public class ClIHumanControllerTest {
    TestHelper testHelper = new TestHelper();

    /**
     * test method for useInputToMakeMoveTest
     */
    @Test
    public void useInputToMakeMoveTest(){
        Game game = new Game();
        Board board = game.getBoard();
        ClIHumanController cliController = new ClIHumanController(game);
        Piece queen = board.getSpecificTile(0,3).getPiece();
        String pieceNotExists = "a4-a5";
        String wrongInputColor = "a7-a6";
        String inputString = "a2-a3";

        board.getRemovedPieces().add(queen);
        String beatenString = "beaten";
        Tile tile = board.getSpecificTile(5,0);
        assertNull(tile.getPiece());
        cliController.useInputToMakeMove(wrongInputColor);
        cliController.useInputToMakeMove(pieceNotExists);
        cliController.useInputToMakeMove(inputString);
        assertTrue(tile.getPiece() instanceof Pawn);
        cliController.useInputToMakeMove(beatenString);

    }

    /**
     * testMethod for a couple of wrong moves
     */
    @Test
    public void movingInCheckPositionTest(){
        Game game = new Game();
        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();
        ClIHumanController cliController = new ClIHumanController(game);

        // set the Pieces we need
        board[0][4].getValue().setPiece(new King(ChessColor.White));
        board[6][0].getValue().setPiece(new Rook(ChessColor.White));

        board[1][0].getValue().setPiece(new Pawn(ChessColor.Black));
        board[6][3].getValue().setPiece(new Rook(ChessColor.Black));
        board[7][4].getValue().setPiece(new King(ChessColor.Black));


        Board b = game.getBoard();

        // set this predefined custom board
        b.setBoardTiles(board);
        String invalidMoveString = "wgdsf";
        String checkMoveString = "e8-d8";

        ChessColor playerColorWhite = b.getSpecificTile(0,4).getPiece().getColor();
        ChessColor playerColorBlack = b.getSpecificTile(1,0).getPiece().getColor();
        assertEquals(playerColorWhite, game.getCurrentPlayer());

        cliController.useInputToMakeMove(checkMoveString);
        cliController.useInputToMakeMove(invalidMoveString);

        assertEquals(playerColorWhite, game.getCurrentPlayer());
        cliController.useInputToMakeMove("e8-f8");

        assertEquals(playerColorBlack, game.getCurrentPlayer());
        cliController.useInputToMakeMove("d2-f2");
        cliController.useInputToMakeMove("f8-e8");
        cliController.useInputToMakeMove("f2-f3");
        cliController.useInputToMakeMove("a2-a1");
        assertEquals(playerColorBlack,game.getCurrentPlayer());


    }
}
