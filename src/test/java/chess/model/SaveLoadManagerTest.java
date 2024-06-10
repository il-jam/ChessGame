package chess.model;

import chess.TestHelper;
import chess.model.pieces.*;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for SaveLoadManager
 */
public class SaveLoadManagerTest {
    TestHelper testHelper = new TestHelper();

    Game game;

    /**
     * Test method for saving the game in the .txt file
     */
    @Test
    public void saveGameTest(){
        game = new Game();
        SimpleObjectProperty<Tile>[][] board = testHelper.createTestBoard();

        for(int i=0; i<8; i++) {

            for(int j=0; j<8; j++) {

                SimpleObjectProperty<Tile> t;
                t = new SimpleObjectProperty<>(new Tile(i, j));
                board[i][j] = t;
            }
        }

        //kings
        board[4][4].getValue().setPiece(new King(ChessColor.Black));
        board[0][0].getValue().setPiece(new King(ChessColor.White));

        //rooks
        board[4][1].getValue().setPiece(new Pawn(ChessColor.Black));
        board[6][3].getValue().setPiece(new Pawn(ChessColor.Black));

        Board b = game.getBoard();

        // set this predefined custom board
        b.setBoardTiles(board);
        b.getSpecificTile(4,4).getPiece().increaseMoveCount();
        b.getSpecificTile(0,0).getPiece().increaseMoveCount();

        Tile startTile = game.getBoard().getSpecificTile(4,1);
        Tile endTile = game.getBoard().getSpecificTile(5,1);
        String promotionLetter = "";

        game.getBoard().movePiece(startTile, endTile, promotionLetter);
        try{
            SaveLoadManager saveLoadManager = new SaveLoadManager(game);
            saveLoadManager.saveGame("Spielstand der Partie aus dem Konsolenspiel.txt");
            game = saveLoadManager.loadGame("Spielstand der Partie aus dem Konsolenspiel.txt");
            assertTrue(game.getBoard().getSpecificTile(0,0).getPiece() instanceof King);



        }
        catch (Exception e){
            System.out.println("not cool bro");
        }
    }

    /**
     * Test method for loading the .txt file
     */
    @Test
    public void loadGameStatesTest() {
        try {
            saveGameTest();
            SaveLoadManager saveLoadManager = new SaveLoadManager(game);
            game = saveLoadManager.loadGame("TestBlackIsChecked.txt");
            assertSame(GameState.getCurrentGameState(), GameState.BLACK_IN_CHECK);
            game = saveLoadManager.loadGame("TestWhiteIsChecked.txt");
            assertSame(GameState.getCurrentGameState(), GameState.WHITE_IN_CHECK);
            game = saveLoadManager.loadGame("TestBlackWins.txt");
            assertSame(GameState.getCurrentGameState(), GameState.BLACK_WINS);
            game = saveLoadManager.loadGame("TestWhiteWins.txt");
            assertSame(GameState.getCurrentGameState(), GameState.WHITE_WINS);
            game = saveLoadManager.loadGame("TestWhiteQueenIsBeaten.txt");
            assertSame(GameState.getCurrentGameState(), GameState.GAME_RUNNING);
            System.out.println("all Games have been loaded");

        } catch (Exception e) {
            System.out.println("hi");

        }
    }

    /**
     * method which tests if the Ai game has been loaded
     */
    @Test
    public void loadAiGameTest(){
        try {
            saveGameTest();
            SaveLoadManager saveLoadManager = new SaveLoadManager(game);
            game = saveLoadManager.loadGame("TestIsAIGame.txt");
            assertTrue(game.isAIGame());
            System.out.println("Ai Game has been loaded");

        } catch (Exception e) {

            System.out.println("hi");
        }
    }

    /**
     * method which tests if the graveyard has been loaded
     */
    @Test
    public void loadGraveyardTest(){
        try {
            saveGameTest();
            SaveLoadManager saveLoadManager = new SaveLoadManager(game);
            game = saveLoadManager.loadGame("TestWhiteQueenIsBeaten.txt");
            Piece whiteQueen = game.getBoard().getRemovedPieces().get(0);
            assertTrue(whiteQueen instanceof Queen && whiteQueen.getColor() == ChessColor.White);
            System.out.println("Graveyard has been loaded");

        } catch (Exception e) {

            System.out.println("hi");
        }
    }
}
