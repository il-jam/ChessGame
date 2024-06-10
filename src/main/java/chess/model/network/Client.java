package chess.model.network;

import chess.controller.Parser;
import chess.model.Game;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;



/**
 * public Class which represents the Client part of the network connection
 */
public class Client extends ConnectionType {

    private Socket socket;


    /**
     * Constructor of Client
     * @param game current game
     * @throws IOException exception
     */
    public Client(Game game) throws IOException {
        super(game);
        this.parser = new Parser(game);
        String ip = game.getIp();
        if (sendPingRequest(ip)){
            socket = new Socket(ip, 4999);
        }
        else {
            System.out.println("retry ip");
        }
    }

    /**
     * method that should wait for the Server if the Client wants to connect with it via String IP
     * @param ipAddress IP address of Server
     * @return a boolean if connection was granted
     */
    public static boolean sendPingRequest(String ipAddress) {
        try {
            InetAddress enemy = InetAddress.getByName(ipAddress);
            System.out.println("waiting for server response");
            return enemy.isReachable(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void makeNetworkMove() throws IOException {
        // gets String from Server
        InputStream in = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(in);
        String networkString = dataInputStream.readUTF();
        // parses the String and moves the Piece
        useTilesToMovePiece(networkString);
        // prints move from Server
        System.out.println();
        System.out.println("Server move was: !" + networkString);

    }

    @Override
    public void sendNetworkMove() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF(game.getSendToNetworkString());
        dataOutputStream.flush();
    }

}
