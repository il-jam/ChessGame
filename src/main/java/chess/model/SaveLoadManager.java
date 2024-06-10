package chess.model;

import chess.model.pieces.*;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SaveLoadManager Class which contains all functionality for the Save and Load mechanic of our Game
 */
public class SaveLoadManager {

    private final Game game;

    /**
     * Constructor of SaveLoadManager
     * @param game current game we eventually want to save
     */
    public SaveLoadManager (Game game){
        this.game = game;
    }
    /**
     * This method saves the game/board state so we can load it
     *
     * @param filePath the path to the .txt file
     * @throws IOException exception
     */
    public void saveGame(String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Tile t = game.getBoard().getTiles()[i][j].getValue();
                Piece p = t.getPiece();
                if (p != null) {
                    // for every piece on the board we write one line in the .txt file
                    writer.write(p.getConsoleChar(p.getColor()) + ";" + i + ";" + j + ";" + p.getMoveCount() + ";");

                    writer.newLine(); // neue zeile
                }
            }
        }

        //after the lines for the pieces we save the other important values
        writer.write(game.getCurrentPlayer() + ";" + GameState.getCurrentGameState() + ";" + game.isAIGame() + ";"
                + game.getBoard().getLastEndTile().getRow() + ";" + game.getBoard().getLastEndTile().getColumn()
                + ";" + game.getBoard().getLastMovedPiece().getConsoleChar(game.getBoard().getLastMovedPiece().getColor())
                + ";" + game.getBoard().validSemanticMove);

        writer.newLine();
        writer.write("G");
        for (Piece piece : game.getBoard().getRemovedPieces()){
            char consoleChar = piece.getConsoleChar(piece.getColor());
            writer.write("," + consoleChar);
        }


        writer.newLine(); // neue zeile
        writer.write( "" + game.getBoard().getMoveHistory().moveHistoryListProperty().getValue());

        writer.close();


    }



    /**
     * this method loads a saveState
     * @param filePath the path to text file
     * @return New Game loaded out of File
     * @throws IOException e
     */
    public Game loadGame(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        Game savedGame = new Game();
        SimpleObjectProperty<Tile>[][] board = new SimpleObjectProperty[8][8];

        for(int i=0; i<8; i++) {

            for(int j=0; j<8; j++) {

                SimpleObjectProperty<Tile> t;
                t = new SimpleObjectProperty<>(new Tile(i, j));
                board[i][j] = t;
            }
        }

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {

            String[] parts = line.split(";");

            if(parts.length == 4){
                loadPieces(parts,board);
            }

            else if(parts.length == 7){
                loadFlags(parts,savedGame,board);

            }
            else if(line.charAt(0) == 'G'){
                loadGraveYard(line, savedGame);
            }
            else{
                String subtractedLine = line.substring(1, line.length() - 1);
                List<String> stringList = Arrays.asList(subtractedLine.split(","));

                SimpleListProperty<String> loadedMoveHistory = new SimpleListProperty<>();
                loadedMoveHistory.setValue(FXCollections.observableArrayList());

                loadedMoveHistory.addAll(stringList);

                savedGame.getMoveHistory().setMoveHistoryList(loadedMoveHistory);
            }

        }

        Board b = savedGame.getBoard();
        b.setBoardTiles(board);

        reader.close();

        return savedGame;
    }


    private void loadFlags(String [] parts, Game savedGame, SimpleObjectProperty<Tile>[][] board){
        String currentPlayerLoaded =  parts[0];
        String currentGameStateLoaded = parts[1];
        boolean loadedIsAIGame = Boolean.parseBoolean(parts[2]);
        int lastEndTileRow = Integer.parseInt(parts[3]);
        int lastEndTileCol = Integer.parseInt(parts[4]);
        String lastMovedPieceLoaded = parts[5];
        boolean loadedValidSemanticMove = Boolean.parseBoolean(parts[6]);

        GameState.setGameState(loadGameState(currentGameStateLoaded));


        savedGame.setCurrentPlayer(colorOutOfString(currentPlayerLoaded));

        savedGame.setAIGame(loadedIsAIGame);


        Piece lastMovedPieceOutOfLastEndTile = board[lastEndTileRow][lastEndTileCol].getValue().getPiece();
        savedGame.getBoard().setLastMovedPiece(lastMovedPieceOutOfLastEndTile);

        Piece lastMovedPiece;
        if(Character.isUpperCase(lastMovedPieceLoaded.charAt(0))) {
            lastMovedPiece = loadPieceOutOfUpperString(lastMovedPieceLoaded);
        }
        else{
            lastMovedPiece = loadPieceOutOfLowerString(lastMovedPieceLoaded);
        }
        savedGame.getBoard().setLastMovedPiece(lastMovedPiece);
        savedGame.getBoard().validSemanticMove = loadedValidSemanticMove;
    }

    private void loadPieces(String [] parts, SimpleObjectProperty<Tile>[][] board){
        String pieceTypeAndColor = parts[0];
        int row = Integer.parseInt(parts[1]);
        int col = Integer.parseInt(parts[2]);
        int moveCount = Integer.parseInt(parts[3]);

        Piece piece;
        if(Character.isUpperCase(pieceTypeAndColor.charAt(0))) {
            piece = loadPieceOutOfUpperString(pieceTypeAndColor);
        }
        else{
            piece = loadPieceOutOfLowerString(pieceTypeAndColor);
        }

        piece.setMoveCount(moveCount);

        board[row][col].getValue().setPiece(piece);
    }

    private ChessColor colorOutOfString(String currentPlayerAsString){
        ChessColor currentPlayerLoaded;
        switch (currentPlayerAsString){
            case "Black":
                currentPlayerLoaded = ChessColor.Black;
                break;
            case "White":
                currentPlayerLoaded = ChessColor.White;
                break;
            default:
                throw new IllegalStateException(Constants.UNEXPECTED_VALUE + currentPlayerAsString);
        }
        return currentPlayerLoaded;
    }

    private GameState loadGameState(String s){
        GameState gameStateLoaded;
        if (s.charAt(0) == 'B'){
            gameStateLoaded = returnBlackRelatedGameState(s);
        }
        else if (s.charAt(0) == 'W'){
            gameStateLoaded = returnWhiteRelatedGameState(s);
        }
        else {
            gameStateLoaded = returnOtherGameState(s);
        }
        return gameStateLoaded;
    }

    private GameState returnBlackRelatedGameState(String s){
        GameState blackGameStateLoaded;
        switch (s){
            case "BLACK_IN_CHECK":
                blackGameStateLoaded = GameState.BLACK_IN_CHECK;
                //GameState.setGameState(GameState.BLACK_IN_CHECK);
                break;
            case "BLACK_MOVE_OUT_OF_CHECK":
                blackGameStateLoaded = GameState.BLACK_MOVE_OUT_OF_CHECK;
                break;
            case "BLACK_WINS":
                blackGameStateLoaded = GameState.BLACK_WINS;
                break;

            default:
                throw new IllegalStateException(Constants.UNEXPECTED_VALUE + s);
        }
        return blackGameStateLoaded;
    }

    private GameState returnWhiteRelatedGameState(String s){
        GameState whiteGameStateLoaded;
        switch (s){
            case "WHITE_IN_CHECK":
                whiteGameStateLoaded = GameState.WHITE_IN_CHECK;
                break;
            case "WHITE_MOVE_OUT_OF_CHECK":
                whiteGameStateLoaded = GameState.WHITE_MOVE_OUT_OF_CHECK;
                break;

            case "WHITE_WINS":
                whiteGameStateLoaded = GameState.WHITE_WINS;
                break;

            default:
                throw new IllegalStateException(Constants.UNEXPECTED_VALUE + s);
        }
        return whiteGameStateLoaded;
    }

    private GameState returnOtherGameState(String s){
        GameState otherGameState;
        switch (s){
            case "GAME_RUNNING":
                otherGameState = GameState.GAME_RUNNING;
                break;
            case "STALEMATE":
                otherGameState = GameState.STALEMATE;
                break;

            default:
                throw new IllegalStateException(Constants.UNEXPECTED_VALUE + s);
        }
        return otherGameState;
    }

    private Piece loadPieceOutOfLowerString(String piece){
        Piece p;
        switch (piece){
            case "k":
                p = new King(ChessColor.Black);
                break;
            case "p":
                p = new Pawn(ChessColor.Black);
                break;
            case "q":
                p = new Queen(ChessColor.Black);
                break;
            case "r":
                p = new Rook(ChessColor.Black);
                break;
            case "b":
                p = new Bishop(ChessColor.Black);
                break;
            case "n":
                p = new Knight(ChessColor.Black);
                break;
            default:
                throw new IllegalStateException(Constants.UNEXPECTED_VALUE + piece);
        }
        return p;
    }


    private Piece loadPieceOutOfUpperString(String piece){
        Piece p;
        switch (piece) {
            case "K":
                p = new King(ChessColor.White);
                break;
            case "P":
                p = new Pawn(ChessColor.White);
                break;
            case "Q":
                p = new Queen(ChessColor.White);
                break;
            case "R":
                p = new Rook(ChessColor.White);
                break;
            case "B":
                p = new Bishop(ChessColor.White);
                break;
            case "N":
                p = new Knight(ChessColor.White);
                break;
            default:
                throw new IllegalStateException(Constants.UNEXPECTED_VALUE + piece);
        }
        return p;

    }

    /**
     * This method loads the saved graveyard
     * @param line this is the string  in which the graveyard pieces are saved
     * @param savedGame the game we want to load
     */
    private void loadGraveYard(String line, Game savedGame) {
        if (line.length() > 1) {
            String lineWithoutTheG = line.substring(2);
            List<String> stringList = Arrays.asList(lineWithoutTheG.split(","));


            Piece piece;
            List<Piece> graveYardPieces = new ArrayList<>();

            for (String s : stringList) {
                char c = s.charAt(0);
                if (Character.isUpperCase(c)) {
                    piece = loadPieceOutOfUpperString(s);
                } else {
                    piece = loadPieceOutOfLowerString(s);
                }
                graveYardPieces.add(piece);
            }

            SimpleListProperty<Piece> loadedGraveYard = new SimpleListProperty<>();
            loadedGraveYard.setValue(FXCollections.observableArrayList());

            loadedGraveYard.addAll(graveYardPieces);

            savedGame.getBoard().setRemovedPieces(loadedGraveYard);
        }


    }
}
