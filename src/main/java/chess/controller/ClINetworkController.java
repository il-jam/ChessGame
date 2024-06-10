package chess.controller;

import chess.model.ChessColor;
import chess.model.Game;
import chess.model.network.Client;
import chess.model.network.ConnectionType;
import chess.model.network.Server;

import java.io.IOException;
import java.util.Scanner;

/**
 * controller for the network actions in Cli
 */
public class ClINetworkController {
    public Game game;

    public ConnectionType connectionType;

    /**
     * constructor of the CliNetworkController
     * @param game current Game
     * @throws IOException exception
     */
    public ClINetworkController(Game game) throws IOException {
        this.game = game;
        if (chooseNetworkType()){
            this.connectionType = new Server(game);
            game.setCurrentPlayer(ChessColor.White);
        }
        else{
            this.connectionType = new Client(game);
            game.setCurrentPlayer(ChessColor.Black);
        }
    }

    /**
     * method which makes a Network move and then calls nextTurn() method in game
     * @throws IOException exception
     */
    public void doNetworkAction() throws IOException {

        connectionType.makeNetworkMove();
        if (connectionType instanceof Server){
            game.setCurrentPlayer(ChessColor.Black);
        }
        else if (connectionType instanceof Client){
            game.setCurrentPlayer(ChessColor.White);
        }
        game.nextTurn();
        System.out.println();
    }

    /**
     * method to send the last move of the player to the other connection party
     * @throws IOException exception
     */
    public void sendString() throws IOException {
        connectionType.sendNetworkMove();
    }

    /**
     * method to let the user decide if he wants to host the game or joins it
     * @return the boolean depending on decision made by user
     */
    public boolean chooseNetworkType(){
        if (game.german){
            System.out.println("Wenn  noch kein Server gestartet wurde, dann tue dies bitte, indem du 'server' eingibst. Sonst koennte die Verbindung fehlschlagen!");
            System.out.println("Moechtest du als Server die Verbindung starten ('server') oder als ein Client einen bestehenden Server beitreten ('client')?");
        }
        else {
            System.out.println("If you haven't setup a Server, you can do that by entering 'server', otherwise the connection could fail!");
            System.out.println("Do you want to launch the 'server' or connect as 'client'?");
        }
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if (answer.equals("server")){

            game.setNetworkColor(ChessColor.Black);
            return true;
        }
        else if (answer.equals("client")){
            System.out.println("Enter IP:");
            Scanner scanner2 = new Scanner(System.in);
            game.setIp(scanner2.nextLine());
            game.setNetworkColor(ChessColor.White);
            return false;
        }
        else {
            return chooseNetworkType();
        }
    }

}
