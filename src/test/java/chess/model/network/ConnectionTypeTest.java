package chess.model.network;

import chess.model.Game;
import chess.model.pieces.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * test Class for ConnectionTeype
 */
public class ConnectionTypeTest {
    Game game = new Game();
    String IP;
    ConnectionType connectionTypeServer;
    ConnectionType connectionTypeClient;

    /**
     * method initConnection, to build a connection before every Test
     * @throws InterruptedException e
     */
    @BeforeEach
    public void initConnection() throws InterruptedException {

        Thread serverThread = new Thread(() -> {
            try {
                connectionTypeServer = new Server(game);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                IP = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            game.setIp(IP);
        });

        Thread clientThread = new Thread(() -> {

            try {
                connectionTypeClient = new Client(game);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        clientThread.start();
        serverThread.join();
        clientThread.join();
    }

    /**
     * test method to send a networkMove, and to execute a Move
     * @throws IOException e
     */
    @Test
    public void sendNetworkMoveTest() throws IOException {
        game.setSendToNetworkString("a2-a3");
        connectionTypeServer.sendNetworkMove();
        connectionTypeClient.makeNetworkMove();
        assertTrue(game.getBoard().getSpecificTile(5,0).getPiece() instanceof Pawn);
        game.setSendToNetworkString("a7-a6");
        connectionTypeClient.sendNetworkMove();
        connectionTypeServer.makeNetworkMove();
        assertTrue(game.getBoard().getSpecificTile(2,0).getPiece() instanceof Pawn);
    }




}
