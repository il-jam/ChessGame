package chess.model.network;

import chess.controller.Parser;
import chess.model.Game;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * public Class which represents the Server part of the network connection
 */
public class Server extends ConnectionType {

    private final Socket socket;


    /**
     * Constructor of Server
     * @param game current game
     * @throws IOException exception
     */
    public Server(Game game) throws IOException {

        super(game);

        this.parser = new Parser(game);

        ServerSocket serverSocket = new ServerSocket(4999);

        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Send this IP to Client: "+ ip);
        socket = serverSocket.accept();

    }

    @Override
    public void makeNetworkMove() throws IOException {
        // gets String from Client
        InputStream in = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(in);
        String networkString = dataInputStream.readUTF();
        // parses the String and moves the Piece
        useTilesToMovePiece(networkString);
        // prints move from Client
        System.out.println();
        System.out.println("Client move was: !" + networkString);

    }

    @Override
    public void sendNetworkMove() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF(game.getSendToNetworkString());
        dataOutputStream.flush();

    }

}
