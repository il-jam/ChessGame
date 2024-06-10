package chess.cli;

import chess.model.Game;
import chess.model.pieces.Piece;

import static chess.model.Constants.BOARD_LENGTH;

/**
 * The View Class for the CLI
 */
public class CliView {

    /**
     * we need the Game class, so the View can get updated
     */
    public Game game;

    public boolean alreadyPrinted;

    /**
     * constructor of the ViewCLI class
     */
    public CliView(){

    }

    /**
     * method to print out the board in console
     */
    public void drawBoard(){

        for(int i = 0; i < BOARD_LENGTH ; i++) {

            System.out.print(8 - i + "  ");

            for (int j = 0; j < BOARD_LENGTH; j++) {

                Piece pieceOnTile = game.getBoard().getTiles()[i][j].getValue().getPiece();

                if ( pieceOnTile == null){

                    System.out.print(" ");

                }
                else {

                    char pieceChar = pieceOnTile.getConsoleChar(pieceOnTile.getColor());

                    System.out.print(pieceChar + "");

                }

                System.out.print("  ");
            }

            System.out.println();

        }

        System.out.println("   a  b  c  d  e  f  g  h");

    }

    /**
     * Method that prints the current game state
     */
    public void printGameState(){
        if(!alreadyPrinted) {
            System.out.print("Welcome to Chess Match");
            System.out.println();
            alreadyPrinted = true;
        }
        drawBoard();
     }

    public void setGame(Game game) {
        this.game = game;
    }
}
