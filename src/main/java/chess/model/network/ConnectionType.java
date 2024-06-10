package chess.model.network;

import chess.controller.Parser;
import chess.model.Game;
import chess.model.Tile;

import java.io.IOException;
import java.util.List;

/**
 * abstract Class of the ConnectionType Server or Client
 */
public abstract class ConnectionType {

    public Game game;

    public Parser parser;


    /**
     * Constructor of ConnectionType
     * @param game current Game
     */
    public ConnectionType(Game game){
        this.game = game;
        this.parser = new Parser(game);

    }

    /**
     * abstract method to compile a move, send by other connection party
     * @throws IOException exception
     */
    public abstract void makeNetworkMove() throws IOException;

    /**
     * abstract method to send a move to the other connection party
     * @throws IOException exception
     */
    public abstract void sendNetworkMove() throws IOException;

    void useTilesToMovePiece(String networkString){
        List<Object> moveListNetwork = parser.parseInput(networkString);
        Tile startTile = (Tile) moveListNetwork.get(0);
        Tile endTile = (Tile) moveListNetwork.get(1);
        String promotionLetter = (String) moveListNetwork.get(2);
        game.getBoard().movePiece(startTile, endTile, promotionLetter);
    }

}
