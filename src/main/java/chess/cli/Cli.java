package chess.cli;

import chess.controller.ClIAIController;
import chess.controller.ClIHumanController;
import chess.controller.ClINetworkController;
import chess.model.ChessColor;
import chess.model.Game;
import chess.model.GameState;
import chess.model.SaveLoadManager;


import java.io.IOException;


import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
public class Cli {
    static boolean humanMove = true;


    static Game game = new Game();
    static CliView cliView = new CliView();
    /**
     * The entry point of the CLI application.
     *
     * @param args The command line arguments passed to the application
     * @throws IOException e
     */
    public static void main(String[] args) throws IOException {

        List<String> arguments = Arrays.asList(args);

        cliView.setGame(game);

        // simple game, for Checker
        if (arguments.contains("--simple")){
            humanRoutine();

        }
        // normal start of CLI game
        else {
            askForLanguage();
            askForLoadGame();
            // if we play HumanVSHuman
            if (vsHuman()){
                if (networkGame()){
                    networkRoutine();
                }
                else{
                    humanRoutine();
                }


            }
            // if we play against Computer
            else if (game.isAIGame()){
                aIRoutine();
            }
            else if (game.isNetworkGame()){
                networkRoutine();
            }
        }
    }

    /**
     * method for setting Game Type
     * @return boolean for Game Type
     */
    private static boolean vsHuman(){
        if (game.german){
            System.out.println("Spiel gegen den Computer ('computer'), oder einen anderen Menschen ('mensch')?");
        }
        else {
            System.out.println("Playing against computer ('computer') or another human ('human')?");
        }

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("human") || input.equals("mensch")){
            return true;
        }
        else if (input.equals("computer")){
            game.setAIGame(true);
            return false;
        }
        else{
            return vsHuman();
        }
    }

    private static boolean networkGame(){
        if (game.german){
            System.out.println("Moechtest du ein Netzwerkspiel spielen? ('ja'/'nein')");
        }
        else {
            System.out.println("Want to play a network game? ('yes'/'no')");
        }
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("yes")|| input.equals("ja")){
            game.setNetworkGame(true);
            return true;
        }
        else if(input.equals("no") || input.equals("nein")){
            game.setNetworkGame(false);
            return false;
        }
        else{
            return networkGame();
        }

    }

    private static boolean gameOver(){
        GameState gameState = GameState.getCurrentGameState();
        return gameState.equals(GameState.STALEMATE) || gameState.equals(GameState.BLACK_WINS) || gameState.equals(GameState.WHITE_WINS);
    }

    private static void aIRoutine(){
        ClIHumanController cliController = new ClIHumanController(game);
        ClIAIController aiController = new ClIAIController(game);
        if (aiController.getAiColor() == ChessColor.White) {
            humanMove = false;
        }
        while (!gameOver()){
            cliView.printGameState();
            if (humanMove) {
                cliController.doUserAction();
                if (cliController.moveHappened){
                    humanMove = false;
                    if (gameOver()){
                        cliView.printGameState();
                    }
                }

            }
            else{
                aiController.doAIAction();
                if (gameOver()){
                    cliView.printGameState();
                }
                humanMove = true;
            }
        }
        cliView.printGameState();
    }

    private static void humanRoutine(){
        ClIHumanController cliController = new ClIHumanController(game);

        while (!gameOver()) {
            cliView.printGameState();
            cliController.doUserAction();
            if (gameOver()){
                cliView.printGameState();
            }
        }
    }
    private static void networkRoutine() throws IOException {

        ClIHumanController cliController = new ClIHumanController(game);
        ClINetworkController clINetworkController = new ClINetworkController(game);

        boolean firstMove = true;


        if (game.getNetworkColor() == ChessColor.White) {
            humanMove = false;
        }
        while (!gameOver()){
            cliView.printGameState();

            if (humanMove) {
                cliController.doUserAction();
                if (cliController.moveHappened){
                    clINetworkController.sendString();
                    humanMove = false;
                    if (gameOver()){
                        cliView.printGameState();
                    }
                }

            }
            else{
                if (cliController.moveHappened|| firstMove){
                    clINetworkController.doNetworkAction();
                    if (gameOver()){
                        cliView.printGameState();
                    }
                    firstMove = false;
                    humanMove = true;
                }
            }
        }
        cliView.printGameState();
    }

    private static void askForLoadGame(){
        try{

            if (game.german){
                System.out.println("Du kannst ein Spiel laden oder speichern. Zu Beginn des Spiels kannst du ein neues Spiel laden, wenn es bereits einen Speicherstand gibt. Ein Spiel kann jeder Zeit mit der Eingabe 'speichern' gespeichert werden.");
                System.out.println("Moechtest du ein Spiel Laden? (ja) / (nein)");
            }
            else {
                System.out.println("You can save & load the game. You can load the game at the start of a new game, if there is already a game saved. You can save it at any point in a game with entering command 'save'." );
                System.out.println("Do you want to load the saved game? (yes) / (no)");
            }



            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equals("yes") || input.equals("ja")){
                System.out.println("Enter the absolute path to your .txt file: You could use something like: " + System.getProperty("user.home") + "\\\\ENTER_HERE_YOUR_FILENAME.txt ");
                input = scanner.nextLine();
                SaveLoadManager saveLoadManager = new SaveLoadManager(game);
                Game loadedGame = saveLoadManager.loadGame(input);
                System.out.println(loadedGame.getCurrentPlayer() + " is next");
                game = loadedGame;
                cliView.setGame(loadedGame);
                if(loadedGame.isAIGame()) {
                    aIRoutine();
                }
                else{
                    humanRoutine();
                }
            }
        }
        catch (Exception e){
            System.out.println("Your input must be wrong. Try again.");
            askForLoadGame();
        }
    }

    private static void askForLanguage(){
        System.out.println("Do you want to play in english 'yes'?");
        System.out.println("Moechtest du in deutsch spielen 'ja'?");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("yes")){
            game.german = false;
        }
        else if (input.equals("ja")){
            game.german = true;

        }
        else{
            askForLanguage();
        }
    }

}
