package chess.controller;

import chess.model.ai.AIType;
import chess.model.ai.MInMaxAI;
import chess.model.ai.SimpleAI;
import chess.model.ChessColor;
import chess.model.Game;

import java.util.Scanner;

/**
 * the Class of the ClIAIController. It's responsible for the AI turn in CLI
 */
public class ClIAIController {

    public Game game;

    private final AIType aiType;

    private final ChessColor aiColor;


    /**
     * Constructor of the CLIAIController Class
     * @param game current Game
     */
    public ClIAIController(Game game){
        this.game = game;
        chooseYourColor();
        this.aiColor = game.getAIColor();
        boolean isBlack = aiColor == ChessColor.Black;
        if (chooseAIType()){
            this.aiType = new SimpleAI(game.getBoard(), aiColor, isBlack);
        }
        else{
            this.aiType = new MInMaxAI(game.getBoard(), aiColor , isBlack);
        }
    }

    /**
     * Method which combines the AIMove with the Game routine logic
     */
    public void doAIAction(){
        aiType.makeAIMove();
        game.nextTurn();
        System.out.println();
    }

    /**
     * Method which lets the user choose the difficulty of AI
     * @return a boolean which decides the AIType in the Constructor
     */
    private boolean chooseAIType(){
        if (game.german){
            System.out.println("Moechtest du gegen eine einfache KI spielen 'einfach', oder gegen eine verbesserte KI 'verbessert' spielen?");
        }
        else {
            System.out.println("do you want to play against a simple ai 'simple', or against a ai using min-max algorithm 'minmax'?");
        }

        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if (answer.equals("simple") || answer.equals("einfach")){
            return true;
        }
        else if (answer.equals("minmax") || answer.equals("verbessert")){
            chooseAIMaxDepth();
            return false;
        }
        else{
            return chooseAIType();
        }
    }

    /**
     * method which lets the user choose his color, and by doing that he automatically sets the color of the AI
     */
    private void chooseYourColor(){
        if (game.german){
            System.out.println("Spielst du als Weiss 'weiss' oder Schwarz 'schwarz'?");
        }
        else {
            System.out.println("do you want to play as white 'white', or black 'black'?");
        }
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if (answer.equals("white") || answer.equals("weiss")){
            game.setAIColor(ChessColor.Black);

        }
        else if (answer.equals("black") || answer.equals("schwarz")){
            game.setAIColor(ChessColor.White);

        }
        else {
            chooseYourColor();
        }
    }

    /**
     * method to let the user of ClI decide which depth the AI has
     */
    public void chooseAIMaxDepth(){
        if (game.german){
            System.out.println("Waehle den Schwierigkeitsgrad! ('1','2','3','4')");
        }
        else {
            System.out.println("Choose the maxDepth for minmax search! ('1','2','3','4')");
        }

        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        switch (answer) {
            case "1":
                game.getBoard().maxDepth = 0;
                break;
            case "2":
                game.getBoard().maxDepth = 1;
                break;
            case "3":
                game.getBoard().maxDepth = 2;
                break;
            case "4":
                game.getBoard().maxDepth = 3;
            default:
                chooseAIMaxDepth();
                break;
        }
    }

    public ChessColor getAiColor() {
        return aiColor;
    }
}
